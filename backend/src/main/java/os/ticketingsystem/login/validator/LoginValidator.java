package os.ticketingsystem.login.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import os.ticketingsystem.login.model.User;
import os.ticketingsystem.login.repository.UserRepository;

import java.util.Optional;

@Service
public class LoginValidator {

    public static final String USER_NOTFOUND = "Utilizatorul %s nu a fost gasit !";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginValidator(UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    public boolean validate(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        return optionalUser.filter(user -> passwordEncoder.matches(password, user.getPassword())).isPresent();
    }

    public boolean existingUser(String email){
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean enabledUser(String email) {
       return userRepository.findByEmail(email).get().getEnabled();
    }
}
