package account.request;

public record UpdateSalaryRequest(
        String employee,
        String period,
        Long salary) {
}
