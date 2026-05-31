package com.haroot.pokebot.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.haroot.pokebot.service.SearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @PostMapping
    public void search() {
        searchService.search();
    }
}
