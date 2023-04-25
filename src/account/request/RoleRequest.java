package account.request;

import account.util.RoleOperation;

public record RoleRequest(
        String user,
        String role,
        RoleOperation operation
) {
}
