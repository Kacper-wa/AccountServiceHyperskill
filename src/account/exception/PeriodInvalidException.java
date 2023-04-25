package account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Error!")
public class PeriodInvalidException extends RuntimeException {
    public PeriodInvalidException(String message) {
        super(message);
    }
}
