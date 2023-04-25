package account.request;

import javax.validation.constraints.NotBlank;

public record PayrollRequest (
        @NotBlank
        String employee,
        @NotBlank
        String period,
        @NotBlank
        Long salary){
}
