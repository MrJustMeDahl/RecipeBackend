package DTO;

import Model.Recipe;
import Model.Tag;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class RecipeDTO {
    private int id;
    private String name;
    private String description;
    private String ingredients;
    private String instructions;
    private Set<TagDTO> tags;
    private String author;

    public RecipeDTO(Recipe recipe) {
        this.id = recipe.getId();
        this.name = recipe.getName();
        this.description = recipe.getDescription();
        this.ingredients = recipe.getIngredients();
        this.instructions = recipe.getInstructions();
        this.author = recipe.getAuthor().getName();
        this.tags = new HashSet<>();
        for(Tag t: recipe.getTags()){
            tags.add(new TagDTO(t));
        }
    }

    public RecipeDTO() {
    }
}

