package os.ticketingsystem.login.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import os.ticketingsystem.login.dto.UserDTO;
import os.ticketingsystem.login.model.Role;
import os.ticketingsystem.login.model.User;
import os.ticketingsystem.login.repository.RoleRepository;
import os.ticketingsystem.login.repository.UserRepository;
import os.ticketingsystem.login.validator.LoginValidator;

import java.util.Optional;

@Service
public class UserAdministrationService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    @Autowired
    public UserAdministrationService(UserRepository userRepository,
                                     PasswordEncoder passwordEncoder,
                                     RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public void resetPassword(int id, UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(LoginValidator.USER_NOTFOUND, userDTO.getEmail()));
        }

        User dbUser = optionalUser.get();

        dbUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        userRepository.save(dbUser);
    }

    public boolean validatePassword(UserDTO userDTO) {
        return userDTO.getPassword().trim().equals(userDTO.getConfirmPassword().trim());
    }

    public void changeRole(int id, String newRole) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilizatorul selectat nu a fost gasit !");
        }

        User dbUser = optionalUser.get();

        switch (newRole.toLowerCase()) {
            case "user":
                Role roleUser = roleRepository.findByEnumRole(Role.EnumRole.ROLE_USER).
                        orElseThrow(() -> new RuntimeException("Error: Role user has not been found !"));
                dbUser.setRole(roleUser);
                break;
            case "admin":
                Role roleAdmin = roleRepository.findByEnumRole(Role.EnumRole.ROLE_ADMIN).
                        orElseThrow(() -> new RuntimeException("Error: Role admin has not been found !"));
                dbUser.setRole(roleAdmin);
                break;
        }

        userRepository.save(dbUser);
    }

    public void delete(int id) {
        userRepository.deleteById(id);
    }
}
