package account.service;

import account.entity.Event;
import account.repository.EventRepository;
import account.util.SecurityEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    private final EventRepository eventRepository;

    public LogService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void addEvent(SecurityEvent event, String subject, String object, String path) {
        eventRepository.save(new Event(event, subject, object, path));
    }

    public ResponseEntity<?> getAllEvents() {
        return new ResponseEntity<>(eventRepository.findAll(), HttpStatus.OK);
    }
}

