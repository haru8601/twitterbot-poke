package com.haroot.pokebot.batch;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.haroot.pokebot.config.ResourcePathConfig;
import com.haroot.pokebot.dto.PokedexDto;
import com.haroot.pokebot.twitterapi.auth.AuthVia20AuthCode;
import com.haroot.pokebot.twitterapi.utils.Tweets;
import com.haroot.pokebot.utils.MapperUtils;
import com.haroot.pokebot.utils.PokeUtils;
import com.twitter.clientlib.api.TwitterApi;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TweetBatch {
	private final AuthVia20AuthCode authVia20AuthCode;
	private final ResourcePathConfig resourcePathConfig;

	@Scheduled(cron = "${batch.cron.tweet}")
	public void tweet() {
		System.out.println("start tweetBatch");

		// initialize instance.
		TwitterApi apiInstance = authVia20AuthCode.init();
		if (apiInstance == null) {
			return;
		}
		System.out.println("initialized apiInstance");

		// get poke list
		List<PokedexDto> pokeAllNode = MapperUtils.readJson(resourcePathConfig.getPokedex(),
				new TypeReference<List<PokedexDto>>() {
				});

		// get today's poke
		PokedexDto todayPoke = PokeUtils.getTodaysPoke(pokeAllNode);
		String todayPokeName = todayPoke.getName().getJapanese();

		// swap name
		String swappedName = PokeUtils.swapName(todayPokeName);

		// make tweet str
		String tweetStr = swappedName;
		// 入れ替わってなければクリア
		if (swappedName.equals(todayPokeName)) {
			tweetStr += PokeUtils.getPokeInfo(todayPoke);
		}

		// ツイート処理
		System.out.println("start tweet.");

		boolean authFlg = Tweets.tweet(apiInstance, tweetStr);
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
		authFlg = Tweets.tweet(apiInstance, swappedName);
		if (authFlg) {
			System.out.println("end tweet.");
		} else {
			System.err.println("cannot tweet...");
		}
		System.out.println("end tweetBatch");
	}
}
