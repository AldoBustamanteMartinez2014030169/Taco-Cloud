package tacos;

import java.util.List;
import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class Taco {
	private Long id;
	private Date createdAt;
	@NotBlank(message = "Name must not be blank")
	private String name;
	@NotBlank(message = "You must choose at least 1 ingredient")
	private List<String> ingredients; // Lista de todos los IDs de los ingredientes del taco creado ("FLTO", "CARN", "SLSA")
}
