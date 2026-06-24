package jp.co.f1.spring.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.f1.spring.Entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

	public Optional<Order> findByOrderno(int orderno);
}
