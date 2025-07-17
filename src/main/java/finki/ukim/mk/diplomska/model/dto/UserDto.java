package finki.ukim.mk.diplomska.model.dto;

import finki.ukim.mk.diplomska.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    public UUID uuid;
    public String username;
    public String firstName;
    public String lastName;
    public String organizationName;
    public String email;
    public String password;
    public LocalDate dayOfBirth;
    public Role role;
}
