package com.haroot.pokebot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.haroot.pokebot.tools.OAuth20GetAccessToken;
import com.haroot.pokebot.tools.PokedexChecker;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tools")
@RequiredArgsConstructor
public class ToolController {

    private final PokedexChecker pokedexChecker;
    private final OAuth20GetAccessToken oAuth20GetAccessToken;

    @GetMapping("/pokedex/check")
    public boolean pokedexCheck() {
        return pokedexChecker.check();
    }

    /**
     * 2026/6/1現在、Developer ConsoleのAgentから取得できるため未テスト
     */
    @GetMapping("/token/auth-url")
    public String getAuthorizationUrl() {
        return oAuth20GetAccessToken.getAuthorizationUrl();
    }

    /**
     * 2026/6/1現在、Developer ConsoleのAgentから取得できるため未テスト
     */
    @PostMapping("/token")
    public String exchangeToken(@RequestParam String code) {
        boolean success = oAuth20GetAccessToken.updateToken(code);
        return success ? "token.json updated successfully." : "Failed to update token.json.";
    }
}
