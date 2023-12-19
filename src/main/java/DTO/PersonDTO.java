package DTO;

import Model.Recipe;
import lombok.Getter;
import Model.Person;

import java.util.HashSet;
import java.util.Set;

@Getter
public class PersonDTO {
    public int id;
    public String name;
    public String password;
    public String email;
    public String role;
    private Set<RecipeDTO> recipes = new HashSet<>();


    public PersonDTO(int id, String name, String password, String email, String role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public PersonDTO(String name, String password, String email, String role) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public PersonDTO(Person person) {
        this.id = person.getId();
        this.name = person.getName();
        this.password = person.getPassword();
        this.email = person.getEmail();
        this.role = person.getRole();
        for(Recipe r: person.getRecipes()){
            recipes.add(new RecipeDTO(r));
        }
    }

    public PersonDTO() {
    }

    public PersonDTO(int id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }
}