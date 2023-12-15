package Security.DAO;

import Config.HibernateConfig;
import DTO.RecipeDTO;
import Model.Recipe;
import jakarta.persistence.EntityManagerFactory;

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
    @Override
    public Recipe create(RecipeDTO recipeDTO) {
        return super.create(recipeDTO);
    }

    @Override
    public Recipe update(RecipeDTO recipeDTO) {
        return super.update(recipeDTO);
    }
}
