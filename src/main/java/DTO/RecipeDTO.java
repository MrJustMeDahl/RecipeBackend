package DTO;

import Model.Recipe;

import java.util.List;
import java.util.Set;

public class RecipeDTO {
    public int id;
    public String name;
    public String description;
    public String ingredients;
    public String instructions;
    public Set<TagDTO> tags;
    public PersonDTO author;

    public RecipeDTO(int id, String name, String description, String ingredients, String instructions, Set<TagDTO> tags, PersonDTO author) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.tags = tags;
        this.author = author;
    }

    public RecipeDTO(String name, String description, String ingredients, String instructions, Set<TagDTO> tags, PersonDTO author) {
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.tags = tags;
        this.author = author;
    }

    public RecipeDTO(String name, String description, String ingredients, String instructions) {
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }
    public RecipeDTO(Recipe recipe) {
        this.id = recipe.getId();
        this.name = recipe.getName();
        this.description = recipe.getDescription();
        this.ingredients = recipe.getIngredients();
        this.instructions = recipe.getInstructions();
    }

    public RecipeDTO() {
    }


}
