package com.haroot.pokebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ツール実行用サーバー起動クラス
 *
 * @author haroot
 *
 */
@SpringBootApplication
public class ToolApplication {
  public static void main(String[] args) {
    SpringApplication.run(ToolApplication.class, args);
  }
}
