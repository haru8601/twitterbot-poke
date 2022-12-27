package com.haroot.pokebot.twitterapi.auth;

import org.springframework.stereotype.Component;

import com.haroot.pokebot.config.ResourcePathConfig;
import com.haroot.pokebot.dto.UserInfoDto;
import com.haroot.pokebot.utils.MapperUtils;
import com.twitter.clientlib.TwitterCredentialsBearer;
import com.twitter.clientlib.api.TwitterApi;

import lombok.RequiredArgsConstructor;

/**
 * Bearerの認証インスタンス取得用クラス
 * 
 * @author sekiharuhito
 *
 */
@Component
@RequiredArgsConstructor
public class AuthVia20AppOnly {
	private final ResourcePathConfig resourcePathConfig;

	public TwitterApi init() {
		UserInfoDto userInfoDto = MapperUtils.readJson(resourcePathConfig.getUserInfo(), UserInfoDto.class);
		String bearer = userInfoDto.getBearer();
		TwitterCredentialsBearer credentials = new TwitterCredentialsBearer(bearer);
		TwitterApi apiInstance = new TwitterApi(credentials);
		return apiInstance;
	}
}
