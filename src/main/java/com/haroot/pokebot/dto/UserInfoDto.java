package com.haroot.pokebot.dto;

import lombok.Data;

@Data
public class UserInfoDto {
	private String myId;
	private String clientId;
	private String clientSecret;
	private String bearer;
	private String redirectUrl;
	private String accessScope;
}
