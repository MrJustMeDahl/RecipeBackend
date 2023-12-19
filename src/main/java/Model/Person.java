package Model;

import DTO.PersonDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mindrot.jbcrypt.BCrypt;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@Table(name = "persons")
@NamedQueries({
        @NamedQuery(name = "Person.getPersonByEmail", query = "SELECT p FROM Person p WHERE email = :email"),
        @NamedQuery(name = "Person.getAll", query = "SELECT p FROM Person p"),
        @NamedQuery(name = "Person.findByName", query = "SELECT p FROM Person p WHERE p.name LIKE :name"),
        @NamedQuery(name = "Person.getAuthorForRecipe", query = "SELECT p FROM Person p INNER JOIN p.recipes r WHERE r.id = :id")
})
public class Person implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;
    private String name;
    private String password;
    private String email;
    private String role;
    @OneToMany(mappedBy = "author")
    private Set<Recipe> recipes = new HashSet<>();

    public Person(PersonDTO personDTO) {
        this.id = personDTO.id;
        this.name = personDTO.name;
        this.password = BCrypt.hashpw(personDTO.password, BCrypt.gensalt());
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

    public void setPassword(String password){
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
    }

    public void removeRecipe(Recipe recipe) {
        for(Recipe r: recipes){
            if(r.getId() == recipe.getId()){
                recipes.remove(r);
            }
        }
    }
}
