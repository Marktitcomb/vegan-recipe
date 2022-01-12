package guru.springframework.services;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.*;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class IngredientServiceImplTest {
	
	final private IngredientToIngredientCommand ingredientToIngredientCommand;
	final private IngredientCommandToIngredient ingredientCommandToIngredient;
	
	@Mock
	RecipeRepository recipeRepository;
	
	@Mock
	UnitOfMeasureRepository unitOfMeasurementRepository;
	
	IngredientService ingredientService;
	
	public IngredientServiceImplTest() {
		this.ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
		this.ingredientCommandToIngredient = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
	}
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);//using this makes it go over all of the @Mocks in the class
		
		ingredientService = new IngredientServiceImpl(recipeRepository, 
				ingredientToIngredientCommand, ingredientCommandToIngredient,
				unitOfMeasurementRepository);
		
	}
	
	/**
	 * Create ingredients
	 * Create recipe
	 * add ingredient to recipe
	 * confirm there Id remain the same once the service is called 
	 * create default return from recipe repository
	 * */
	@Test
	public void findRecipeIdAndIngredientIdtest() {
		
		//setting up what to return 
		Recipe recipe = new Recipe();
		recipe.setId(1L);
		
		Ingredient ingredientOne = new Ingredient();
		ingredientOne.setId(1L);
		Ingredient ingredientTwo = new Ingredient();
		ingredientTwo.setId(2L);
		Ingredient ingredientThree = new Ingredient();
		ingredientThree.setId(3L);
		
		recipe.addIngredient(ingredientOne);
		recipe.addIngredient(ingredientTwo);
		recipe.addIngredient(ingredientThree);
		
		Optional<Recipe> recipeOption = Optional.of(recipe);
		
		when(recipeRepository.findById(anyLong())).thenReturn(recipeOption);
		
		//calling the ingredientservice 
		IngredientCommand ingredientCommand = ingredientService.findRecipeIdAndIngredientId(1L, 3L);
		
		assertEquals(Long.valueOf(3L), ingredientCommand.getId());
		assertEquals(Long.valueOf(1L), ingredientCommand.getRecipeId());
		verify(recipeRepository, times(1)).findById(anyLong());
		
	}
	
	/**
	 * makes an ingredient command 
	 * then a recipe optional 
	 * then a saved recipe
	 * when repo.findBY ID retunr the recipeOptional
	 * when repoSave return the saved recipe 
	 * 
	 * do the save ingredient command 
	 * */
	@Test
	public void testSaveRecipeCommand() throws Exception{
		IngredientCommand command = new IngredientCommand();
		command.setId(3L);
		command.setRecipeId(2L);
		command.setDescription("description");
		command.setAmount(new BigDecimal(3));
		UnitOfMeasureCommand uomCommand = new UnitOfMeasureCommand();
		uomCommand.setId(3L);
		command.setUom(uomCommand);
		
		
		
		Optional<Recipe> recipeOptional = Optional.of(new Recipe());
		
		Recipe savedRecipe = new Recipe();
		
		Ingredient newIngredient = new Ingredient();
		newIngredient.setId(3L);
		newIngredient.setDescription("description");
		newIngredient.setAmount(new BigDecimal(3));
		UnitOfMeasure uom = new UnitOfMeasure();
		uom.setId(3L);
		newIngredient.setUom(uom);
		
		savedRecipe.addIngredient(newIngredient);
		savedRecipe.getIngredients().iterator().next().setId(3L);
		
		when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
		when(recipeRepository.save(any())).thenReturn(savedRecipe);
		
		IngredientCommand saveCommand = ingredientService.saveIngredientCommand(command);
		
		assertEquals(Long.valueOf(3), saveCommand.getId());
		verify(recipeRepository, times(1)).findById(anyLong());
		verify(recipeRepository, times(1)).save(any());
		
	}
	
	

}
