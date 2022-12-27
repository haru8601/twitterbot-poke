package com.haroot.pokebot.dto;

import lombok.Data;

@Data
public class TokenDto {
	private String accessToken;
	private String refreshToken;
}
