package os.ticketingsystem.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import os.ticketingsystem.login.dto.UserDTO;
import os.ticketingsystem.login.payload.response.MessageResponse;
import os.ticketingsystem.login.services.RegisterService;
import os.ticketingsystem.login.validator.ConfirmationTokenValidator;
import os.ticketingsystem.login.validator.RegisterValidator;

import javax.mail.Message;
import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/register", produces = "application/json")
public class RegisterRestController {

    private final RegisterValidator registerValidator;
    private final RegisterService registerService;
    private final ConfirmationTokenValidator tokenValidator;

    @Autowired
    public RegisterRestController(RegisterValidator registerValidator, RegisterService registerService, ConfirmationTokenValidator tokenValidator) {
        this.registerValidator = registerValidator;
        this.registerService = registerService;
        this.tokenValidator = tokenValidator;
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO) {
        if (registerValidator.validate(userDTO)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Email-ul: " + userDTO.getEmail() + " exista deja in baza de date"));

        }

        if (!registerValidator.confirmPasswordFor(userDTO)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Eroare: Parolele nu se potrivesc !"));
        }

        if (!registerValidator.passwordComplexity(userDTO)) {

            final String message = "Parola trebuie sa contina: \n" +
                    "cel putin o cifra \n" +
                    "cel putin 0 litera mica \n" +
                    "cel putin o litera capitalizata \n" +
                    "cel putin un caracter special @ # $ % ! \n" +
                    "parola trebuie sa contina cel putin 8 caractere";

            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(message));
        }

        if (!registerValidator.emailValidation(userDTO)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Eroare: Formatul emailul este invalid"));
        }

        registerService.registerUser(userDTO);
        return ResponseEntity.ok(new MessageResponse(userDTO.getEmail() + " inregistrat cu succes !"));
    }

    @GetMapping(path = "confirm")
    public ResponseEntity<MessageResponse> confirm(@RequestParam("token") String token) {
        if (!tokenValidator.tokenExists(token)){
            return ResponseEntity.badRequest().body(new MessageResponse("Token-ul nu a fost gasit !"));
        }

        if (tokenValidator.emailAlreadyConfirmed(token)){
            return ResponseEntity.badRequest().body(new MessageResponse("Contul a fost deja activat !"));
        }

        if (tokenValidator.tokenExpired(token)){
            return ResponseEntity.badRequest().body(new MessageResponse("Token-ul a expirat"));
        }

        return ResponseEntity.ok(new MessageResponse(registerService.confirmToken(token)));
    }

}
