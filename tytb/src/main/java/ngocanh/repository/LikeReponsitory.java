package ngocanh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ngocanh.model.Like;

public interface LikeReponsitory extends JpaRepository<Like, Long>{
	//kiểm tra xem 1 like có tồn tại với 1 userId và twitId cụ thể hay không
	@Query("SELECT I FROM Like I WHERE I.user.id=:userId AND I.twit.id=:twitId")
	public Like isLikeExist(@Param("userId") Long userId, @Param("twitId") Long twitId);
	
	//tìm tất cả các like của 1 twitId
	@Query("SELECT I FROM Like I WHERE I.twit.id=:twitId")
	public List<Like> findByTwitId(@Param("twitId") Long twitId);
	

}
