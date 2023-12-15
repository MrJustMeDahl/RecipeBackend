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
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private String ingredients;
    private String instructions;
    @ManyToMany
    private Set<Tag> tags;

    public Recipe() {
    }
}
