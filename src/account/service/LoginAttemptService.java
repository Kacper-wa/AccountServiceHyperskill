package account.service;

import account.entity.User;
import account.repository.UserRepository;
import account.util.SecurityEvent;
import account.util.UserRole;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginAttemptService {

    public static final int MAX_FAILED_ATTEMPTS = 4;
    private final UserRepository userRepository;
    private final LogService logService;

    public LoginAttemptService(UserRepository userRepository, LogService logService) {
        this.userRepository = userRepository;
        this.logService = logService;
    }

    public void loginSuccess(String email) {
        User user = userRepository.findByEmailIgnoreCase(email).orElseGet(User::new);
        user.setFailedAttempt(0);
        userRepository.save(user);
    }

    public void loginFailure(String email, String uri) {
        logFailedLoginEvent(email, uri);
        Optional<User> userOptional = userRepository.findByEmailIgnoreCase(email);
        userOptional.ifPresent(user -> {
            if (!user.hasRole(UserRole.ROLE_ADMINISTRATOR.name())) {
                user.setFailedAttempt(user.getFailedAttempt() + 1);
                if (user.getFailedAttempt() > MAX_FAILED_ATTEMPTS) {
                    lockUser(email, uri);
                    user.setAccountNonLocked(false);
                }
                userRepository.save(user);
            }
        });
    }

    private void logFailedLoginEvent(String email, String uri) {
        logService.addEvent(SecurityEvent.LOGIN_FAILED, email, uri, uri);
    }

    private void lockUser(String email, String uri) {
        logService.addEvent(SecurityEvent.BRUTE_FORCE, email, uri, uri);
        logService.addEvent(SecurityEvent.LOCK_USER, email, String.format("Lock user %s", email), uri);
    }

}
