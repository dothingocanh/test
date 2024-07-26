package ngocanh.dto.mapper;

import java.lang.invoke.CallSite;
import java.util.ArrayList;
import java.util.List;

import ngocanh.dto.LikeDto;
import ngocanh.dto.TwitDto;
import ngocanh.dto.UserDto;
import ngocanh.model.Like;
import ngocanh.model.User;

public class LikeDtoMapper {
	public static LikeDto toLikeDto(Like like, User reqUser) {
		UserDto user = UserDtoMapper.toUserDto(like.getUser());
		UserDto reqUserDto = UserDtoMapper.toUserDto(reqUser);
		TwitDto twit = TwitDtoMapper.toTwitDto(like.getTwit(), reqUser);
		
		LikeDto likeDto= new LikeDto();
		likeDto.setId(like.getId());
		likeDto.setTwit(twit);
		likeDto.setUser(user);
		
		
		return likeDto;
	}
	public static List<LikeDto> toLikeDtos(List<Like> likes, User reqUser){
		List<LikeDto> likeDtos= new ArrayList<>();
		
		for(Like like:likes) {
			UserDto user= UserDtoMapper.toUserDto(like.getUser());
			TwitDto twit = TwitDtoMapper.toTwitDto(like.getTwit(), reqUser);

			LikeDto likeDto= new LikeDto();
			likeDto.setId(like.getId());
			likeDto.setTwit(twit);
			likeDto.setUser(user);
			likeDtos.add(likeDto);
			
			
		}
		return likeDtos;
	}

}
