package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.RankingDto;
import org.example.service.RankingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rankings")
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;


    @GetMapping
    public List<RankingDto> getRankings(@RequestParam("category") String category) {
        return rankingService.getRankingsForCategory(category);
    }
}
