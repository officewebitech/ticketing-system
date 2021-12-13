package os.ticketingsystem.login.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import os.ticketingsystem.login.dto.PartialUserDTO;
import os.ticketingsystem.login.dto.UserDTO;
import os.ticketingsystem.login.model.Role;
import os.ticketingsystem.login.model.User;
import os.ticketingsystem.login.repository.RoleRepository;

@Service
public class UserMapper {

    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    @Autowired
    public UserMapper(PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public User map(UserDTO userDTO) {
        return User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .role(userDTO.getRole())
                .phoneNumber(userDTO.getPhoneNumber())
                .projectName(userDTO.getProjectName())
                .enabled(userDTO.getEnabled())
                .locked(userDTO.getLocked())
                .build();
    }

    public UserDTO map(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .phoneNumber(user.getPhoneNumber())
                .projectName(user.getProjectName())
                .enabled(user.getEnabled())
                .locked(user.getLocked())
                .build();
    }

    public PartialUserDTO mapToPartialUser(User user) {
        return PartialUserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole().getNameOfRole())
                .build();
    }

    public User registerUser(UserDTO userDTO) {

        Role role = roleRepository.findByEnumRole(Role.EnumRole.ROLE_CLIENT)
                .orElseThrow(() -> new RuntimeException("Error: Role user has not been found!"));
        return User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .locked(userDTO.getLocked())
                .enabled(userDTO.getLocked())
                .role(role)
                .phoneNumber(userDTO.getPhoneNumber())
                .projectName(userDTO.getProjectName())
                .build();
    }


}
