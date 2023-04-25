package account.repository;

import account.entity.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    List<Payroll> findPayrollsByUserIdOrderByPeriodDesc(Long id);
    Payroll findPayrollByUserIdAndPeriod(Long id, String period);

}
