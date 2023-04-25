package account.entity;


import account.request.SignUpRequest;
import account.util.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String name;
    private String lastname;
    private String email;
    private String password;
    // account is unlocked by default
    private boolean accountNonLocked = true;

    @OneToMany(
            mappedBy = "user",
            cascade = {CascadeType.DETACH, CascadeType.MERGE,
                    CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    List<Payroll> payrolls;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "userRoles")
    private List<UserRole> roles;
    private int failedAttempt = 0;

    public void mapToUser(SignUpRequest signUpRequest, String password) {
        this.name = signUpRequest.name();
        this.lastname = signUpRequest.lastname();
        this.email = signUpRequest.email();
        this.password = password;
    }

    public void addRole(UserRole userRole) {
        if (roles == null) {
            roles = new ArrayList<>();
        }
        roles.add(userRole);
    }

    public String getEmail() {
        return email.toLowerCase();
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public void removeRole(UserRole userRole) {
        roles.remove(userRole);
    }

    public boolean hasRole(String role) {
        return roles.stream().anyMatch(userRole -> userRole.name().equals(role));
    }
}
