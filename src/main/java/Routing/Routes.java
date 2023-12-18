package Routing;

import Controllers.PersonController;
import Controllers.RecipeController;
import DTO.PersonDTO;
import Exceptions.APIException;
import Exceptions.ExceptionHandler;
import Security.DAO.PersonDAO;
import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    private ExceptionHandler exceptionHandler = new ExceptionHandler();
    private PersonController personController = new PersonController();
    private RecipeController recipeController = new RecipeController();
    private AuthorizationRoutes authRoutes = new AuthorizationRoutes();

    public static final String PERSONS = "/persons";
    public static final String RECIPES = "/recipes";

    public EndpointGroup getRoutes(Javalin app) {
        return () -> {
            app.routes(() -> {
                path("/", () -> {
                    get(PERSONS, personController.getAll());
                    get(PERSONS + "/{id}", personController.getById());
                    get(PERSONS + "/name/{name}", personController.getByName());
                    // get(PERSONS + "/email/{email}", personController.getPersonByEmail()); todo
                    put(PERSONS + "/{id}", personController.update());
                    post(PERSONS, personController.create());
                    delete(PERSONS + "/{id}", personController.delete());
                    get(RECIPES, recipeController.getAll());
                    get(RECIPES + "/{id}", recipeController.getById());
                    get(RECIPES + "/name/{name}", recipeController.getByName());
                    put(RECIPES + "/{id}", recipeController.update());
                    post(RECIPES, recipeController.create());
                    delete(RECIPES + "/{id}", recipeController.delete());
                });
                path("/", authRoutes.getRoutes());
            });
            app.exception(APIException.class, exceptionHandler::apiExceptionHandler);
            app.exception(NumberFormatException.class, exceptionHandler::numberFormatExceptionHandler);
            app.exception(Exception.class, exceptionHandler::exceptionHandler);
        };
    }
}
