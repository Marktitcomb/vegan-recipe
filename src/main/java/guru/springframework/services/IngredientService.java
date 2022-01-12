package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;

public interface IngredientService {
	
	IngredientCommand findRecipeIdAndIngredientId(Long recipeId, Long ingredidentId);
	
	IngredientCommand saveIngredientCommand(IngredientCommand command);
	
	void deletebyid(Long recipeId, Long idToDelete);
	

}
