package com.haroot.pokebot.utils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MapperUtils {
	public static <T> T readJson(String jsonPath, Class<T> type) {
		// 引数の型のインスタンス生成
		T instance;
		try {
			instance = new ObjectMapper().readValue(Paths.get(jsonPath).toFile(), type);
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return instance;
	}

	public static <T> List<T> readJson(String jsonPath, TypeReference<List<T>> typeReference) {
		// 引数の型のインスタンス生成
		List<T> instanceList;
		try {
			instanceList = new ObjectMapper().readValue(Paths.get(jsonPath).toFile(), typeReference);
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return instanceList;
	}

	public static boolean writeJson(String dest, Object obj) {
		boolean writeFlg = true;
		try {
			new ObjectMapper().writeValue(Paths.get(dest).toFile(), obj);
		} catch (IOException ex) {
			ex.printStackTrace();
			writeFlg = false;
		}
		return writeFlg;
	}
}
