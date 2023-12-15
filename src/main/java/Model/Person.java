package Model;

import DTO.PersonDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@Table(name = "persons")
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
}
