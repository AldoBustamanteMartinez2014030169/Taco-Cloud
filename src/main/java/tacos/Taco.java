package tacos;

import java.util.List;
import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Data
@Entity
public class Taco {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private Date createdAt;
	@NotBlank(message = "Name must not be blank")
	private String name;
	@ManyToMany(targetEntity=Ingredient.class)
	@NotBlank(message = "You must choose at least 1 ingredient")
	private List<String> ingredients; // Lista de todos los IDs de los ingredientes del taco creado ("FLTO", "CARN", "SLSA")
	
	@PrePersist
	void createdAt() {
		this.createdAt = new Date();
	}
}
