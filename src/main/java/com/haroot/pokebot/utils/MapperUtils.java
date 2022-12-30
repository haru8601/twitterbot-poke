package com.haroot.pokebot.utils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MapperUtils {
	public static <T> T readJson(String jsonPath, Class<T> type) {
		log.info("start reading json");
		// 引数の型のインスタンス生成
		T instance;
		try {
			instance = new ObjectMapper().readValue(Paths.get(jsonPath).toFile(), type);
		} catch (IOException ex) {
			log.error(ex.getMessage(), ex);
			return null;
		}
		log.info("end reading json");
		return instance;
	}

	public static <T> List<T> readJsonAsList(String jsonPath, TypeReference<List<T>> typeReference) {
		log.info("start reading json(typeof list)");
		// 引数の型のインスタンス生成
		List<T> instanceList;
		try {
			instanceList = new ObjectMapper().readValue(Paths.get(jsonPath).toFile(), typeReference);
		} catch (IOException ex) {
			log.error(ex.getMessage(), ex);
			return null;
		}
		log.info("end reading json(typeof list)");
		return instanceList;
	}

	public static boolean writeJson(String dest, Object obj) {
		boolean writeFlg = true;
		try {
			new ObjectMapper().writeValue(Paths.get(dest).toFile(), obj);
		} catch (IOException ex) {
			log.error(ex.getMessage(), ex);
			writeFlg = false;
		}
		return writeFlg;
	}
}
