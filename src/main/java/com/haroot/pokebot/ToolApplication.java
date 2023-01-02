package com.haroot.pokebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.haroot.pokebot.tools.PokedexChecker;

/**
 * PokeBotのライフサイクル外のツール実行クラス
 *
 * @author sekiharuhito
 *
 */
@SpringBootApplication
public class ToolApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext cac = SpringApplication.run(ToolApplication.class, args);
		// getBean内のクラスは適宜実行したいクラスに置き換えて使用
		cac.getBean(PokedexChecker.class).check();
		return;
	}
}
