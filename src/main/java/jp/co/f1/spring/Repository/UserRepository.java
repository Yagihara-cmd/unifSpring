package jp.co.f1.spring.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.f1.spring.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	public Optional<User> findByUseridAndPassword(String userid, String password);
}
