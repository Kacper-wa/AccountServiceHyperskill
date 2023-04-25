package account.response;

import account.entity.User;
import account.util.UserRole;

import java.util.List;

public record SignUpResponse(
        Long id,
        String name,
        String lastname,
        String email,
        List<UserRole> roles) {

    public static SignUpResponse response(User user) {
        return new SignUpResponse(
                user.getId(),
                user.getName(),
                user.getLastname(),
                user.getEmail(),
                user.getRoles());
    }
}
