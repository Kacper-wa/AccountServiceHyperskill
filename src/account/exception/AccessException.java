package account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class AccessException extends RuntimeException {
    public AccessException(String message) {
        super(message);
    }
}
