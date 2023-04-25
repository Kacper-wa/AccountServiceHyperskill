package account.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = formatter.format(new Date());
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("timestamp", formattedDate);
        responseMap.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        responseMap.put("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
        responseMap.put("message", "User is not authorized, bad credentials or user is locked");
        responseMap.put("path", request.getRequestURI());
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(responseMap);
        response.getWriter().write(json);
    }
}