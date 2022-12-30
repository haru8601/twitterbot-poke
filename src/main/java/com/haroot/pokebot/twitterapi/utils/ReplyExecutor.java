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
import lombok.extern.slf4j.Slf4j;

/**
 * リプライ実行クラス
 * 
 * @author sekiharuhito
 *
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ReplyExecutor {
	private final AuthVia20AuthCode authVia20AuthCode;
	private final ResourcePathConfig resourcePathConfig;

	/**
	 * リプライ処理実行関数
	 * 
	 * @param receivedTweet リプライ対象ツイート
	 */
	public void exec(Tweet receivedTweet) {
		log.info(receivedTweet.getText());
		// initialize instance.
		TwitterApi apiInstance = authVia20AuthCode.init();
		if (apiInstance == null) {
			return;
		}
		log.info("initialized apiInstance");

		// メイン処理
		log.info("start tweet");
		// find target poke
		List<PokedexDto> pokeList = MapperUtils.readJsonAsList(resourcePathConfig.getPokedex(),
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
			log.info("end tweet.");
			log.info("used access token.");
			return;
		}

		// エラー処理
		// token更新して再度実行
		log.info("start refresh token.");
		try {
			apiInstance.refreshToken();
		} catch (Exception ex) {
			log.error("cannot refresh token...");
			log.error(ex.getMessage(), ex);
			return;
		}
		log.info("refreshed access token.");

		// 再実行
		log.info("restart tweet.");
		authFlg = Tweets.tweetAsReply(apiInstance, resText, receivedTweet);
		if (authFlg) {
			log.info("end tweet.");
		} else {
			log.error("cannot tweet...");
		}
		return;
	}
}