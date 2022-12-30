package com.haroot.pokebot.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.haroot.pokebot.dto.PokedexDto;
import com.haroot.pokebot.dto.PokedexDto.Base;

import lombok.extern.slf4j.Slf4j;

/**
 * ポケモン周りのutilクラス
 * 
 * @author sekiharuhito
 *
 */
@Slf4j
public class PokeUtils {
	/**
	 * 本日のポケモン取得
	 * 
	 * @param pokeAllNode ポケモン一覧
	 * @return 今日のポケモン
	 */
	public static PokedexDto getTodaysPoke(List<PokedexDto> pokeAllNode) {
		log.info("start getting today's poke.");
		// 全ポケモン数
		int pokeSumCount = pokeAllNode.size();

		// 今日のポケモン
		PokedexDto todayPoke = pokeAllNode.get(new Random().nextInt(pokeSumCount));
		log.info("today's pokemon: " + todayPoke.getName().getJapanese());
		log.info("end getting today's poke.");
		return todayPoke;
	}

	/**
	 * 文字列のアナグラム生成関数
	 * 
	 * @param oldStr 元の文字列
	 * @return アナグラム
	 */
	public static String swapName(String oldStr) {
		log.info("start swapping name");
		int strLen = oldStr.length();

		// ポケモンの文字列をcharリストにする
		List<Character> nameList = new ArrayList<Character>();
		for (int i = 0; i < strLen; i++) {
			nameList.add(oldStr.charAt(i));
		}
		StringBuilder newName = new StringBuilder();
		Random random = new Random();

		// ポケモン名のcharリストからランダムに一つずつ取り出す
		for (int i = 0; i < strLen; i++) {
			int randomNum = random.nextInt(nameList.size());
			String s1 = Character.toString(nameList.get(randomNum));

			List<String> notFirstList = Arrays.asList("ー", "ッ", "ァ", "ィ", "ゥ", "ェ", "ォ", "ャ", "ュ", "ョ", "ン", "２", "Ｚ",
					"♀", "♂");

			// 先頭にきてほしく無いやつが来たらやり直し
			if (i == 0) {
				if (notFirstList.contains(s1)) {
					i--;
					continue;
				}
			}

			// ランダムに選んだものをstringbuilderに入れてリストから削除
			newName.append(s1);
			nameList.remove(randomNum);
		}
		String swappedName = newName.toString();
		log.info("new name: " + swappedName);
		log.info("end swapping name");
		return swappedName;
	}

	/**
	 * ポケモンの詳細テキスト取得
	 * 
	 * @param targetPoke 対象ポケモン
	 * @return 詳細テキスト
	 */
	public static String getPokeInfo(PokedexDto targetPoke) {
		final String BR = System.getProperty("line.separator");

		Base base = targetPoke.getBase();
		int no = targetPoke.getId();
		int h = base.getH();
		int a = base.getA();
		int b = base.getB();
		int c = base.getC();
		int d = base.getD();
		int s = base.getS();
		int sum = h + a + b + c + d + s;
		String res = BR + BR + "【No." + no + "】" + BR + "HP: " + h + BR + "攻撃: " + a + BR + "防御: " + b + BR + "特攻: " + c
				+ BR + "特防: " + d + BR + "素早: " + s + BR + "合計: " + sum;
		return res;
	}

	/**
	 * 文章内からポケモンが含まれてるか検索する
	 * 
	 * @param pokeAllNode  ポケモン一覧
	 * @param receivedText 受け取ったテキスト
	 * @return 対象ポケモン(いない場合はnull)
	 */
	public static PokedexDto findPoke(List<PokedexDto> pokeAllNode, String receivedText) {
		// get pokemon in tweet
		String formatReceivedTweet = receivedText.replaceAll("2", "２").replaceAll("Z", "Ｚ");

		PokedexDto targetPoke = null;
		int nameLen = 0;
		// O(1000)
		for (PokedexDto pokemon : pokeAllNode) {
			String pokeName = pokemon.getName().getJapanese();
			if (formatReceivedTweet.contains(pokeName)) {
				log.info(pokeName);
				// 「ゾロア」と「ゾロアーク」両方一致する場合、名前が長い方(ゾロアーク)を採用
				if (pokeName.length() > nameLen) {
					targetPoke = pokemon;
					nameLen = pokeName.length();
				}
			}
		}
		return targetPoke;
	}
}
