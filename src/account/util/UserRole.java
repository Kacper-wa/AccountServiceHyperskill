package account.util;

import account.exception.RoleNotFoundException;

public enum UserRole {
    USER,
    ACCOUNTANT,
    ADMINISTRATOR,
    AUDITOR,

    ROLE_USER,
    ROLE_ACCOUNTANT,
    ROLE_ADMINISTRATOR,
    ROLE_AUDITOR;

    public static UserRole getRole(String role) {
        return switch (role) {
            case "USER" -> ROLE_USER;
            case "ACCOUNTANT" -> ROLE_ACCOUNTANT;
            case "ADMINISTRATOR" -> ROLE_ADMINISTRATOR;
            case "AUDITOR" -> ROLE_AUDITOR;
            default -> throw new RoleNotFoundException("Role not found!");
        };
    }
}
