package guru.springframework.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Recipe {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String description;
	private Integer prepTime;
	private Integer cookTime;
	private Integer servings;
	private String source;
	private String url;
	
	@Lob // large object
	private String directions;
	
	//mappedBy creates a bidirectional relationship between recipe and ingredients 
	//in other words they both know about each other
	//you can find a recipe from ingredients and visa versa
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe")
	private Set<Ingredient> ingredients = new HashSet<>();
	
	@Lob
	private Byte[] image;
	
	//difficult to add an enum type in a db, here we're telling JPA to add it as a String
	@Enumerated(value = EnumType.STRING)
	private Difficulty difficulty;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Notes notes;
	
	@ManyToMany// below we're joining the tables both ways to create a brand new table
	@JoinTable(name = "recipe_catagory", joinColumns = @JoinColumn(name = "recipe_id"),
			   inverseJoinColumns = @JoinColumn(name = "catagory_id"))
	private Set<Category> categories = new HashSet<>();
	
	public void setNotes(Notes notes) {
		this.notes = notes;
		notes.setRecipe(this);
	}
	
	public Recipe addIngredient(Ingredient ingredient) {
		ingredient.addToRecipe(this);
		this.ingredients.add(ingredient);
		return this;
		
	}
	
}
