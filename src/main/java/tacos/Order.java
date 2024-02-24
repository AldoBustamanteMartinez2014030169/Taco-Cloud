package tacos;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.CreditCardNumber;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import tacos.Taco;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Data
@Entity
@Table(name="Taco_Order")
public class Order implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
 	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private Date placedAt;
	@NotBlank(message="Name is required")
	private String name;
	@NotBlank(message="street is required")
	private String street;
	@NotBlank(message="city is required")
	private String city;
	@NotBlank(message="state is required")
	private String state;
	@NotBlank(message="zip code is required")
	private String zip;
	@CreditCardNumber(message="Not a valid credit card number")
	private String ccNumber;
	@Pattern(regexp="^(0[1-9]|1[0-2])([\\/])([1-9][0-9])$", message="Must be formatted MM/YY")
	private String ccExpiration;
	@Digits(integer=3, fraction=0, message="Invalid CVV")
	private String ccCVV;
	@ManyToMany(targetEntity=Taco.class)
	private List<Taco> tacos;

	public void addDesign(Taco taco){
		if(tacos == null){
			tacos = new ArrayList<>();
		}
		tacos.add(taco);
	}

	@PrePersist
	void placedAt() {
		this.placedAt = new Date();
	}
}
