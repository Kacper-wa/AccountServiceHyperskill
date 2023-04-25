package account.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public record SignUpRequest(
        @NotBlank
        String name,

        @NotBlank
        String lastname,

        @Email
        @Pattern(regexp = "\\w+(@acme.com)$")
        @NotBlank
        String email,

        @NotBlank
        String password) {
}
