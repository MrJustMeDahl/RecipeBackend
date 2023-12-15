package Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;
    private String name;
    @ManyToMany(mappedBy = "tags")
    private Set<Recipe> recipes;

    public Tag() {
    }
}
