package unibo.springHateoas;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrdersForCustomer(String customerId);

    Order getOrderByIdForCustomer(String customerId, String orderId);
}
