package Security.DAO;

import DTO.RecipeDTO;
import Model.Recipe;

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
