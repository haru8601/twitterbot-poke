package com.haroot.pokebot.twitterapi.auth;

import org.springframework.stereotype.Component;

import com.haroot.pokebot.config.ResourcePathConfig;
import com.haroot.pokebot.config.UserInfoConfig;
import com.haroot.pokebot.dto.TokenDto;
import com.haroot.pokebot.utils.MapperUtils;
import com.twitter.clientlib.TwitterCredentialsOAuth2;
import com.twitter.clientlib.api.TwitterApi;

import lombok.RequiredArgsConstructor;

/**
 * Auth Code with PKCEの認証インスタンス取得用クラス
 * 
 * @author sekiharuhito
 *
 */
@Component
@RequiredArgsConstructor
public class AuthVia20AuthCode {
	private final ResourcePathConfig resourcePathConfig;
	private final MaintainToken maintainToken;
	private final UserInfoConfig userInfoConfig;

	public TwitterApi init() {
		// read token file
		TokenDto tokenDto = MapperUtils.readJson(resourcePathConfig.getToken(), TokenDto.class);
		if (tokenDto == null) {
			return null;
		}

		// initialize credentials
		TwitterCredentialsOAuth2 credentials = new TwitterCredentialsOAuth2(userInfoConfig.getClientId(),
				userInfoConfig.getClientSecret(), tokenDto.getAccessToken(), tokenDto.getRefreshToken());

		// create instance
		TwitterApi apiInstance = new TwitterApi(credentials);
		// リフレッシュ後のコールバック関数を指定
		apiInstance.addCallback(maintainToken);
		return apiInstance;
	}
}
