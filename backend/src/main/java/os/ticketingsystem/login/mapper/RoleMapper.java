package os.ticketingsystem.login.mapper;

import org.springframework.stereotype.Service;
import os.ticketingsystem.login.dto.RoleDTO;
import os.ticketingsystem.login.model.Role;

@Service
public class RoleMapper {

    public RoleDTO map(Role role) {
        return RoleDTO.builder()
                .id(role.getId())
                .enumRole(role.getEnumRole())
                .nameOfRole(role.getNameOfRole())
                .build();
    }

    public Role map(RoleDTO roleDTO) {
        return Role.builder()
                .enumRole(roleDTO.getEnumRole())
                .nameOfRole(roleDTO.getNameOfRole())
                .build();
    }
}
