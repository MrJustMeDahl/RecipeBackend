package Routing;

import Controllers.AuthorizationController;
import Security.RoutingRoles;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class AuthorizationRoutes {

    private AuthorizationController authorizationController = new AuthorizationController();

    protected EndpointGroup getRoutes(){
        return () -> {
            path("/auth", () -> {
                post("/login", authorizationController::login, RoutingRoles.ANYONE);
                post("/register", authorizationController::register, RoutingRoles.ANYONE);
            });
        };
    }
}
