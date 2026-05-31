package com.haroot.pokebot.service;

import org.springframework.stereotype.Component;

import com.haroot.pokebot.twitterapi.auth.AuthVia20AuthCode;
import com.haroot.pokebot.twitterapi.auth.TokenUtils;
import com.haroot.pokebot.twitterapi.utils.Tweets;
import com.twitter.clientlib.api.TwitterApi;
import com.twitter.clientlib.model.Get2TweetsSearchRecentResponse;
import com.twitter.clientlib.model.Tweet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SearchService {
  private final AuthVia20AuthCode authVia20AuthCode;

  public void search() {
    log.info("start searchBatch");

    // initialize instance.
    TwitterApi apiInstance = authVia20AuthCode.init();
    if (apiInstance == null) {
      log.error("token is null.");
      return;
    }
    log.info("initialized apiInstance");

    // search tweet(I'd like to "like")
    // 基本は#ポケモンで検索、あとは変なのを除外
    String query = "ポケモン -#ポケモン交換 -#レイド -#副業 "
        + "-営業 -いいね -RT -トレーナーコード -購入 -配信 -出品 -bot -発売 -急募 -募集 -無料 -定期 -自動 -大会 -拡散 -交換 -求 -出 -配布 -DM "
        + "-is:retweet -is:reply -has:links -has:mentions";
    Get2TweetsSearchRecentResponse searchRes = Tweets.searchTweet(apiInstance, query);
    if (searchRes == null) {
      log.error("there is no tweet...");
      if (!TokenUtils.refreshToken(apiInstance)) {
        // トークンをrefreshできなければ終了
        return;
      }
      log.info("start searching tweet again");
      searchRes = Tweets.searchTweet(apiInstance, query);
    }

    for (Tweet tweet : searchRes.getData()) {
      log.info(tweet.getText());
    }
    log.info("end searchBatch");
  }

}
