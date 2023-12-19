package Controllers;

import DTO.RecipeDTO;
import Exceptions.APIException;
import Model.Person;
import Model.Tag;
import Security.DAO.PersonDAO;
import Security.DAO.RecipeDAO;
import io.javalin.http.Handler;
import Model.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RecipeController implements IController{
    @Override
    public Handler getAll() {

        return ctx -> {
            RecipeDAO RecipeDAO = Security.DAO.RecipeDAO.getInstance();
            List<Recipe> recipes = RecipeDAO.getAllRecipes();
            List<RecipeDTO> recipeDTOS = new ArrayList<>();
            for(Recipe r: recipes){
                recipeDTOS.add(new RecipeDTO(r));
            }
            if (recipeDTOS.isEmpty()) {
                throw new APIException(404, "No recipes in database");
            }
            ctx.json(200);
            ctx.json(recipeDTOS);
        };
    }

    @Override
    public Handler getById() {
        return ctx->{
            String idString = ctx.pathParam("id");
            int id = Integer.parseInt(idString);
            RecipeDAO RecipeDAO = Security.DAO.RecipeDAO.getInstance();
            Recipe Recipe= RecipeDAO.getRecipeByID(id);
            RecipeDTO RecipeDTO = new RecipeDTO(Recipe);
            if(RecipeDTO == null){
                throw new APIException(404, "No Recipe with id: " + id + " in database");
            }
            ctx.json(200);
            ctx.json(RecipeDTO);
        };
    }

    @Override
    public Handler getByName() {
        return ctx->{
            String nameString = ctx.pathParam("name");
            RecipeDAO recipeDAO = Security.DAO.RecipeDAO.getInstance();
            List<Recipe> recipes = recipeDAO.getRecipeByName(nameString);
            List<RecipeDTO> dtos = new ArrayList<>();
            for(Recipe r: recipes){
                dtos.add(new RecipeDTO(r));
            }
            if(recipes.isEmpty()){
                throw new APIException(404, "No Recipe with name: " + nameString + " in database");
            }
            ctx.json(200);
            ctx.json(dtos);
        };
    }

    @Override
    public Handler create() {
        return ctx->{
            RecipeDTO recipeDTO = ctx.bodyAsClass(RecipeDTO.class);
            RecipeDAO recipeDAO = Security.DAO.RecipeDAO.getInstance();
            Person author = PersonDAO.getInstance().getPersonByID(Integer.parseInt(recipeDTO.getAuthor()));
            Set<Tag> tags = recipeDAO.getTagsByName(recipeDTO.getTags());
            Recipe createdRecipe = recipeDAO.create(recipeDTO, author, tags);
            RecipeDTO createdRecipeDTO = new RecipeDTO(createdRecipe);
            ctx.status(201);
            ctx.json(createdRecipeDTO);
        };
    }

    @Override
    public Handler update() {
        return ctx->{
            RecipeDTO recipeDTO = ctx.bodyAsClass(RecipeDTO.class);
            recipeDTO.setId(Integer.parseInt(ctx.pathParam("id")));
            RecipeDAO recipeDAO = Security.DAO.RecipeDAO.getInstance();
            Person author = PersonDAO.getInstance().getPersonByID(Integer.parseInt(recipeDTO.getAuthor()));
            Person oldAuthor = PersonDAO.getInstance().getAuthorForRecipe(recipeDTO.getId());
            Set<Tag> tags = recipeDAO.getTagsByName(recipeDTO.getTags());
            RecipeDTO updatedRecipe = new RecipeDTO(recipeDAO.update(recipeDTO, oldAuthor, author, tags));
            ctx.status(201);
            ctx.json(updatedRecipe);
        };
    }

    @Override
    public Handler delete() {
        return ctx->{
            String idString = ctx.pathParam("id");
            int id = Integer.parseInt(idString);
            RecipeDAO RecipeDAO = Security.DAO.RecipeDAO.getInstance();
            boolean deleted = RecipeDAO.delete(id, Recipe.class);
            if(!deleted){
                throw new APIException(404, "No Recipe with id: " + id + " in database");
            }
            ctx.status(204);
        };
    }
}
