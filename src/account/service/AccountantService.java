package account.service;

import account.entity.Payroll;
import account.entity.User;
import account.exception.PeriodInvalidException;
import account.exception.SalaryInvalidException;
import account.exception.UserNotFoundException;
import account.repository.PayrollRepository;
import account.repository.UserRepository;
import account.request.PayrollRequest;
import account.request.UpdateSalaryRequest;
import account.response.PayrollResponse;
import account.response.UpdateSalaryResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountantService {

    private static final String PERIOD_REGEX = "^(0[1-9]|1[0-2])-(20\\d{2}|[3-9]\\d)$";

    private final UserRepository userRepository;
    private final PayrollRepository payrollRepository;

    public AccountantService(UserRepository userRepository, PayrollRepository payrollRepository) {
        this.userRepository = userRepository;
        this.payrollRepository = payrollRepository;
    }

    public ResponseEntity<?> getPayrolls(String email, String period) {
        if (period != null) {
            if (!period.matches(PERIOD_REGEX)) {
                throw new PeriodInvalidException("Error!");
            }
            return getPayrollByPeriod(email, period);
        }
        Long id = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found!"))
                .getId();
        List<Payroll> payrolls = payrollRepository.findPayrollsByUserIdOrderByPeriodDesc(id);
        return new ResponseEntity<>(PayrollResponse.response(payrolls), HttpStatus.OK);
    }

    public ResponseEntity<?> getPayrollByPeriod(String email, String period) {
        Long id = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException("User not found!"))
                .getId();
        Payroll payroll = payrollRepository.findPayrollByUserIdAndPeriod(id, period);
        return new ResponseEntity<>(PayrollResponse.response(payroll), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> createPayrolls(List<PayrollRequest> request) {
        if (searchForErrors(request) != null) {
            throw new PeriodInvalidException(searchForErrors(request));
        }
        String period = "";
        for (PayrollRequest payrollRequest : request) {
            if (payrollRequest.period().equals(period)) {
                throw new PeriodInvalidException("No duplicate period!");
            }
            period = payrollRequest.period();
            User user = userRepository.findByEmailIgnoreCase(payrollRequest.employee())
                    .orElseThrow(() -> new UserNotFoundException("User not found!"));
            Payroll payroll = Payroll.mapToPayroll(user, payrollRequest);
            payrollRepository.save(payroll);
        }
        return new ResponseEntity<>(new UpdateSalaryResponse("Added successfully!"),
                HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> updateSalary(UpdateSalaryRequest request) {
        if (!request.period().matches(PERIOD_REGEX)) {
            throw new PeriodInvalidException("Error!");
        }
        if (request.salary() < 0) {
            throw new SalaryInvalidException("Wrong salary in request body!");
        }
        if (payrollExists(request)) {
            Long id = userRepository.findByEmailIgnoreCase(request.employee())
                    .orElseThrow(() -> new RuntimeException("User not found!"))
                    .getId();
            List<Payroll> payrolls = payrollRepository.findPayrollsByUserIdOrderByPeriodDesc(id);
            for (Payroll payroll : payrolls) {
                if (payroll.getPeriod().equals(request.period())) {
                    payroll.setSalary(request.salary());
                    payrollRepository.save(payroll);
                }
            }
            return new ResponseEntity<>(new UpdateSalaryResponse("Updated successfully!"),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new UpdateSalaryResponse("Payroll not found!"),
                    HttpStatus.NOT_FOUND);
        }
    }

    private boolean payrollExists(UpdateSalaryRequest request) {
        Long id = userRepository.findByEmailIgnoreCase(request.employee())
                .orElseThrow(() -> new UserNotFoundException("User not found!"))
                .getId();
        return payrollRepository.findPayrollsByUserIdOrderByPeriodDesc(id).stream()
                .anyMatch(payroll -> payroll.getPeriod().equals(request.period()));
    }

    private String searchForErrors(List<PayrollRequest> request) {
        Map<Integer, String> errors = new HashMap<>();
        for (int i = 0; i < request.size(); i++) {
            if (request.get(i).salary() < 0) {
                errors.put(i, "salary: Salary must be non negative!");
            }
            if (!request.get(i).period().matches(PERIOD_REGEX)) {
                errors.put(i, "period: Wrong date!");
            }
        }
        if (errors.size() > 0) {
            StringBuilder message = new StringBuilder();
            int i = 0;
            for (Map.Entry<Integer, String> entry : errors.entrySet()) {
                message.append("payment[").append(entry.getKey()).append("].").append(entry.getValue());
                if (i < errors.size() - 1)
                    message.append(", ");
                i++;
            }
            return message.toString();
        }
        return null;
    }
}