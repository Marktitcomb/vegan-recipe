package guru.springframework.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService{
	
	private final RecipeRepository recipeRepository;
	private final IngredientToIngredientCommand ingredientToIngredientCommand;
	private final IngredientCommandToIngredient ingredientCommandToIngredient;
	private final UnitOfMeasureRepository unitOfMeasurementRepo;
	
	
	public IngredientServiceImpl(RecipeRepository recipeRepository,
			IngredientToIngredientCommand ingredientToIngredientCommand,
			IngredientCommandToIngredient ingredientCommandToIngredient,
			UnitOfMeasureRepository unitOfMeasurementRepo) {
		this.recipeRepository = recipeRepository;
		this.ingredientCommandToIngredient = ingredientCommandToIngredient;
		this.ingredientToIngredientCommand = ingredientToIngredientCommand;
		this.unitOfMeasurementRepo = unitOfMeasurementRepo;
		
	}


	@Override
	public IngredientCommand findRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
		IngredientCommand ingredientCommand = new IngredientCommand();
		
		Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
		if(!recipeOptional.isPresent()) {
			log.error("Recipe could not be found, id: " + recipeId);
		}
		
		Recipe recipe =  recipeOptional.get();
		
		Optional<IngredientCommand> ingredientCommmandOptional = recipe.getIngredients().stream()
				.filter(e -> e.getId().equals(ingredientId))
				.map(e -> ingredientToIngredientCommand.convert(e)).findFirst();
		
		ingredientCommmandOptional.get().setRecipeId(recipeId);
		
		if(!ingredientCommmandOptional.isPresent()) {
			log.error("Ingredient cannot be found, ID: "+ ingredientId);
		}
		
		return ingredientCommmandOptional.get();
	}


	/**
	 * Function to save to an ingredient or create a new one 
	 * 1. see if the recipe even exists 
	 * 2. if it does then see of the ingredient already exists
	 * 3. if it does then fill in th ingredient with all the command details... see if the unit of measurement exists
	 * 4. if not then create a new ingredient using the convert with command
	 * 5. add the recipe to it and and it to the recipe
	 * 6. then save the recipe
	 * 7. then do one final check to make sure the saved recipe instance includes
	 * an ingredient which matches the command in every way.. description, id, UOM
	 * 8. finall return the saved instance as a command converting it back 
	 * */
	@Override
	@Transactional
	public IngredientCommand saveIngredientCommand(IngredientCommand command) {
		//get recipe details from command
		Long recipeId = command.getRecipeId();
		Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
		
		if(!recipeOptional.isPresent()) {
			log.error("Recipe cannot be found id: " + recipeId);
			return new IngredientCommand();
		}
		else {
			//if recipeID exists in repo then see if the ingredient already exists 
			Recipe recipe = recipeOptional.get();
			Optional<Ingredient> ingredientOptional = recipe
					.getIngredients()
					.stream()
					.filter(e -> e.getId().equals(command.getId()))
					.findFirst();
			//if the ingredient with that ID exists 
			if(ingredientOptional.isPresent()) {
				Ingredient ingredientFound = ingredientOptional.get();
				ingredientFound.setDescription(command.getDescription());
				ingredientFound.setAmount(command.getAmount());
				//see if the unit of measurement exists
				ingredientFound.setUom(unitOfMeasurementRepo
						.findById(command.getUom().getId())
						.orElseThrow(() -> new RuntimeException("Unit of measure not found")
						));
			}
			else {
				Ingredient ingredient = ingredientCommandToIngredient.convert(command);
				ingredient.setRecipe(recipe);
				recipe.addIngredient(ingredient);
			}
			//then you;v either saved a completely new ingredient or added to an existing
			Recipe savedRecipe = recipeRepository.save(recipe);
			
			Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
					.filter(e -> e.getId().equals(command.getId()))
					.findFirst();
			
			if(savedIngredientOptional.isPresent()) {
				savedIngredientOptional = savedRecipe.getIngredients().stream()
						.filter(e -> e.getDescription().equals(command.getDescription()))
						.filter(e -> e.getAmount().equals(command.getAmount()))
						.filter(e -> e.getUom().getId().equals(command.getUom().getId()))
						.findFirst();
			}
			
			return ingredientToIngredientCommand.convert(savedIngredientOptional.get());
		}
		
	
	}


	@Override
	public void deletebyid(Long recipeId, Long idToDelete) {
		// TODO Auto-generated method stub
		
	}

}
