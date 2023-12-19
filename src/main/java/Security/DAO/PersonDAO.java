package Security.DAO;

import DTO.PersonDTO;
import Exceptions.APIException;
import Model.Person;
import Model.Recipe;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Set;

public class PersonDAO extends DAO<PersonDTO, Person> {

    //Singleton
    private static PersonDAO instance;
    private PersonDAO(){}
    public static PersonDAO getInstance(){
        if(instance == null){
            instance = new PersonDAO();
        }
        return instance;
    }


        public List<Person> getAll(){
            RecipeDAO recipeDAO = RecipeDAO.getInstance();
            try(EntityManager em = emf.createEntityManager()){
                List<Person> allPersons = em.createNamedQuery("Person.getAll").getResultList();
                for(Person p: allPersons){
                    Set<Recipe> recipes = p.getRecipes();
                    for(Recipe r: recipes){
                        r.setTags(recipeDAO.getTagsForRecipe(r.getId()));
                        r.setAuthor(p);
                    }
                }
                return allPersons;
            }
        }

        public Person getPersonByID(int id){
            RecipeDAO recipeDAO = RecipeDAO.getInstance();
            try(EntityManager em = emf.createEntityManager()){
                Person foundPerson = em.find(Person.class, id);
                for(Recipe r: foundPerson.getRecipes()){
                    r.setTags(recipeDAO.getTagsForRecipe(id));
                    r.setAuthor(foundPerson);
                }
                return foundPerson;
            }
        }

        public List<Person> getPersonsByName(String name){
            RecipeDAO recipeDAO = RecipeDAO.getInstance();
            try(EntityManager em = emf.createEntityManager()){
                Query query = em.createNamedQuery("Person.findByName");
                List<Person> foundPersons = query.setParameter("name", name).getResultList();
                for(Person p: foundPersons){
                    Set<Recipe> recipes = p.getRecipes();
                    for(Recipe r: recipes){
                        r.setTags(recipeDAO.getTagsForRecipe(r.getId()));
                        r.setAuthor(p);
                    }
                }
                return foundPersons;
            }
        }

        @Override
        public Person create(PersonDTO personDTO) {
            try (EntityManager em = emf.createEntityManager()){
                em.getTransaction().begin();
                Person person = new Person(personDTO);

                em.persist(person);
                em.getTransaction().commit();
                //Find the autogenerated id for the person and return it to set it in the personDTO
                List<Person> persons = em.createQuery("SELECT p FROM Person p", Person.class).getResultList();
                Person createdPerson = persons.get(persons.size()-1);
                return createdPerson;
            }    }

        @Override
        public Person update(PersonDTO personDTO) {
            try (EntityManager em = emf.createEntityManager()){
                em.getTransaction().begin();
                Person person = em.find(Person.class, personDTO.id);
                person.setName(personDTO.name);
                person.setEmail(personDTO.email);
                person.setPassword(personDTO.password);
                person.setRole(personDTO.role);
                em.merge(person);
                em.getTransaction().commit();
                return person;
            }    }

        public Person getAuthorForRecipe(int id) {
            try(EntityManager em = emf.createEntityManager()){
                Query query = em.createNamedQuery("Person.getAuthorForRecipe");
                query.setParameter("id", id);
                Person author = (Person) query.getSingleResult();
                author.getRecipes().size();
                return author;
            }
        }

        public boolean delete(int id) throws APIException{
            Person foundPerson;
            try(EntityManager em = emf.createEntityManager()){
                em.getTransaction().begin();
                foundPerson = em.find(Person.class, id);
                if(foundPerson == null){
                    throw new APIException(404, "No person with id: " + id + " in database");
                }
                for(Recipe r: foundPerson.getRecipes()){
                    em.remove(r);
                }
                em.remove(foundPerson);
                em.getTransaction().commit();
            }
            try(EntityManager em = emf.createEntityManager()){
                if(em.find(Person.class, id) == null){
                    return true;
                }
            }
            return false;
        }


    public Person getPersonByEmailAndPassword(String email, String password) throws APIException{
        try(EntityManager em = emf.createEntityManager()) {
            TypedQuery<Person> namedQuery = em.createNamedQuery("Person.getPersonByEmail", Person.class);
            namedQuery.setParameter("email", email);
            Person foundPerson = namedQuery.getSingleResult();
            if(foundPerson == null){
                throw new APIException(400, "No user with email: " + email + " is found. Please try again.");
            } else if (!foundPerson.verifyPassword(password)){
                throw new APIException(400, "Incorrect password");
            }
            return foundPerson;
        }
    }

    public Person registerPerson(String email, String password, String name, String role) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();

            Person newPerson = new Person(email, password, name, role);
            em.persist(newPerson);
            em.getTransaction().commit();
            return newPerson;
        }
    }
}
