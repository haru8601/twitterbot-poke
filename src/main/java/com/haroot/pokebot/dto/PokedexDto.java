package com.haroot.pokebot.dto;

import java.util.List;

import lombok.Data;

@Data
public class PokedexDto {
	private int id;
	private Name name;
	private List<String> type;
	private Base base;

	@Data
	public static class Name {
		private String english;
		private String japanese;
		private String chinese;
		private String french;
	}

	@Data
	public static class Base {
		private int h;
		private int a;
		private int b;
		private int c;
		private int d;
		private int s;
	}
}
