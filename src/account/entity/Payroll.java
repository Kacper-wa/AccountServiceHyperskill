package account.entity;

import account.request.PayrollRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payrolls")
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String lastname;
    private String period;
    private Long salary;

    @ManyToOne(
            cascade = {CascadeType.DETACH, CascadeType.MERGE,
                    CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public static Payroll mapToPayroll(User user, PayrollRequest request) {
        Payroll payroll = new Payroll();
        payroll.setName(user.getName());
        payroll.setLastname(user.getLastname());
        payroll.setPeriod(request.period());
        payroll.setSalary(request.salary());
        payroll.setUser(user);
        return payroll;
    }
}