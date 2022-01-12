package guru.springframework.domain;

import java.math.BigDecimal;

import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = {"recipe"})
@Entity
public class Ingredient {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String description;
	private BigDecimal amount;
	
	/*
	 * Now when you load a Ingredient from the database, JPA loads its id, description, and amount fields for you. 
	 * But you have two options for how uom should be loaded:
	
		To load it together with the rest of the fields (i.e. eagerly), or
		To load it on-demand (i.e. lazily) when you call the Ingredient's getUOM() method.
	 * */
	
	@OneToOne(fetch = FetchType.EAGER)
	private UnitOfMeasure uom;
	
	@ManyToOne
	private Recipe recipe;
	
	public Ingredient() {}

	public void addToRecipe(Recipe recipe) {
		// TODO Auto-generated method stub
		
	}
	
    public Ingredient(String description, BigDecimal amount, UnitOfMeasure uom) {
        this.description = description;
        this.amount = amount;
        this.uom = uom;
    }

    public Ingredient(String description, BigDecimal amount, UnitOfMeasure uom, Recipe recipe) {
        this.description = description;
        this.amount = amount;
        this.uom = uom;
        this.recipe = recipe;
    }
	
	

}
