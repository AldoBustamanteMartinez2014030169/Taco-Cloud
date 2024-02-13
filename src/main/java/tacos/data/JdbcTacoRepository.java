package tacos.data;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import tacos.Ingredient;
import tacos.Taco;

@Repository
public class JdbcTacoRepository implements TacoRepository {
	private JdbcTemplate jdbc;
	
	public JdbcTacoRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}
	
	@Override
	public Taco save(Taco taco) {
		long tacoId = saveTacoInfo(taco);
		taco.setId(tacoId);
		// taco.getIngredients() es una lista de Strings
		for (String ingredient : taco.getIngredients()) {
			saveIngredientToTaco(ingredient, tacoId);
		}
		return taco;
	}

	// Como necesitamos leer el ID de la base de datos, se usa update con un PSC y un KeyHolder
	private long saveTacoInfo(Taco taco) {
		taco.setCreatedAt(new Date());

		PreparedStatementCreatorFactory preparedStatementCreatorFactory = new PreparedStatementCreatorFactory(
			"insert into Taco (name, createdAt) values (?, ?)",
			Types.VARCHAR, Types.TIMESTAMP
		);
		preparedStatementCreatorFactory.setReturnGeneratedKeys(true);
		PreparedStatementCreator psc = preparedStatementCreatorFactory.newPreparedStatementCreator(
			Arrays.asList(
			"aldo", new Timestamp(taco.getCreatedAt().getTime())));

		// PreparedStatementCreator psc = new PreparedStatementCreatorFactory(
		// 	"insert into Taco (name, createdAt) values (?, ?)",
		// 	Types.VARCHAR, Types.TIMESTAMP
		// 	).newPreparedStatementCreator(
		// 		Arrays.asList(
		// 		taco.getName(), new Timestamp(taco.getCreatedAt().getTime())));
		KeyHolder keyHolder = new GeneratedKeyHolder();
		// Con UPDATE se crea un nuevo registro en la BD junto con su ID, y se recupera esta ID en KEYHOLDER
		jdbc.update(psc, keyHolder);
		return keyHolder.getKey().longValue();
	}

	private void saveIngredientToTaco(String ingredient, long tacoId) {
		jdbc.update(
			"insert into Taco_Ingredients (taco, ingredient) " + "values (?, ?)",
			tacoId, ingredient);
	}
}