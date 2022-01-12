package guru.springframework.domain;

import lombok.Data;

import java.util.Set;
import javax.persistence.*;


@Data
@Entity
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String description;

	@ManyToMany(mappedBy = "categories")//categories is a field variable in recipes 
	private Set<Recipe> recipes;



}
