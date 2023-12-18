package Controllers;

import DTO.PersonDTO;
import Exceptions.APIException;
import Security.RoutingRoles;
import Security.TokenActions;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.security.RouteRole;

import java.util.Set;

public class AccessManagerController {

    private TokenActions tokenActions = TokenActions.getInstance();

    public void accessManagerHandler(Handler handler, Context context, Set<? extends RouteRole> routeRoles) throws Exception {

        String path = context.path();
        boolean accessAllowed = false;
            if (path.equals("/api/auth/login") || path.equals("/api/auth/register") || routeRoles.contains(RoutingRoles.ANYONE)) {
                handler.handle(context);
            } else {
                RoutingRoles userRole = getUserRole(context);
                    if(routeRoles.contains(userRole)){
                        accessAllowed = true;
                    }
                if(accessAllowed){
                    handler.handle(context);
                } else {
                    throw new APIException(403, "You are not permitted to perform your this request");
                }
            }
    }

    private RoutingRoles getUserRole(Context context) throws APIException {
        String token = context.header("Authorization").split(" ")[1];
        PersonDTO person = tokenActions.verifyToken(token);
        return RoutingRoles.valueOf(person.getRole());
    }
}
