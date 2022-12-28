package com.haroot.pokebot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@ConfigurationProperties(prefix = "user-info")
public class UserInfoConfig {
	private String myId;
	private String clientId;
	private String clientSecret;
	private String bearer;
	private String redirectUrl;
	private String accessScope;
}
