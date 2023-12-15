package Controllers;

import Config.HibernateConfig;
import Exceptions.APIException;
import Model.Person;
import Security.TokenCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManagerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AuthorizationController {

    private personDAO dao;

    private TokenCreator tokenCreator = TokenCreator.getInstance();

    public AuthorizationController(){
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig();
        personDAO = PersonDAO.getInstance(emf);
    }
    public Handler login(Context context) throws APIException {
        return () -> {
            String[] personInfo = getPersonInfo(context, true);
            Person person = personDAO.getPersonByEmailAndPassword(personInfo[0], personInfo[1]);
            String token = tokenCreator.createToken(person.getId(), person.getName(), person.getRole());

            context.status(200);
            String jsonResponse = createResponse(person.getId(), person.getName(), token);
            context.json(jsonResponse);
        };
    }

    public Handler register(Context context) throws APIException {
        return () -> {
            String[] personInfo = getPersonInfo(context, false);
            Person person = personDAO.registerPerson(personInfo[0], personInfo[1], personInfo[2]);
            String token = tokenCreator.createToken(person.getId(), person.getName(), person.getRole());

            context.status(201);
            String jsonResponse = createResponse(person.getId(), person.getName(), token);
            context.json(jsonResponse);
        };
    }

    private String[] getPersonInfo(Context context, boolean tryLogin) throws APIException{
        String request = context.body();

        ObjectMapper mapper = new ObjectMapper();
        try {
            Map jsonObject = mapper.readValue(request, Map.class);
            String email = jsonObject.get("email").toString();
            String password = jsonObject.get("password").toString();
            String role = "";

            List<String> roles = Arrays.asList("admin", "reader", "author");
            if(!tryLogin){
                role = jsonObject.get("role").toString();
                if(!roles.contains(role)){
                    throw new APIException(400, "Can't register user with the given role: " + role);
                }
            }

            return new String[]{email, password, role};
        } catch (JsonProcessingException e){
            throw new APIException(400, "JSON received from request body is malformed");
        }
    }

    private String createResponse(int id, String name, String token) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode response = mapper.createObjectNode();
        response.put("id", id);
        response.put("name", name);
        response.put("token", token);
        return response.toString();
    }
}
