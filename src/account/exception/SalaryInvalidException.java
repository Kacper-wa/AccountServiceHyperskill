package account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class SalaryInvalidException extends RuntimeException {
    public SalaryInvalidException(String message) {
        super(message);
    }
}
