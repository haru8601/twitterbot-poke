package com.haroot.pokebot.twitterapi.utils;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.haroot.pokebot.config.ResourcePathConfig;
import com.haroot.pokebot.dto.PokedexDto;
import com.haroot.pokebot.twitterapi.auth.AuthVia20AuthCode;
import com.haroot.pokebot.utils.MapperUtils;
import com.haroot.pokebot.utils.PokeUtils;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.model.Tweet;

import lombok.RequiredArgsConstructor;

/**
 * リプライ実行クラス
 * 
 * @author sekiharuhito
 *
 */
@Component
@RequiredArgsConstructor
public class ReplyExecutor {
	private final AuthVia20AuthCode authVia20AuthCode;
	private final ResourcePathConfig resourcePathConfig;

	/**
	 * リプライ処理実行関数
	 * 
	 * @param receivedTweet リプライ対象ツイート
	 */
	public void exec(Tweet receivedTweet) {
		System.out.println(receivedTweet.getText());
		// initialize instance.
		TwitterApi apiInstance = authVia20AuthCode.init();
		if (apiInstance == null) {
			return;
		}
		System.out.println("initialized apiInstance");

		// メイン処理
		System.out.println("start tweet.");
		// find target poke
		List<PokedexDto> pokeList = MapperUtils.readJson(resourcePathConfig.getPokedex(),
				new TypeReference<List<PokedexDto>>() {
				});
		PokedexDto targetPoke = PokeUtils.findPoke(pokeList, receivedTweet.getText());
		// make response text
		String resText = "それは新しいポケモン...?";
		if (targetPoke != null) {
			resText = targetPoke.getName().getJapanese() + PokeUtils.getPokeInfo(targetPoke);
		}
		boolean authFlg = Tweets.tweetAsReply(apiInstance, resText, receivedTweet);
		// 成功したら終了
		if (authFlg) {
			System.out.println("end tweet.");
			System.out.println("used access token.");
			return;
		}

		// エラー処理
		// token更新して再度実行
		System.out.println("start refresh token.");
		try {
			apiInstance.refreshToken();
		} catch (Exception e) {
			System.err.println("cannot refresh token...");
			e.printStackTrace();
			return;
		}
		System.out.println("refreshed access token.");

		// 再実行
		System.out.println("restart tweet.");
		authFlg = Tweets.tweetAsReply(apiInstance, resText, receivedTweet);
		if (authFlg) {
			System.out.println("end tweet.");
		} else {
			System.err.println("cannot tweet...");
		}
		return;
	}
}