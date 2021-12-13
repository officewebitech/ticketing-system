package os.ticketingsystem.login.dto;

import lombok.*;
import os.ticketingsystem.login.model.Role;
import os.ticketingsystem.login.model.User;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RoleDTO {

    private int id;

    private String nameOfRole;
    private Role.EnumRole enumRole;
    private List<User> userList;
}
