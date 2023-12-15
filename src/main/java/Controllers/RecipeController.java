package Controllers;

import DTO.RecipeDTO;
import Exceptions.APIException;
import Model.Person;
import Security.DAO.RecipeDAO;
import io.javalin.http.Handler;
import Model.Recipe;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeController implements IController{
    @Override
    public Handler getAll() {

        return ctx -> {
            RecipeDAO RecipeDAO = Security.DAO.RecipeDAO.getInstance();
            List<Recipe> recipes = RecipeDAO.getAll(Recipe.class);
            List<RecipeDTO> RecipeDTOS = recipes.stream().map(RecipeDTO::new).toList();
            if (RecipeDTOS.isEmpty()) {
                throw new APIException(404, "No recipes in database");
            }
            ctx.json(RecipeDTOS);
        };
    }

    @Override
    public Handler getById() {
        return ctx->{
            String idString = ctx.pathParam("id");
            int id = Integer.parseInt(idString);
            RecipeDAO RecipeDAO = Security.DAO.RecipeDAO.getInstance();
            Recipe Recipe= RecipeDAO.getById(id, Recipe.class);
            RecipeDTO RecipeDTO = new RecipeDTO(Recipe);
            if(RecipeDTO == null){
                throw new APIException(404, "No Recipewith id: " + id + " in database");
            }
            ctx.json(RecipeDTO);
        };
    }

    @Override
    public Handler getByName() {
        return ctx->{
            String nameString = ctx.pathParam("name");
            RecipeDAO RecipeDAO = Security.DAO.RecipeDAO.getInstance();
            Recipe Recipe= RecipeDAO.getByName(nameString, Recipe.class);
            RecipeDTO RecipeDTO = new RecipeDTO(Recipe);
            if(RecipeDTO == null){
                throw new APIException(404, "No Recipewith name: " + nameString + " in database");
            }
            ctx.json(RecipeDTO);
        };
    }

    @Override
    public Handler create() {
        return ctx->{
            RecipeDTO RecipeDTO = ctx.bodyAsClass(RecipeDTO.class);
            RecipeDAO RecipeDAO = Security.DAO.RecipeDAO.getInstance();
            Recipe createdRecipe = RecipeDAO.create(RecipeDTO);
            RecipeDTO createdRecipeDTO = new RecipeDTO(createdRecipe);
            ctx.status(201);
            ctx.json(createdRecipeDTO);
        };
    }

    @Override
    public Handler update() {
        return ctx->{
            RecipeDTO RecipeDTO = ctx.bodyAsClass(RecipeDTO.class);
            RecipeDAO RecipeDAO = Security.DAO.RecipeDAO.getInstance();
            RecipeDAO.update(RecipeDTO);
            ctx.status(201);
            ctx.json(RecipeDTO);
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
                throw new APIException(404, "No Recipewith id: " + id + " in database");
            }
            ctx.status(204);
        };
    }
}
