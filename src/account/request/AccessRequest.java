package account.request;

public record AccessRequest(
        String user,
        String operation
) {
}
