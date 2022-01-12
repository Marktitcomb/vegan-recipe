package guru.springframework.services;


import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {
	
	RecipeServiceImpl recipeService;
	
	@Mock // so the class or interfce can be mocked to record 
	//its behaviours 
	RecipeRepository recipeRepository;
	
	@Mock
	RecipeToRecipeCommand recipeToRecipeCommand;
	
	@Mock
	RecipeCommandToRecipe recipeCommandToRecipe;
	
	@Before
	public void setUp() throws Exception{
		MockitoAnnotations.initMocks(this);
		
		recipeService = new RecipeServiceImpl(recipeRepository, recipeCommandToRecipe, recipeToRecipeCommand);
	}
	/**
	 * Create a recipe
	 * make it an option
	 * say this is whats returned from the repository "When"
	 * make a recipe = receipe service get (id)
	 * assertNotNull
	 * verify the findBYId in repository was only called once
	 * verify the findAll was never called
	 * 
	 * */
	@Test
	public void getRecipeByIdTest() throws Exception{
		Recipe recipe = new Recipe();
		recipe.setId(1L);
		Optional recipeOption = Optional.of(recipe);
		
		when(recipeRepository.findById(anyLong())).thenReturn(recipeOption);
		
		Recipe returnedRecipe = recipeService.findById(1L);
		
		assertNotNull("Null Recipe Returned", returnedRecipe);
		verify(recipeRepository, times(1)).findById(anyLong());
		verify(recipeRepository, never()).findAll();
		
		
	}
	
	/**
	 * Same as above but for recipe command
	 * */
	@Test
    public void getRecipeCommandByIdTest() throws Exception {
		
		//making a recipe return value
		Recipe recipe = new Recipe();
		recipe.setId(1L);
		Optional recipeOption = Optional.of(recipe);
		
		when(recipeRepository.findById(anyLong())).thenReturn(recipeOption);
		
		//making a command return value
		RecipeCommand recipeCommand = new RecipeCommand();
		recipeCommand.setId(1L);
		
		when(recipeToRecipeCommand.convert(any())).thenReturn(recipeCommand);
		
		//then combining to two return values, the convert and get by ID
		RecipeCommand commandbyid = recipeToRecipeCommand.convert(recipeService.findById(1L));
		
		assertNotNull("Command is null", commandbyid);
		verify(recipeRepository, times(1)).findById(any());
		verify(recipeRepository, never()).findAll();
		
	}
	
	/**
	 * here we only wanted findall() to be called in the repository
	 * */
	@Test
    public void getRecipesTest() throws Exception {
		Recipe recipe = new Recipe();
		Set<Recipe> recipeSet = new HashSet<>();
		recipeSet.add(recipe);
		
        when(recipeService.getRecipes()).thenReturn(recipeSet);

        Set<Recipe> recipes = recipeService.getRecipes();

        assertEquals(recipes.size(), 1);
        verify(recipeRepository, times(1)).findAll();
        verify(recipeRepository, never()).findById(anyLong());
	}
	
	/**
	 * just verify that delete was only called once 
	 * */
	@Test
	public void testDeleteById() throws Exception {
		
		Long idToDelete = Long.valueOf(2L);
		
		recipeService.deleteById(idToDelete); //no when here as void return type
		
		verify(recipeRepository, times(1)).deleteById(any());
	}
	
	

}
