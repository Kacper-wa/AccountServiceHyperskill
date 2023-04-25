package account.controller;

import account.request.PayrollRequest;
import account.request.UpdateSalaryRequest;
import account.service.AccountantService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AccountantController {

    private final AccountantService accountantService;

    public AccountantController(AccountantService accountantService) {
        this.accountantService = accountantService;
    }

    @GetMapping("/api/empl/payment")
    public ResponseEntity<?> getPayrolls(@AuthenticationPrincipal UserDetails userDetails,
                                         @Valid @RequestParam(required = false) String period) {
        return accountantService.getPayrolls(userDetails.getUsername(), period);
    }

    @PostMapping("/api/acct/payments")
    public ResponseEntity<?> createPayrolls(@Valid @RequestBody List<PayrollRequest> request) {
        return accountantService.createPayrolls(request);
    }

    @PutMapping("/api/acct/payments")
    public ResponseEntity<?> updateSalary(@Valid @RequestBody UpdateSalaryRequest request) {
        return accountantService.updateSalary(request);
    }



}
