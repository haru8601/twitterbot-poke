package com.haroot.pokebot.twitterapi.utils;

import java.util.HashSet;
import java.util.Set;

import com.twitter.clientlib.ApiException;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.model.Get2TweetsSearchRecentResponse;
import com.twitter.clientlib.model.ResourceUnauthorizedProblem;
import com.twitter.clientlib.model.Tweet;
import com.twitter.clientlib.model.TweetCreateRequest;
import com.twitter.clientlib.model.TweetCreateRequestReply;
import com.twitter.clientlib.model.TweetCreateResponse;
import com.twitter.clientlib.model.UsersLikesCreateRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Tweets {

	/**
	 * 通常ツイート
	 * 
	 * @param apiInstance APIインスタンス
	 * @param text        ツイート文
	 * @return true: ツイート成功, false: ツイート失敗
	 */
	public static boolean tweet(TwitterApi apiInstance, String text) {
		TweetCreateRequest tweetCreateRequest = new TweetCreateRequest();
		tweetCreateRequest.text(text);
		try {
			TweetCreateResponse result = apiInstance.tweets().createTweet(tweetCreateRequest).execute();
			if (result.getErrors() != null && result.getErrors().size() > 0) {
				log.info("Error:");
				result.getErrors().forEach(e -> {
					log.info(e.toString());
					if (e instanceof ResourceUnauthorizedProblem) {
						log.info(((ResourceUnauthorizedProblem) e).getTitle() + " "
								+ ((ResourceUnauthorizedProblem) e).getDetail());
					}
				});

				// 正常系
			} else {
				log.info("createTweet - Tweet Text: " + result.toString());
				return true;
			}
		} catch (ApiException ex) {
			log.error("cannot tweet...");
			log.error(ex.getMessage(), ex);
		}

		return false;
	}

	/**
	 * リプライ
	 * 
	 * @param apiInstance      APIインスタンス
	 * @param text             ツイート文
	 * @param replyTargetTweet リプライ対象
	 * @return true: ツイート成功, false: ツイート失敗
	 */
	public static boolean tweetAsReply(TwitterApi apiInstance, String text, Tweet replyTargetTweet) {
		// create request
		TweetCreateRequest tweetCreateRequest = new TweetCreateRequest();
		tweetCreateRequest.text(text);
		// setting reply info
		TweetCreateRequestReply replyContent = new TweetCreateRequestReply();
		replyContent.inReplyToTweetId(replyTargetTweet.getId());
		tweetCreateRequest.reply(replyContent);

		try {
			// reply
			TweetCreateResponse result = apiInstance.tweets().createTweet(tweetCreateRequest).execute();

			// if there are errors
			if (result.getErrors() != null && result.getErrors().size() > 0) {
				log.info("Error:");
				result.getErrors().forEach(e -> {
					log.info(e.toString());
					if (e instanceof ResourceUnauthorizedProblem) {
						log.info(((ResourceUnauthorizedProblem) e).getTitle() + " "
								+ ((ResourceUnauthorizedProblem) e).getDetail());
					}
				});
				// 正常系
			} else {
				log.info("createTweet - Tweet Text: " + result.toString());
				return true;
			}
		} catch (ApiException ex) {
			log.error("cannot tweet as reply...");
			log.error(ex.getMessage(), ex);
		}
		return false;
	}

	/**
	 * ツイート検索
	 * 
	 * @param apiInstance APIインスタンス
	 * @param query       検索クエリ
	 * @return 検索結果
	 */
	public static Get2TweetsSearchRecentResponse searchTweet(TwitterApi apiInstance, String query) {
		log.info("start searching tweet");
		Get2TweetsSearchRecentResponse response = null;
		try {
			// リクエスト数は10-100
			int maxResults = 100;
			// retweetやlike数把握のためmetrics情報を追加で要求
			Set<String> tweetFields = new HashSet<>();
			tweetFields.add("public_metrics");

			// request
			response = apiInstance.tweets().tweetsRecentSearch(query).maxResults(maxResults).tweetFields(tweetFields)
					.execute();
		} catch (ApiException ex) {
			log.error("cannot get recent tweets...");
			log.error(ex.getMessage(), ex);
		}
		log.info("end searching tweet");
		return response;
	}

	/**
	 * ツイートいいね
	 * 
	 * @param apiInstance APIインスタンス
	 * @param tweetId     対象ツイートID
	 * @param myId        個人ID
	 * @return true: いいね成功, false: いいね失敗
	 */
	public static boolean likeTweet(TwitterApi apiInstance, String tweetId, String myId) {
		boolean passed = true;
		try {
			// setting
			UsersLikesCreateRequest request = new UsersLikesCreateRequest();
			request.tweetId(tweetId);

			// request
			apiInstance.tweets().usersIdLike(myId).usersLikesCreateRequest(request).execute();
		} catch (ApiException ex) {
			log.error("cannot like tweet...");
			log.error("tweet id:" + tweetId);
			log.error(ex.getMessage(), ex);
			passed = false;
		}
		return passed;
	}
}
