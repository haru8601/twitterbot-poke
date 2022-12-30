package com.haroot.pokebot.twitterapi.auth;

import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.api.TwitterApi;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenUtils {
	public static boolean refreshToken(TwitterApi apiInstance) {
		log.info("start refresh token.");
		try {
			apiInstance.refreshToken();
			log.info("refreshed access token.");
			return true;
		} catch (ApiException ex) {
			log.error("cannot refresh token...");
			log.error(ex.getMessage(), ex);
		}
		return false;
	}
}
