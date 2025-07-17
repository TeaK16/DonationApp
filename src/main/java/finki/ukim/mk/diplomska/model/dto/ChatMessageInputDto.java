package finki.ukim.mk.diplomska.model.dto;


import lombok.Data;

import java.util.UUID;

@Data
public class ChatMessageInputDto {
    public String recipientId;
    public String content;

}
