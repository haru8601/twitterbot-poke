package com.haroot.pokebot.tools;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.haroot.pokebot.config.ResourcePathConfig;
import com.haroot.pokebot.dto.PokedexDto;
import com.haroot.pokebot.utils.MapperUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class PokedexChecker {
	private final ResourcePathConfig resourcePathConfig;

	public void check() {
		log.info("start checking pokedex");
		List<PokedexDto> pokeList = MapperUtils.readJsonAsList(resourcePathConfig.getPokedex(),
				new TypeReference<List<PokedexDto>>() {
				});
		int pokeSize = pokeList.size();
		int tmpCount = 1;
		for (int i = 0; i < pokeSize; i++) {
			PokedexDto poke = pokeList.get(i);
			if (poke.getId() != tmpCount) {
				log.warn("{}:{}", poke.getId(), poke.getName().getJapanese());
				if (i != pokeSize - 1) {
					tmpCount = pokeList.get(i + 1).getId();
				}
			} else {
				tmpCount++;
			}
		}
		log.info("end checking pokedex");
	}
}
