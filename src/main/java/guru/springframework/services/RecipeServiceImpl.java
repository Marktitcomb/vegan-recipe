package guru.springframework.services;

import java.util.*;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {
	
    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;
	
	//@Autowired is default through instructor injection
	public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeCommandToRecipe recipeCommandToRecipe, 
			RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeRepository = recipeRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

	

	@Override
	public Set<Recipe> getRecipes() {
		log.debug("im in the service");
		
		Set<Recipe> recipes = new HashSet<>();
		
		recipeRepository.findAll().forEach(recipes::add);
		
		return recipes;
	}
	


	@Override
	public Recipe findById(Long id) {
		Optional<Recipe> recipe = recipeRepository.findById(id);
		
		if(!recipe.isPresent()) {
			throw new RuntimeException("Cannot find recipe");
		}
		
		return recipe.get();
	}
	
    @Override
    @Transactional //-- 	//Spring dynamically creates a proxy that implements the 
							//same interface(s) as the class you're annotating. 
							//And when clients make calls into your object, the calls are 
							//intercepted and the behaviors injected via the proxy mechanism.
    public RecipeCommand findCommandById(Long l) {
        return recipeToRecipeCommand.convert(findById(l));
    }
	
	/**
	 * Takes in recipe command 
	 * converts to recipe
	 * saves the recipe 
	 * */
	@Override
	@Transactional
	public RecipeCommand saveRecipeCommand(RecipeCommand command) {
		Recipe savedRecipe = recipeCommandToRecipe.convert(command);
		recipeRepository.save(savedRecipe);
		log.debug("Recipe saved: " + savedRecipe.getId());
		return recipeToRecipeCommand.convert(savedRecipe);
	}
	
    @Override
    public void deleteById(Long idToDelete) {
        recipeRepository.deleteById(idToDelete);
    }
	
	
}
