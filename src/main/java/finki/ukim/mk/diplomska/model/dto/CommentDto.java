package finki.ukim.mk.diplomska.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    public UUID id;
    public String description;
    public Integer likes;
    public UUID postUUID;
    public UUID userUUID;
}
