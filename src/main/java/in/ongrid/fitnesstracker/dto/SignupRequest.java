package in.ongrid.fitnesstracker.dto;

import in.ongrid.fitnesstracker.model.enums.Gender;
import in.ongrid.fitnesstracker.model.enums.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
public class SignupRequest {

    @NotBlank
    @Size(min = 3, max = 50)
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;

    private Gender gender;

    private UserType userType = UserType.USER; // ✅ Default to USER if not provided
}
