package guru.springframework.repositories;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import guru.springframework.db_init.RecipeInit;
import guru.springframework.domain.Recipe;
import guru.springframework.domain.UnitOfMeasure;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RecipeRepositoryTest {

	@Autowired
	RecipeRepository recipeRepo;
	

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void findRecipe() throws Exception {
		Recipe guacRecipe = new Recipe();
		recipeRepo.save(guacRecipe);
		long id = 1L;
		Optional<Recipe> recipeOptional = recipeRepo.findById(id);
		assertEquals(id, recipeOptional.get().getId());

	}

	@Test
	public void getAllRecipes() throws Exception {
		Recipe testRecipe = new Recipe();
		Recipe testTwoRecipe = new Recipe();
		recipeRepo.save(testRecipe);
		recipeRepo.save(testTwoRecipe);
		
		
		Set<Recipe> recipes = new HashSet<Recipe>();
		recipeRepo.findAll().iterator().forEachRemaining(recipes::add);

		assertEquals(2, recipes.size());
	}

}
