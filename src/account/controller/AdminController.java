package account.controller;

import account.request.AccessRequest;
import account.request.RoleRequest;
import account.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PutMapping("/api/admin/user/role")
    public ResponseEntity<?> updateRole(@Valid @RequestBody RoleRequest request,
                                        @AuthenticationPrincipal UserDetails loggedUser) {
        return adminService.updateRole(request, loggedUser.getUsername());
    }

    @GetMapping("/api/admin/user")
    public ResponseEntity<?> getAllUsers() {
        return adminService.getAllUsers();
    }

    @DeleteMapping("/api/admin/user/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable String email,
                                        @AuthenticationPrincipal UserDetails loggedUser) {
        return adminService.deleteUser(email, loggedUser.getUsername());
    }

    @PutMapping("/api/admin/user/access")
    public ResponseEntity<?> updateAccess(@Valid @RequestBody AccessRequest AccessRequest,
                                          @AuthenticationPrincipal UserDetails loggedUser) {
        return adminService.updateAccess(AccessRequest, loggedUser.getUsername());
    }


}
