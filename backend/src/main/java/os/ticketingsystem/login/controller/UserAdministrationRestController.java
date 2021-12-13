package os.ticketingsystem.login.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import os.ticketingsystem.login.dto.PartialUserDTO;
import os.ticketingsystem.login.dto.UserDTO;
import os.ticketingsystem.login.payload.response.MessageResponse;
import os.ticketingsystem.login.services.LoginService;
import os.ticketingsystem.login.services.UserAdministrationService;
import os.ticketingsystem.login.validator.LoginValidator;
import os.ticketingsystem.login.validator.RegisterValidator;
import os.ticketingsystem.login.validator.UserAdministrationValidator;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/user-administration", produces = "application/json")
public class UserAdministrationRestController {

    private LoginService loginService;
    private UserAdministrationService userAdministrationService;
    private UserAdministrationValidator userAdministrationValidator;
    private RegisterValidator registerValidator;

    @Autowired
    public UserAdministrationRestController(LoginService loginService,
                                            RegisterValidator registerValidator,
                                            UserAdministrationService userAdministrationService,
                                            UserAdministrationValidator userAdministrationValidator){
        this.loginService = loginService;
        this.registerValidator = registerValidator;
        this.userAdministrationService = userAdministrationService;
        this.userAdministrationValidator = userAdministrationValidator;
    }

    @GetMapping
    public ResponseEntity<List<PartialUserDTO>> getAll(){
        return ResponseEntity.ok(loginService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> resetPassword(@PathVariable int id,
                                           @RequestBody UserDTO userDTO){

        if(!userAdministrationValidator.validateById(id)){
            return ResponseEntity.badRequest().body(new MessageResponse(String.format(LoginValidator.USER_NOTFOUND, userDTO.getEmail())));
        }

        if (!registerValidator.passwordComplexity(userDTO)) {

            final String message = "Parola trebuie sa contina: \n" +
                    "cel putin o cifra \n" +
                    "cel putin o litera mica \n" +
                    "cel putin o litera capitalizata \n" +
                    "cel putin un caracter special @ # $ % ! \n" +
                    "parola trebuie sa contina cel putin 8 caractere";

            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(message));
        }

        if (!userAdministrationService.validatePassword(userDTO)){
            return ResponseEntity.badRequest().body(new MessageResponse("Parola si confirma parola nu sunt la fel !"));
        }

        userAdministrationService.resetPassword(id, userDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/change-role/{id}")
    public ResponseEntity<?> changeRole(@PathVariable int id,
                                        @RequestParam String role){
        /* TODO:
        Validation to be done if the user is an admin block the change of the role
         */

        userAdministrationService.changeRole(id, role);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PartialUserDTO> delete(@PathVariable int id){
        userAdministrationService.delete(id);
        return ResponseEntity.ok().build();
    }

}

