package com.haroot.pokebot.batch;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.common.reflect.TypeToken;
import com.haroot.pokebot.twitterapi.auth.AuthVia20AppOnly;
import com.haroot.pokebot.twitterapi.utils.ReplyExecutor;
import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.JSON;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.model.FilteredStreamingTweetResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReplyBatch {
	private final AuthVia20AppOnly authVia20AppOnly;
	private final ReplyExecutor replyExecutor;
	public static final long MINUTE_MILLS = 1000 * 60;
	// 23h59m
	public static final long EXEC_TIME = MINUTE_MILLS * 60 * 24 - MINUTE_MILLS;

	// returnしたら1m後に再実行
	@Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
	public void reply() {
		log.info("start replyBatch");

		// initialize instance
		log.info("started check beader token.");
		TwitterApi apiInstance = authVia20AppOnly.init();
		log.info("finished check beader token.");

		// listen
		log.info("start listening.");
		long start = new Date().getTime();
		try {
			InputStream stream = apiInstance.tweets().searchStream().execute();
			try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
				Type localVarReturnType = new TypeToken<FilteredStreamingTweetResponse>() {
					private static final long serialVersionUID = 1L;
				}.getType();
				String line = br.readLine();

				// 無限ループ
				while (line != null && new Date().getTime() - start < EXEC_TIME) {
					if (line.isEmpty()) {
						line = br.readLine();
						continue;
					}
					log.info("found tweet.");
					// 取得結果をキャスト
					FilteredStreamingTweetResponse res = (FilteredStreamingTweetResponse) JSON.getGson().fromJson(line,
							localVarReturnType);
					// リプライ処理開始
					replyExecutor.exec(res.getData());
					line = br.readLine();
				}
			} catch (Exception ex) {
				log.error(ex.getMessage(), ex);
			}
		} catch (ApiException ex) {
			log.error(ex.getMessage(), ex);
		}
		log.info("end listening.");
		log.info("end replyBatch");
	}
}
