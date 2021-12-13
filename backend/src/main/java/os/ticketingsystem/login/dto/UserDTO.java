package os.ticketingsystem.login.dto;

import lombok.*;
import os.ticketingsystem.login.model.Role;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserDTO {

    private int id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String confirmPassword;

    private Boolean locked = false;

    private Boolean enabled = false;

    @NotBlank
    private Role role;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String projectName;
}
