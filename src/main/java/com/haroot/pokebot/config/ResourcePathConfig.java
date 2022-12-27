package com.haroot.pokebot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
@ConfigurationProperties(prefix = "file-path.resources")
public class ResourcePathConfig {
	private String baseUrl = "";
	private String userInfo;
	private String token;
	private String pokedex;

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = addResourcePath(userInfo);
	}

	public void setToken(String token) {
		this.token = addResourcePath(token);
	}

	public void setPokedex(String pokedex) {
		this.pokedex = addResourcePath(pokedex);
	}

	private String addResourcePath(String filePath) {
		return baseUrl + filePath;
	}
}
