package account.security;

import account.util.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String ADMIN = UserRole.ADMINISTRATOR.name();
    private static final String USER = UserRole.USER.name();
    private static final String ACCOUNTANT = UserRole.ACCOUNTANT.name();
    private static final String AUDITOR = UserRole.AUDITOR.name();

    private final MyAuthorizationFailureHandler authorizationFailureHandler;

    public SecurityConfig(MyAuthorizationFailureHandler authorizationFailureHandler) {
        this.authorizationFailureHandler = authorizationFailureHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.httpBasic()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint()) // Handle auth error
                .and()
                .csrf().disable().headers().frameOptions().disable() // for Postman, the H2 console
                .and()
                .authorizeRequests() // manage access
                .antMatchers("/h2/**").permitAll()
                .antMatchers("/actuator/shutdown").permitAll()
                .antMatchers(HttpMethod.POST, "/api/auth/signup/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/auth/changepass/**").hasAnyRole(ADMIN, USER, ACCOUNTANT)
                .antMatchers(HttpMethod.GET, "/api/empl/payment/**", "/api/empl/payment").hasAnyRole(USER, ACCOUNTANT)
                .antMatchers(HttpMethod.POST, "/api/acct/payments/**", "/api/acct/payments").hasRole(ACCOUNTANT)
                .antMatchers(HttpMethod.PUT, "/api/acct/payments/**").hasRole(ACCOUNTANT)
                .antMatchers(HttpMethod.GET, "/api/admin/user/**").hasRole(ADMIN)
                .antMatchers(HttpMethod.DELETE, "/api/admin/user/**").hasRole(ADMIN)
                .antMatchers(HttpMethod.PUT, "/api/admin/user/role/**").hasRole(ADMIN)
                .antMatchers(HttpMethod.PUT, "/api/admin/user/access/**").hasRole(ADMIN)
                .antMatchers(HttpMethod.GET, "/api/security/events/**").hasRole(AUDITOR)
                .antMatchers(HttpMethod.GET, "/api/auth/signup/**").hasRole(AUDITOR)
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(authorizationFailureHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();
    }

}