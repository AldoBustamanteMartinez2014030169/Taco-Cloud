package tacos.data;

import tacos.Order;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

// public interface OrderRepository {
// 	Order save(Order order);
// }

public interface OrderRepository extends CrudRepository<Order, Long> {
	List<Order> findByDeliveryZip(String deliveryZip);
	List<Order> readOrdersByDeliveryZipAndPlacedAtBetween(String deliveryZip, Date startDate, Date endDate);
}