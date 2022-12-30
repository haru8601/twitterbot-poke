package com.haroot.pokebot.batch;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.haroot.pokebot.config.ResourcePathConfig;
import com.haroot.pokebot.dto.PokedexDto;
import com.haroot.pokebot.twitterapi.auth.AuthVia20AuthCode;
import com.haroot.pokebot.twitterapi.auth.TokenUtils;
import com.haroot.pokebot.twitterapi.utils.Tweets;
import com.haroot.pokebot.utils.MapperUtils;
import com.haroot.pokebot.utils.PokeUtils;
import com.twitter.clientlib.api.TwitterApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TweetBatch {
	private final AuthVia20AuthCode authVia20AuthCode;
	private final ResourcePathConfig resourcePathConfig;

	@Scheduled(cron = "${batch.cron.tweet}")
	public void tweet() {
		log.info("start tweetBatch");

		// initialize instance.
		TwitterApi apiInstance = authVia20AuthCode.init();
		if (apiInstance == null) {
			return;
		}
		log.info("initialized apiInstance");

		// get poke list
		List<PokedexDto> pokeAllNode = MapperUtils.readJsonAsList(resourcePathConfig.getPokedex(),
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
		log.info("start tweet.");

		boolean authFlg = Tweets.tweet(apiInstance, tweetStr);
		// 成功したら終了
		if (authFlg) {
			log.info("end tweet.");
			log.info("used access token.");
			return;
		}

		// トークン更新
		if (!TokenUtils.refreshToken(apiInstance)) {
			return;
		}

		// 再実行
		log.info("restart tweet.");
		authFlg = Tweets.tweet(apiInstance, swappedName);
		if (authFlg) {
			log.info("end tweet.");
		} else {
			log.error("cannot tweet...");
		}
		log.info("end tweetBatch");
	}
}
