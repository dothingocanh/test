package ngocanh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ngocanh.model.Twit;
import ngocanh.model.User;

public interface TwitRepository extends JpaRepository<Twit, Long>{

	List<Twit>findAllByIsTwitTrueOrderByCreatedAtDesc();
	
	List<Twit> findByRetwitUserContainsOrUser_IdAndIsTwitTrueOrderByCreatedAtDesc(User user, Long userId);
	
	List<Twit> findByLikesContainingOrderByCreatedAtDesc(User user);
	
	@Query("SELECT t FROM Twit t JOIN t.likes I WHERE I.user.id=:userId")
	List<Twit> findByLikesUser_id(Long userId);
	
}
