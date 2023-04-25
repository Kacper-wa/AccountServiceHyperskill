package account.response;

public record AccessResponse(
        String status
) {
    public static AccessResponse response(String user, String status) {
        return new AccessResponse("User " + user.toLowerCase() + " " + status );
    }
}
