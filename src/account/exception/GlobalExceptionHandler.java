package account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PasswordInvalidException.class)
    public ResponseEntity<?> handlePasswordInvalidException(PasswordInvalidException e, HttpServletRequest request) {
        return new ResponseEntity<>(badRequestMap(e, request), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExistsException(UserAlreadyExistsException e, HttpServletRequest request) {
        return new ResponseEntity<>(badRequestMap(e, request), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PeriodInvalidException.class)
    public ResponseEntity<?> handlePeriodInvalidException(PeriodInvalidException e, HttpServletRequest request) {
        return new ResponseEntity<>(badRequestMap(e, request), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SalaryInvalidException.class)
    public ResponseEntity<?> handleSalaryInvalidException(SalaryInvalidException e, HttpServletRequest request) {
        return new ResponseEntity<>(badRequestMap(e, request), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidPathVariable.class)
    public ResponseEntity<?> handleInvalidPathVariable(InvalidPathVariable e, HttpServletRequest request) {
        return new ResponseEntity<>(badRequestMap(e, request), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RoleException.class)
    public ResponseEntity<?> handleRoleException(RoleException e, HttpServletRequest request) {
        return new ResponseEntity<>(badRequestMap(e, request), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AccessException.class)
    public ResponseEntity<?> handleAccessException(AccessException e, HttpServletRequest request) {
        return new ResponseEntity<>(badRequestMap(e, request), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException e, HttpServletRequest request) {
        return new ResponseEntity<>(notFoundMap(e, request), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<?> handleRoleNotFoundException(RoleNotFoundException e, HttpServletRequest request) {
        return new ResponseEntity<>(notFoundMap(e, request), HttpStatus.NOT_FOUND);
    }

    private Map<String, Object> badRequestMap(Exception ex, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String date = formatter.format(new Date());
        response.put("timestamp", date);
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        response.put("message", ex.getMessage());
        response.put("path", request.getRequestURI());
        return response;
    }

    private Map<String, Object> notFoundMap(Exception ex, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String date = formatter.format(new Date());
        response.put("timestamp", date);
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("error", HttpStatus.NOT_FOUND.getReasonPhrase());
        response.put("message", ex.getMessage());
        response.put("path", request.getRequestURI());
        return response;
    }
}
