package Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NamedQueries({
        @NamedQuery(name = "Tag.getTagsForRecipe", query = "SELECT t from Tag t INNER JOIN t.recipes r WHERE r.id = :id"),
        @NamedQuery(name = "Tag.getTagByName", query = "SELECT t FROM Tag t WHERE t.name LIKE :name")
})
@Table(name = "tags")
public class Tag implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;
    private String name;
    @ManyToMany(mappedBy = "tags")
    private Set<Recipe> recipes;

    public Tag() {
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
