package com.example.demo.chat.domain.dto;

import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ChatRoomDto {

	private Set<Object> byName;
	private int count;
	
	public static ChatRoomDto create(Set<Object> byName,int count) {
		return ChatRoomDto.builder()
				.byName(byName)
				.count(count)
				.build();
	}
	
	@Builder
	public ChatRoomDto(Set<Object> byName,int count) {
		this.byName=byName;
		this.count=count;
	}
}
