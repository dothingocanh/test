package ngocanh.service;

import java.util.List;

import ngocanh.exception.TwitException;
import ngocanh.exception.UserException;
import ngocanh.model.Like;
import ngocanh.model.User;

public interface LikeService {

	public Like likeTwit(Long twitId, User user) throws UserException, TwitException;
	
	public List<Like> getAllLikes(Long twitId) throws TwitException;
	
}
