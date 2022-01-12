package guru.springframework.domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CategoryTest {
	
	Category category;
	
	
	@Before
	public void setUp() {
		category = new Category();
	}
	
	@Test
	public void getId() throws Exception{
		Long id = 4L;
		
		category.setId(id);
		
		assertEquals(id, category.getId());
	}
	
	@Test
	public void getDescription() throws Exception{
		String dsc = "randomDescription";
		category.setDescription(dsc);
		
		assertEquals(dsc, category.getDescription());
	}


}
