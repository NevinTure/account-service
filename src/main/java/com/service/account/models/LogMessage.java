package com.service.account.models;

import com.service.account.util.EventName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "log_messages")
public class LogMessage {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_message_id")
    private long id;

    @Getter
    @Setter
    @Column(name = "event_name")
    @Enumerated(EnumType.STRING)
    private EventName eventName;

    @Getter
    @Setter
    @Column(name = "subject")
    private String subject;

    @Getter
    @Setter
    @Column(name = "object")
    private String object;

    @Getter
    @Setter
    @Column(name = "api_path")
    private String path;

    @Getter
    @Setter
    @Column(name = "date")
    private LocalDate date;

    public LogMessage(String path) {
        this.path = path;
        this.date = LocalDate.now();
    }
}
