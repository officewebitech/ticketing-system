package os.ticketingsystem.login.validator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import os.ticketingsystem.login.security.registrationToken.ConfirmationTokenService;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ConfirmationTokenValidator {

    private final ConfirmationTokenService tokenService;

    public boolean tokenExists(String token) {
        return tokenService.getToken(token).isPresent();
    }

    public boolean emailAlreadyConfirmed(String token) {
        return tokenService.getToken(token).get().getConfirmedAt() != null;
    }

    public boolean tokenExpired(String token) {
        LocalDateTime expiredAt = tokenService.getToken(token).get().getExpiresAt();
        return expiredAt.isBefore(LocalDateTime.now());
    }
}
