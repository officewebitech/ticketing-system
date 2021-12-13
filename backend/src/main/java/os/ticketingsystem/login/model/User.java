package os.ticketingsystem.login.model;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private String phoneNumber;
    private String projectName;

    private Boolean locked;
    private Boolean enabled;

    @ManyToOne
    private Role role;
}
