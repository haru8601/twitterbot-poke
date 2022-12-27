package com.haroot.pokebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Springboot起動クラス
 * 
 * @author sekiharuhito
 *
 */
@SpringBootApplication
@EnableScheduling
public class PokeBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(PokeBotApplication.class, args);
	}

}
