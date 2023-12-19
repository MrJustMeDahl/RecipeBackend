package Model;

import DTO.RecipeDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NamedQueries({
        @NamedQuery(name = "Recipe.getAllRecipes", query = "SELECT r FROM Recipe r"),
        @NamedQuery(name = "Recipe.getRecipeByName", query = "SELECT r FROM Recipe r WHERE r.name LIKE :name")
})
@Table(name = "recipes")
public class Recipe implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private String ingredients;
    private String instructions;
    @ManyToMany
    private Set<Tag> tags;
    @ManyToOne
    private Person author;

    public Recipe() {
    }

    public Recipe(RecipeDTO dto){
        this.id = dto.getId();
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.ingredients = dto.getIngredients();
        this.instructions = dto.getInstructions();
        this.tags = new HashSet<>();
        this.author = null;
    }

    public void setAuthor(Person author){
        this.author = author;
        author.addRecipe(this);
    }

    public void addTags(Set<Tag> tags) {
        for(Tag t: tags){
            this.tags.add(t);
            t.addRecipe(this);
        }
    }

    public void removeAuthor(Person oldAuthor) {
        this.author = null;
        oldAuthor.removeRecipe(this);
    }

    public void removeAllTags() {
        for(Tag t: tags){
            tags.remove(t);
            t.removeRecipe(this);
        }
    }
}
