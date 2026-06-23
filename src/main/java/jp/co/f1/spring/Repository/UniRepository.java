package jp.co.f1.spring.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.f1.spring.Entity.Uniform;

@Repository
public interface UniRepository extends JpaRepository<Uniform, String> {
	
	public Optional<Uniform> findByUniid(String uniid);
}
