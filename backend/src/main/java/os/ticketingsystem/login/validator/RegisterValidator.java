package os.ticketingsystem.login.validator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import os.ticketingsystem.login.dto.UserDTO;
import os.ticketingsystem.login.repository.UserRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RegisterValidator {

    private UserRepository userRepository;

    @Autowired
    public RegisterValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean validate(UserDTO userDTO) {
        return userRepository.existsByEmail(userDTO.getEmail().trim());
    }

    public boolean confirmPasswordFor(UserDTO userDTO) {
        return userDTO.getPassword().trim().equals(userDTO.getConfirmPassword().trim());
    }

    public boolean emailValidation(UserDTO userDTO) {
        String regex = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(userDTO.getEmail());

        return matcher.matches();
    }

    public boolean passwordComplexity(UserDTO userDTO) {
        String regex = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,64})";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(userDTO.getPassword());

        return matcher.matches();
    }

}
