package com.example.demo.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@ToString
public class MemberResponseDto {
	private String id;
	private String token;
	private String role;
}
