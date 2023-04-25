package account.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public record ChangePasswordRequest(
        @NotNull
        @JsonProperty("new_password")
        String newPassword) {}
