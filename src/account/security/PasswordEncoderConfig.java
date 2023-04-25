package account.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Configuration
public class PasswordEncoderConfig {

    private final List<String> breachedPasswords = List.of(
            "PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch",
            "PasswordForApril", "PasswordForMay", "PasswordForJune", "PasswordForJuly",
            "PasswordForAugust", "PasswordForSeptember", "PasswordForOctober",
            "PasswordForNovember", "PasswordForDecember");

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2A, 13);
    }

    public List<String> getBreachedPasswords() {
        return breachedPasswords;
    }
}