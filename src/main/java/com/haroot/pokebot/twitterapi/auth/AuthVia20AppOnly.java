package com.haroot.pokebot.twitterapi.auth;

import org.springframework.stereotype.Component;

import com.haroot.pokebot.config.UserInfoConfig;
import com.twitter.clientlib.TwitterCredentialsBearer;
import com.twitter.clientlib.api.TwitterApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Bearerの認証インスタンス取得用クラス
 *
 * @author haroot
 *
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthVia20AppOnly {
	private final UserInfoConfig userInfoConfig;

	public TwitterApi init() {
		log.info("start initializing auth(Bearer)");
		String bearer = userInfoConfig.getBearer();
		TwitterCredentialsBearer credentials = new TwitterCredentialsBearer(bearer);
		TwitterApi apiInstance = new TwitterApi(credentials);
		log.info("end initializing auth(Bearer)");
		return apiInstance;
	}
}
