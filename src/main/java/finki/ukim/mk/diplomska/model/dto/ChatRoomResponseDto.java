package finki.ukim.mk.diplomska.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ChatRoomResponseDto {
    UUID recipientId;
    String recipientUsername;
    String chatRoomId;
}
