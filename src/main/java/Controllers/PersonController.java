package Controllers;

import DTO.PersonDTO;
import Exceptions.APIException;
import Model.Person;
import Security.DAO.PersonDAO;
import io.javalin.http.Handler;

import java.util.List;
import java.util.stream.Collectors;

public class PersonController implements IController{
    @Override
    public Handler getAll() {

        return ctx -> {
            PersonDAO personDAO = PersonDAO.getInstance();
            List<Person> persons = personDAO.getAll(Person.class);
            List<PersonDTO> personDTOS = persons.stream().map(PersonDTO::new).toList();
            if (personDTOS.isEmpty()) {
                throw new APIException(404, "No persons in database");
            }
            ctx.json(personDTOS);
        };
    }

    @Override
    public Handler getById() {
        return ctx->{
            String idString = ctx.pathParam("id");
            int id = Integer.parseInt(idString);
            PersonDAO personDAO = PersonDAO.getInstance();
            Person person = personDAO.getById(id, Person.class);
            PersonDTO personDTO = new PersonDTO(person);
            if(personDTO == null){
                throw new APIException(404, "No person with id: " + id + " in database");
            }
            ctx.json(personDTO);
        };
    }

    @Override
    public Handler getByName() {
        return ctx->{
            String nameString = ctx.pathParam("name");
            PersonDAO personDAO = PersonDAO.getInstance();
            Person person = personDAO.getByName(nameString, Person.class);
            PersonDTO personDTO = new PersonDTO(person);
            if(personDTO == null){
                throw new APIException(404, "No person with name: " + nameString + " in database");
            }
            ctx.json(personDTO);
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
            boolean deleted = personDAO.delete(id, Person.class);
            if(!deleted){
                throw new APIException(404, "No person with id: " + id + " in database");
            }
            ctx.status(204);
        };
    }
}
