package os.ticketingsystem.login.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartialUserDTO {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;

    private String phoneNumber;
    private String projectName;
}
