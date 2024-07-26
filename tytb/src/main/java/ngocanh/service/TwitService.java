package ngocanh.service;

import java.util.List;



import ngocanh.exception.TwitException;
import ngocanh.exception.UserException;
import ngocanh.model.Twit;
import ngocanh.model.User;
import ngocanh.request.TwitReplyReques;


public interface TwitService {
	public Twit createTwit(Twit req, User user) throws UserException;
	public List<Twit> findAllTwit();
	public Twit retwit(Long twitId, User user) throws UserException, TwitException;
	public Twit findById(Long twitId) throws TwitException;
	
	public void deleteTwitById(Long twitId, Long userId)throws TwitException, UserException;
	
	public Twit removeFromRetwit(Long twitId, User user) throws TwitException, UserException;

	public Twit createdReply(TwitReplyReques req, User user)throws TwitException;
	
//	public List<Twit> getUserTwit(User user);
	public List<Twit> getUserTwit(Long userId);
	public List<Twit> findByLikesContainsUser(User user);
}
