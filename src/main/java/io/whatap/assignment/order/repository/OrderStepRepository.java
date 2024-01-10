package io.whatap.assignment.order.repository;

import io.whatap.assignment.order.domain.OrderSteps;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStepRepository extends JpaRepository<OrderSteps,Long> {

}
