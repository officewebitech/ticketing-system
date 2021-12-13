package os.ticketingsystem.login.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import os.ticketingsystem.login.repository.UserRepository;

@Service
public class UserAdministrationValidator {

    private UserRepository userRepository;

    @Autowired
    public UserAdministrationValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean validateById(int id) {
        return userRepository.existsById(id);
    }
}
