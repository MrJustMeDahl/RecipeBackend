package Controllers;

import DTO.PersonDTO;
import Exceptions.APIException;
import Model.Person;
import Security.DAO.PersonDAO;
import io.javalin.http.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PersonController implements IController{
    @Override
    public Handler getAll() {

        return ctx -> {
            PersonDAO personDAO = PersonDAO.getInstance();
            List<Person> persons = personDAO.getAll();
            List<PersonDTO> personDTOS = new ArrayList<>();
            for(Person p: persons){
                personDTOS.add(new PersonDTO(p));
            }
            if (personDTOS.isEmpty()) {
                throw new APIException(404, "No persons in database");
            }
            ctx.json(200);
            ctx.json(personDTOS);
        };
    }

    @Override
    public Handler getById() {
        return ctx->{
            String idString = ctx.pathParam("id");
            int id = Integer.parseInt(idString);
            PersonDAO personDAO = PersonDAO.getInstance();
            Person person = personDAO.getPersonByID(id);
            PersonDTO personDTO = new PersonDTO(person);
            if(personDTO == null){
                throw new APIException(404, "No person with id: " + id + " in database");
            }
            ctx.json(200);
            ctx.json(personDTO);
        };
    }

    @Override
    public Handler getByName() {
        return ctx->{
            String nameString = ctx.pathParam("name");
            PersonDAO personDAO = PersonDAO.getInstance();
            List<Person> persons = personDAO.getPersonsByName(nameString);
            List<PersonDTO> dtos = new ArrayList<>();
            for(Person p: persons) {
                dtos.add(new PersonDTO(p));
            }
            if(dtos.isEmpty()){
                throw new APIException(404, "No person with name: " + nameString + " in database");
            }
            ctx.status(200);
            ctx.json(dtos);
        };
    }

    @Override
    public Handler create() {
        return ctx->{
            PersonDTO personDTO = ctx.bodyAsClass(PersonDTO.class);
            PersonDAO personDAO = PersonDAO.getInstance();
            Person createdPerson = personDAO.create(personDTO);
            PersonDTO createdPersonDTO = new PersonDTO(createdPerson);
            ctx.status(201);
            ctx.json(createdPersonDTO);
        };
    }

    @Override
    public Handler update() {
        return ctx->{
            PersonDTO personDTO = ctx.bodyAsClass(PersonDTO.class);
            PersonDAO personDAO = PersonDAO.getInstance();
            personDAO.update(personDTO);
            ctx.status(201);
            ctx.json(personDTO);
        };
    }

    @Override
    public Handler delete() {
        return ctx->{
            String idString = ctx.pathParam("id");
            int id = Integer.parseInt(idString);
            PersonDAO personDAO = PersonDAO.getInstance();
            personDAO.delete(id);
            ctx.status(204);
        };
    }
}
