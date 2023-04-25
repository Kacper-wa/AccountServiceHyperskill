package account.controller;

import account.service.LogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuditorController {

    private final LogService logService;

    public AuditorController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("/api/security/events")
    public ResponseEntity<?> getSecurityEvents() {
        return logService.getAllEvents();
    }
}
