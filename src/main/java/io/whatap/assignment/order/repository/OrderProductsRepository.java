package io.whatap.assignment.order.repository;

import io.whatap.assignment.order.domain.Order;
import io.whatap.assignment.order.domain.OrderProducts;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductsRepository extends JpaRepository<OrderProducts,Long> {

  List<OrderProducts> findAllByOrder(Order order);
}
