package DTO;

import Model.Recipe;

import java.util.Set;

public class TagDTO {
    public int id;
    public String name;
    public Set<Recipe> recipes;

    public TagDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public TagDTO(String name, Set<Recipe> recipes) {
        this.name = name;
        this.recipes = recipes;
    }

    public TagDTO(int id, String name, Set<Recipe> recipes) {
        this.id = id;
        this.name = name;
        this.recipes = recipes;
    }

    public TagDTO(String name) {
        this.name = name;
    }

    public TagDTO() {
    }
}
