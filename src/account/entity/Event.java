package account.entity;

import account.util.SecurityEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;
    @Enumerated(EnumType.STRING)
    private SecurityEvent action;
    private String subject;
    private String object;
    private String path;

    public Event(
            SecurityEvent action,
            String subject,
            String object,
            String path) {
        this.date = new Date();
        this.action = action;
        this.subject = subject;
        this.object = object;
        this.path = path;
    }
}