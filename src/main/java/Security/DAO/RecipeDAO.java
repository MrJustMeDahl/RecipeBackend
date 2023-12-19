package Security.DAO;

import DTO.RecipeDTO;
import DTO.TagDTO;
import Model.Person;
import Model.Recipe;
import Model.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecipeDAO extends DAO <RecipeDTO, Recipe>{

    //singleton
    private static RecipeDAO instance;
    private RecipeDAO(){}
    public static RecipeDAO getInstance(){
        if(instance == null){
            instance = new RecipeDAO();
        }
        return instance;
    }
    public Recipe create(RecipeDTO recipeDTO, Person author, Set<Tag> tags) {
        Recipe newRecipe = new Recipe(recipeDTO);
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            newRecipe.setAuthor(author);
            newRecipe.addTags(tags);
            em.persist(newRecipe);
            em.merge(author);
            for(Tag t: tags){
                em.merge(t);
            }
            em.getTransaction().commit();
        }
        return newRecipe;
    }

    public Recipe update(RecipeDTO recipeDTO, Person oldAuthor, Person author, Set<Tag> tags) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            Recipe oldRecipe = em.find(Recipe.class, recipeDTO.getId());
            oldRecipe.setName(recipeDTO.getName());
            oldRecipe.setIngredients(recipeDTO.getIngredients());
            oldRecipe.setDescription(recipeDTO.getDescription());
            oldRecipe.setInstructions(recipeDTO.getInstructions());
            oldRecipe.removeAuthor(oldAuthor);
            oldRecipe.setAuthor(author);
            oldRecipe.setTags(tags);
            em.merge(oldRecipe);
            em.getTransaction().commit();
            return oldRecipe;
        }
    }

    public List<Recipe> getAllRecipes(){
        PersonDAO personDAO = PersonDAO.getInstance();
        try(EntityManager em = emf.createEntityManager()){
            List<Recipe> allRecipes = em.createNamedQuery("Recipe.getAllRecipes").getResultList();
            for(Recipe r: allRecipes){
                r.setAuthor(personDAO.getAuthorForRecipe(r.getId()));
                r.getTags().size();
            }
            return allRecipes;
        }
    }

    public Recipe getRecipeByID(int id){
        PersonDAO personDAO = PersonDAO.getInstance();
        try (EntityManager em = emf.createEntityManager()){
            Recipe foundRecipe = em.find(Recipe.class, id);
            foundRecipe.setAuthor(personDAO.getAuthorForRecipe(foundRecipe.getId()));
            foundRecipe.getTags().size();
            return foundRecipe;
        }
    }

    public List<Recipe> getRecipeByName(String name){
        PersonDAO personDAO = PersonDAO.getInstance();
        try(EntityManager em = emf.createEntityManager()){
            Query query = em.createNamedQuery("Recipe.getRecipeByName");
            query.setParameter("name", name);
            List<Recipe> foundRecipes = query.getResultList();
            for(Recipe r: foundRecipes){
                r.setAuthor(personDAO.getAuthorForRecipe(r.getId()));
                r.getTags().size();
            }
            return foundRecipes;
        }
    }

    public Set<Tag> getTagsForRecipe(int id){
        try(EntityManager em = emf.createEntityManager()){
            Query tags = em.createNamedQuery("Tag.getTagsForRecipe");
            tags.setParameter("id", id);
            return new HashSet<>(tags.getResultList());
        }
    }

    public Set<Tag> getTagsByName(Set<TagDTO> tags) {
        Set<Tag> resultTags = new HashSet<>();
        try(EntityManager em = emf.createEntityManager()){
            for(TagDTO t: tags){
                Query query = em.createNamedQuery("Tag.getTagByName");
                query.setParameter("name", t.name);
                resultTags.add((Tag) query.getSingleResult());
            }
            for(Tag t: resultTags){
                t.getRecipes().size();
            }
        }
        return resultTags;
    }
}
