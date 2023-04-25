package account.response;

import account.entity.Event;
import account.util.SecurityEvent;

import java.util.Date;

public record EventResponse(
        Date date,
        SecurityEvent action,
        String subject,
        String object,
        String path
) {
    public static EventResponse response(Event event) {
        return new EventResponse(
                event.getDate(),
                event.getAction(),
                event.getSubject(),
                event.getObject(),
                event.getPath());
    }
}
