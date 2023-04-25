package account.service;

import account.entity.User;
import account.exception.AccessException;
import account.exception.RoleException;
import account.exception.UserNotFoundException;
import account.repository.UserRepository;
import account.request.AccessRequest;
import account.request.RoleRequest;
import account.response.AccessResponse;
import account.response.DeleteUserResponse;
import account.response.RoleResponse;
import account.response.UserListResponse;
import account.util.SecurityEvent;
import account.util.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static account.util.UserRole.ROLE_ADMINISTRATOR;
import static account.util.RoleOperation.REMOVE;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final LogService logService;

    public AdminService(
            UserRepository userRepository,
            LogService logService) {
        this.userRepository = userRepository;
        this.logService = logService;
    }

    @Transactional
    public ResponseEntity<?> updateRole(RoleRequest roleRequest, String adminEmail) {
        User user = userRepository.findByEmailIgnoreCase(roleRequest.user())
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
        UserRole requestedRole = UserRole.getRole(roleRequest.role());
        List<UserRole> userRoles = user.getRoles();
        String role = roleRequest.role();

        checkIfUserIsNotAdministrator(user, roleRequest);
        checkIfUserCanCombineRoles(requestedRole, user);

        switch (roleRequest.operation()) {
            case GRANT -> {
                if (!userRoles.contains(requestedRole)) {
                    user.addRole(requestedRole);
                    logService.addEvent(SecurityEvent.GRANT_ROLE, adminEmail,
                            String.format("Grant role %s to %s", role, roleRequest.user().toLowerCase()), "/api/admin/user/role");
                }
            }
            case REMOVE -> {
                checkIfUserHasRole(requestedRole, user);
                user.removeRole(requestedRole);
                checkIfUserHasAtLeastOneRole(user);
                logService.addEvent(SecurityEvent.REMOVE_ROLE, adminEmail,
                        String.format("Remove role %s from %s", role, roleRequest.user().toLowerCase()), "/api/admin/user/role");
            }
            default -> throw new IllegalArgumentException("Invalid operation: " + roleRequest.operation());
        }

        userRepository.save(user);
        return new ResponseEntity<>(RoleResponse.response(user), HttpStatus.OK);
    }

    public ResponseEntity<?> getAllUsers() {
        List<UserListResponse> responseList = userRepository.findAll()
                .stream()
                .map(UserListResponse::response)
                .toList();
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> deleteUser(String email, String adminEmail) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
        if (user.getRoles().contains(ROLE_ADMINISTRATOR)) {
            throw new RoleException("Can't remove ADMINISTRATOR role!");
        }
        userRepository.delete(user);
        logService.addEvent(SecurityEvent.DELETE_USER, adminEmail, email, "/api/admin/user");
        return new ResponseEntity<>(DeleteUserResponse.response(user), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> updateAccess(AccessRequest accessRequest, String adminEmail) {
        User user = userRepository.findByEmailIgnoreCase(accessRequest.user())
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
        String userEmail = user.getEmail();
        checkIfUserIsNotAdministrator(user, accessRequest);
        if (accessRequest.operation().equals("LOCK")) {
            user.setAccountNonLocked(false);
            logService.addEvent(SecurityEvent.LOCK_USER, adminEmail,
                    String.format("Lock user %s", userEmail), "/api/admin/user/access");

        } else if (accessRequest.operation().equals("UNLOCK")) {
            user.setAccountNonLocked(true);
            user.setFailedAttempt(0);
            logService.addEvent(SecurityEvent.UNLOCK_USER, adminEmail,
                    String.format("Unlock user %s", userEmail), "/api/admin/user/access");
        } else {
            throw new AccessException("Invalid operation!");
        }
        String status = user.isAccountNonLocked() ? "unlocked!" : "locked!";
        userRepository.save(user);
        return new ResponseEntity<>(AccessResponse.response(userEmail, status),HttpStatus.OK);
    }

    private void checkIfUserHasRole(UserRole userRole, User user) {
        if (!user.getRoles().contains(userRole)) {
            throw new RoleException("The user does not have a role!");
        }
    }

    private void checkIfUserHasAtLeastOneRole(User user) {
        if (user.getRoles().isEmpty()) {
            throw new RoleException("The user must have at least one role!");
        }
    }

    private void checkIfUserIsNotAdministrator(User user, RoleRequest roleRequest) {
        if (user.getRoles().contains(ROLE_ADMINISTRATOR) && roleRequest.operation().equals(REMOVE)) {
            throw new RoleException("Can't remove ADMINISTRATOR role!");
        }
    }

    private void checkIfUserIsNotAdministrator(User user, AccessRequest accessRequest) {
        if (user.getRoles().contains(ROLE_ADMINISTRATOR) && accessRequest.operation().equals("LOCK")) {
            throw new AccessException("Can't lock the ADMINISTRATOR!");
        }
    }

    private void checkIfUserCanCombineRoles(UserRole userRole, User user) {
        if (user.getRoles().contains(ROLE_ADMINISTRATOR) || userRole.equals(ROLE_ADMINISTRATOR)) {
            throw new RoleException("The user cannot combine administrative and business roles!");
        }
    }

}