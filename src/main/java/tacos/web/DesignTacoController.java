package tacos.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.extern.slf4j.Slf4j;
import tacos.Taco;
import tacos.Order;
import tacos.data.IngredientRepository;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.data.TacoRepository;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {

	private final IngredientRepository ingredientRepo;
	private TacoRepository designRepo;

	@Autowired
	public DesignTacoController(IngredientRepository ingredientRepo, TacoRepository designRepo) {
		this.ingredientRepo = ingredientRepo;
		this.designRepo = designRepo;
	}

	// Se hace saber a Spring que el valor de Order se obtiene del Modelo
	@ModelAttribute(name = "order")
	public Order order() {
		return new Order();
	}

	@ModelAttribute(name = "taco")
	public Taco taco() {
		return new Taco();
	}

	@GetMapping
	public String showDesignForm(Model model) {
		List<Ingredient> ingredients = new ArrayList<>();
		ingredientRepo.findAll().forEach(i -> ingredients.add(i));
		Type[] types = Ingredient.Type.values();
		for (Type type : types) {
			// Se agrega al modelo: (wrap,(lista de ingredientes de tipo wrap)) y asi con todos los tipos
			model.addAttribute(type.toString().toLowerCase(),
			filterByType(ingredients, type));
		}
		return "design";
	}

	@PostMapping
	public String processDesign(@Valid Taco taco, Errors errors, @ModelAttribute Order order) {
		if (errors.hasErrors()) {
			return "design";
		}
		
		Taco saved = designRepo.save(taco);
 		order.addDesign(saved);
		log.info("Processing design: " + taco);
		return "redirect:/orders/current";
	}

	private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
        return ingredients.stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }
}