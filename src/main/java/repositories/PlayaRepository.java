package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Playa;

@Repository
public interface PlayaRepository extends JpaRepository<Playa, Integer> {
	
	@Query("select p from Playa p")
	List<Playa> findPlayaForNoAuthenticate();
}