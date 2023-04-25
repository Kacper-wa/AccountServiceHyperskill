package account.response;

import account.entity.User;

public record DeleteUserResponse(
        String user,
        String status
) {
    public static DeleteUserResponse response(User user) {
        return new DeleteUserResponse(user.getEmail(), "Deleted successfully!");
    }
}
