package account.controller;

import account.request.ChangePasswordRequest;
import account.request.SignUpRequest;
import account.response.SignUpResponse;
import account.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/auth/signup")
    public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        return userService.signUp(signUpRequest);
    }

    @PostMapping("/api/auth/changepass")
    public ResponseEntity<?> changePass(@Valid @RequestBody ChangePasswordRequest changePasswordRequest,
                                        @AuthenticationPrincipal UserDetails loggedUser) {
        return userService.changePass(loggedUser, changePasswordRequest.newPassword());
    }

}
