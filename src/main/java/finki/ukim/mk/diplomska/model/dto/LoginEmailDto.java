package finki.ukim.mk.diplomska.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginEmailDto {
    public String email;
    public String password;

}
