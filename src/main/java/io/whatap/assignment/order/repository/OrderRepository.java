package io.whatap.assignment.order.repository;

import io.whatap.assignment.order.domain.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
  List<Order> findAllByRequesterId(final String requesterId);
}
