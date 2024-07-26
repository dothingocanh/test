package ngocanh.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;


@Data
public class TwitDto {
	private Long id;
	private String content;
	private String image;
	private String video;
	private UserDto user;
	private LocalDateTime createAt;
	private int totalLikes;
	private int totalReplies;
	private int totalRetweets;
	private boolean isLiked;

	private boolean isRetwit;
	private List<Long> retwitUsersId;
	private List<TwitDto> replyTwits;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}
	public UserDto getUser() {
		return user;
	}
	public void setUser(UserDto user) {
		this.user = user;
	}
	public LocalDateTime getCreateAt() {
		return createAt;
	}
	public void setCreateAt(LocalDateTime createAt) {
		this.createAt = createAt;
	}
	public int getTotalLikes() {
		return totalLikes;
	}
	public void setTotalLikes(int totalLikes) {
		this.totalLikes = totalLikes;
	}
	public int getTotalReplies() {
		return totalReplies;
	}
	public void setTotalReplies(int totalReplies) {
		this.totalReplies = totalReplies;
	}
	public int getTotalRetweets() {
		return totalRetweets;
	}
	public void setTotalRetweets(int totalRetweets) {
		this.totalRetweets = totalRetweets;
	}
	public boolean isLiked() {
		return isLiked;
	}
	public void setLiked(boolean isLiked) {
		this.isLiked = isLiked;
	}
	public boolean isRetwit() {
		return isRetwit;
	}
	public void setRetwit(boolean isRetwit) {
		this.isRetwit = isRetwit;
	}
	public List<Long> getRetwitUserId() {
		return retwitUsersId;
	}
	
	public List<Long> getRetwitUsersId() {
		return retwitUsersId;
	}
	public void setRetwitUsersId(List<Long> retwitUsersId) {
		this.retwitUsersId = retwitUsersId;
	}
	public List<TwitDto> getReplyTwits() {
		return replyTwits;
	}
	public void setReplyTwits(List<TwitDto> replyTwits) {
		this.replyTwits = replyTwits;
	}
	
	
}
