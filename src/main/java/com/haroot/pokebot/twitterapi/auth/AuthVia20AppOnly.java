package com.haroot.pokebot.twitterapi.auth;

import org.springframework.stereotype.Component;

import com.haroot.pokebot.config.UserInfoConfig;
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
	private final UserInfoConfig userInfoConfig;

	public TwitterApi init() {
		String bearer = userInfoConfig.getBearer();
		TwitterCredentialsBearer credentials = new TwitterCredentialsBearer(bearer);
		TwitterApi apiInstance = new TwitterApi(credentials);
		return apiInstance;
	}
}
