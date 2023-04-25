package account.security;

import account.service.LogService;
import account.util.SecurityEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MyAuthorizationFailureHandler extends AccessDeniedHandlerImpl {

    private final LogService logService;

    public MyAuthorizationFailureHandler(LogService logService) {
        this.logService = logService;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        logService.addEvent(SecurityEvent.ACCESS_DENIED, SecurityContextHolder.getContext().getAuthentication().getName(),
                request.getRequestURI(), request.getRequestURI());
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = formatter.format(new Date());
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", formattedDate);
        errorResponse.put("status", HttpServletResponse.SC_FORBIDDEN);
        errorResponse.put("error", HttpStatus.FORBIDDEN.getReasonPhrase());
        errorResponse.put("message", "Access Denied!");
        errorResponse.put("path", request.getRequestURI());
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(errorResponse);
        response.getWriter().write(json);
    }
}
