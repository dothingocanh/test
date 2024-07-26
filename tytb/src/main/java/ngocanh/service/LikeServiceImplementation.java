package ngocanh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ngocanh.exception.TwitException;
import ngocanh.exception.UserException;
import ngocanh.model.Like;
import ngocanh.model.Twit;
import ngocanh.model.User;
import ngocanh.repository.LikeReponsitory;
import ngocanh.repository.TwitRepository;

@Service
public class LikeServiceImplementation implements LikeService{

	@Autowired
	private LikeReponsitory likeReponsitory;
	
	@Autowired
	private TwitService twitService;
	
	@Autowired
	private TwitRepository twitRepository;
	
	
	@Override
	public Like likeTwit(Long twitId, User user) throws UserException, TwitException {
		//kiểm tra sự tồn tại của like ứng với 1 userId và 1 twitId cụ thể
		Like isLikeExist= likeReponsitory.isLikeExist(user.getId(), twitId);
		
		if (isLikeExist!= null) {
			likeReponsitory.deleteById(isLikeExist.getId());
			return isLikeExist;
		}
		
		//tìm twit theo twitId
		Twit twit= twitService.findById(twitId);
		Like like = new Like();
		like.setTwit(twit);
		like.setUser(user);
		
		Like savedLike= likeReponsitory.save(like);
		twit.getLikes().add(savedLike);
		twitRepository.save(twit);
		return savedLike;
	}

	@Override
	public List<Like> getAllLikes(Long twitId) throws TwitException {
		Twit twit= twitService.findById(twitId);
		List<Like> likes=likeReponsitory.findByTwitId(twitId);
		
		
		return likes;
	}
	

	
}
