package os.ticketingsystem.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import os.ticketingsystem.login.dto.UserDTO;
import os.ticketingsystem.login.payload.response.MessageResponse;
import os.ticketingsystem.login.services.LoginService;
import os.ticketingsystem.login.validator.LoginValidator;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/login", produces = "application/json")
public class LoginRestController {

    private static final String ERROR_MSG = "Email %s or password incorrect !";
    private static final String USER_NON_EXISTING = "Email %s does not exist !";
    private static final String USER_DISABLED = "Email %s has not been activated";
    private final LoginService loginService;
    private final LoginValidator loginValidator;

    @Autowired
    public LoginRestController(LoginService loginService,
                               LoginValidator loginValidator) {
        this.loginService = loginService;
        this.loginValidator = loginValidator;
    }

    @PostMapping
    public ResponseEntity<?> login(@Valid @RequestBody UserDTO userDTO) {

        if (!loginValidator.existingUser(userDTO.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse(String.format(USER_NON_EXISTING, userDTO.getEmail())));
        }

        if (!loginValidator.enabledUser(userDTO.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse(String.format(USER_DISABLED, userDTO.getEmail())));
        }

        if (!loginValidator.validate(userDTO.getEmail(), userDTO.getPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse(String.format(ERROR_MSG, userDTO.getEmail())));
        }

        return ResponseEntity.ok(loginService.login(userDTO));
    }

}
