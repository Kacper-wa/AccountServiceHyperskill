package account.service;

import account.entity.User;
import account.exception.PasswordInvalidException;
import account.exception.UserAlreadyExistsException;
import account.repository.UserRepository;
import account.request.SignUpRequest;
import account.response.ChangePasswordResponse;
import account.response.SignUpResponse;
import account.security.PasswordEncoderConfig;
import account.security.UserDetailsImpl;
import account.util.SecurityEvent;
import account.util.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private static final UserRole ADMINISTRATOR = UserRole.ROLE_ADMINISTRATOR;
    private static final UserRole USER = UserRole.ROLE_USER;

    private final UserRepository userRepository;
    private final PasswordEncoderConfig passwordEncoderConfig;
    private final LogService logService;

    public UserService(
            UserRepository userRepository,
            PasswordEncoderConfig passwordEncoderConfig,
            LogService logService) {
        this.userRepository = userRepository;
        this.passwordEncoderConfig = passwordEncoderConfig;
        this.logService = logService;
    }

    @Override
    public UserDetailsImpl loadUserByUsername(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserDetailsImpl(user);
    }

    @Transactional
    public ResponseEntity<SignUpResponse> signUp(SignUpRequest signUpRequest) {
        if (!userRepository.existsByEmailIgnoreCase(signUpRequest.email())) {
            List<User> users = userRepository.findAll();
            UserRole userRole;
            User user = new User();
            if (users.isEmpty()) {
                userRole = ADMINISTRATOR;
            } else {
                userRole = USER;
            }
            validatePassword(signUpRequest.password());
            String encodedPassword = passwordEncoderConfig.passwordEncoder().encode(signUpRequest.password());
            user.mapToUser(signUpRequest, encodedPassword);
            user.addRole(userRole);
            userRepository.save(user);
            logService.addEvent(SecurityEvent.CREATE_USER, "Anonymous", user.getEmail(), "/api/auth/signup");
            return new ResponseEntity<>(SignUpResponse.response(user), HttpStatus.OK);
        } else {
            throw new UserAlreadyExistsException("User exist!");
        }
    }

    @Transactional
    public ResponseEntity<?> changePass(UserDetails loggedUser, String newPassword) {
        validatePassword(newPassword);
        String userEmail = loggedUser.getUsername();
        User user = userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + loggedUser.getUsername()));
        // must use raw password and encoded password from database to compare
        if (passwordEncoderConfig.passwordEncoder().matches(newPassword, user.getPassword())) {
            throw new PasswordInvalidException("The passwords must be different!");
        }
        String encodedNewPassword = passwordEncoderConfig.passwordEncoder().encode(newPassword);
        user.setPassword(encodedNewPassword);
        userRepository.save(user);
        logService.addEvent(SecurityEvent.CHANGE_PASSWORD, user.getEmail(), user.getEmail(), "/api/auth/changepass");
        return new ResponseEntity<>(
                new ChangePasswordResponse(userEmail, "The password has been updated successfully"),
                HttpStatus.OK);
    }

    private void validatePassword(String password) {
        if (passwordEncoderConfig.getBreachedPasswords().contains(password)) {
            throw new PasswordInvalidException("The password is in the hacker's database!");
        }
        if (!password.matches("\\S{12,}")) {
            throw new PasswordInvalidException("Password length must be 12 chars minimum!");
        }
    }
}
