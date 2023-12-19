package Routing;

import Controllers.PersonController;
import Controllers.RecipeController;
import DTO.PersonDTO;
import Exceptions.APIException;
import Exceptions.ExceptionHandler;
import Security.DAO.PersonDAO;
import Security.RoutingRoles;
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
                    get(PERSONS, personController.getAll(), RoutingRoles.ADMIN);
                    get(PERSONS + "/{id}", personController.getById(), RoutingRoles.ADMIN);
                    get(PERSONS + "/name/{name}", personController.getByName(), RoutingRoles.ADMIN);
                    // get(PERSONS + "/email/{email}", personController.getPersonByEmail()); todo
                    put(PERSONS + "/{id}", personController.update(), RoutingRoles.ADMIN);
                    post(PERSONS, personController.create(), RoutingRoles.ADMIN);
                    delete(PERSONS + "/{id}", personController.delete(), RoutingRoles.ADMIN);
                    get(RECIPES, recipeController.getAll(), RoutingRoles.ADMIN, RoutingRoles.AUTHOR, RoutingRoles.READER, RoutingRoles.ANYONE);
                    get(RECIPES + "/{id}", recipeController.getById(), RoutingRoles.ADMIN, RoutingRoles.AUTHOR, RoutingRoles.READER, RoutingRoles.ANYONE);
                    get(RECIPES + "/name/{name}", recipeController.getByName(), RoutingRoles.ADMIN, RoutingRoles.AUTHOR, RoutingRoles.READER, RoutingRoles.ANYONE);
                    put(RECIPES + "/{id}", recipeController.update(), RoutingRoles.ADMIN, RoutingRoles.AUTHOR);
                    post(RECIPES, recipeController.create(), RoutingRoles.ADMIN, RoutingRoles.AUTHOR);
                    delete(RECIPES + "/{id}", recipeController.delete(), RoutingRoles.ADMIN, RoutingRoles.AUTHOR);
                });
                path("/", authRoutes.getRoutes());
            });
            app.exception(APIException.class, exceptionHandler::apiExceptionHandler);
            app.exception(NumberFormatException.class, exceptionHandler::numberFormatExceptionHandler);
            app.exception(Exception.class, exceptionHandler::exceptionHandler);
        };
    }
}
