package finki.ukim.mk.diplomska.model;


import jakarta.persistence.Entity;
import jakarta.persistence.*;

import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
public class ChatRoom {


    @Id
    private String chatId;
    private LocalDateTime dateOfCreation;
    public ChatRoom() {
        this.dateOfCreation = LocalDateTime.now();
    }
    public ChatRoom(String chatId) {
        this.chatId = chatId;
        this.dateOfCreation = LocalDateTime.now();
    }


}
