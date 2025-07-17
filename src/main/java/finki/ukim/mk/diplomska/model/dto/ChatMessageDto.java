package finki.ukim.mk.diplomska.model.dto;

import finki.ukim.mk.diplomska.model.ApplicationUser;
import finki.ukim.mk.diplomska.model.ChatRoom;
import lombok.Data;

import java.util.UUID;

@Data
public class ChatMessageDto {
    public ApplicationUser sender;
    public ApplicationUser recipient;
    public ChatRoom chatRoom;
    public String content;
}
