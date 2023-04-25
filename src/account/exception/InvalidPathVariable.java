package account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidPathVariable extends RuntimeException {
    public InvalidPathVariable(String message) {
        super(message);
    }
}
