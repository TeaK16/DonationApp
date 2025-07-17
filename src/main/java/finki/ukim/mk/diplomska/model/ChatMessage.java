package finki.ukim.mk.diplomska.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class ChatMessage {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private UUID id;


    @ManyToOne
    private ChatRoom chatRoom;

    @ManyToOne
    private ApplicationUser sender;

    @ManyToOne
    private ApplicationUser recipient;

    private String content;

    private LocalDateTime localDateTime;

    public ChatMessage() {
    }

    public ChatMessage(ChatRoom chatRoom, ApplicationUser sender, ApplicationUser recipient, String content, LocalDateTime localDateTime) {
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.localDateTime = LocalDateTime.now();
    }
}
