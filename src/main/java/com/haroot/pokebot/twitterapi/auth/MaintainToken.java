package com.haroot.pokebot.twitterapi.auth;

import org.springframework.stereotype.Component;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.haroot.pokebot.config.ResourcePathConfig;
import com.haroot.pokebot.dto.TokenDto;
import com.haroot.pokebot.utils.MapperUtils;
import com.twitter.clientlib.ApiClientCallback;

import lombok.RequiredArgsConstructor;

/**
 * トークン更新実装クラス
 * 
 * @author sekiharuhito
 *
 */
@Component
@RequiredArgsConstructor
class MaintainToken implements ApiClientCallback {
	private final ResourcePathConfig resourcePathConfig;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onAfterRefreshToken(OAuth2AccessToken accessToken) {
		System.out.println("try refreshing token.");
		// set new token to root
		TokenDto tokenDto = new TokenDto();
		tokenDto.setAccessToken(accessToken.getAccessToken());
		tokenDto.setRefreshToken(accessToken.getRefreshToken());

		// update json file
		boolean writeFlg = MapperUtils.writeJson(resourcePathConfig.getToken(), tokenDto);
		if (!writeFlg) {
			System.err.println("cannot refresh token...");
			return;
		}
		System.out.println("refreshed token!");
		return;
	}
}