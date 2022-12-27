package com.haroot.pokebot.twitterapi.utils;

import java.util.ArrayList;
import java.util.List;

import com.twitter.clientlib.model.Tweet;
import com.twitter.clientlib.model.TweetPublicMetrics;

/**
 * TwitterAPI使用下でのutilクラス
 * 
 * @author sekiharuhito
 *
 */
public class TweetUtils {
	/**
	 * リツイート数やいいね数が1以上のものIDを抽出
	 * 
	 * @param tweetList 検索対象のツイートリスト
	 * @return 抽出後のツイートIDリスト
	 */
	public static List<String> getFamousTweets(List<Tweet> tweetList) {
		List<String> idList = new ArrayList<>();
		// select famous tweet
		for (Tweet tweet : tweetList) {
			TweetPublicMetrics metrics = tweet.getPublicMetrics();
			if (metrics.getRetweetCount() > 0 || metrics.getLikeCount() > 0) {
				idList.add(tweet.getId());
			}
		}
		System.out.println("selected popular tweets.");
		return idList;
	}
}
