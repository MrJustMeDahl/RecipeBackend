package Model;

import DTO.PersonDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mindrot.jbcrypt.BCrypt;

@Entity
@Setter
@Getter
@AllArgsConstructor
@Table(name = "persons")
@NamedQueries(
        @NamedQuery(name = "Person.getPersonByEmail", query = "SELECT p FROM Person p WHERE email = :email")
)
public class Person {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;
    private String name;
    private String password;
    private String email;
    private String role;

    public Person(PersonDTO personDTO) {
        this.id = personDTO.id;
        this.name = personDTO.name;
        this.password = personDTO.password;
        this.email = personDTO.email;
        this.role = personDTO.role;
    }

    public Person() {
    }

    public Person(String email, String password, String name, String role) {
        this.name = name;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.email = email;
        this.role = role;
    }

    public boolean verifyPassword(String password) {
        return BCrypt.checkpw(password, this.password);
    }
}
