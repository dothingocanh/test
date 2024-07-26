package ngocanh.util;

import java.util.Iterator;

import ngocanh.model.Like;
import ngocanh.model.Twit;
import ngocanh.model.User;

public class TwitUtil {
	
	public final static boolean isLikedByReqUser(User reqUser, Twit twit) {
		for(Like like: twit.getLikes()) {
			if (like.getUser().getId().equals(reqUser.getId())) {
				return true;
			}
		}
		return false;
	}
	public final static boolean isRetiwtedByReqUser(User reqUser, Twit twit) {
		for(User user:twit.getRetwitUser()) {
			if (user.getId().equals(reqUser.getId())) {
				return true;
			}
		}
		return false;
	}

}
