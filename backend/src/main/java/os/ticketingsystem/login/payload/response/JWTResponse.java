package os.ticketingsystem.login.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JWTResponse {

    private int id;

    private String token;

    private String type = "Bearer ";

    private String firstName;

    private String lastName;

    private String email;

    private List<String> roles;

    public JWTResponse(int id, String token, String firstName, String lastName,String email, List<String> roles) {
        this.id = id;
        this.token = token;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = roles;
    }
}
