package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.RankingDto;
import org.example.entity.Fighter;
import org.example.entity.RatingEntry;
import org.example.entity.WeightClass;
import org.example.exceptions.InputDataErrorException;
import org.example.repository.FighterRepository;
import org.example.repository.RatingEntryRepository;
import org.example.repository.WeightClassRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RankingService {

    private final FighterRepository fighterRepository;
    private final WeightClassRepository weightClassRepository;
    private final FighterService fighterService;
    private final FightUtil fightUtil;
    private final RatingEntryRepository ratingEntryRepository;

    public List<String> getAllWeightClassNames() {
        return weightClassRepository.findAll().stream().map(WeightClass::getName).collect(Collectors.toList());
    }

    public List<RankingDto> getRankingsForCategory(String categoryName) {
        Set<Integer> categoryNumbers = switch (categoryName.toLowerCase()) {
            case "men's p4p" -> Set.of(1, 2, 3, 4, 5, 6, 7, 8);
            case "women's p4p" -> Set.of(9, 10, 11);
            case "all" -> Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
            default -> {
                WeightClass category = weightClassRepository.findByNameIgnoreCase(categoryName)
                        .orElseThrow(() -> new InputDataErrorException("Weight class not found: " + categoryName));
                yield Set.of(category.getNumber());
            }
        };
        int sequenceInTotal = fightUtil.getMaxSequenceForDate(null);
        Set<RatingEntry> allEntries = new HashSet<>(ratingEntryRepository.findAll());
        List<Fighter> allFighters = fighterRepository.findAll();
        List<Fighter> filtered = allFighters.stream()
                .filter(f -> f.isActive()) // только активные
                .filter(f -> {
                    WeightClass wc = fightUtil.getWeightClass(f, sequenceInTotal, allEntries);
                    return wc != null && categoryNumbers.contains(wc.getNumber());
                })
                .sorted(Comparator.comparingDouble(Fighter::getEloRating).reversed())
                .toList();
        AtomicInteger counter = new AtomicInteger(0);
        return filtered.stream()
                .map(f -> RankingDto.builder()
                        .name(f.getName())
                        .score(Math.round(f.getEloRating() * 100.0) / 100.0) // округление
                        .rank(counter.getAndIncrement())
                        .build())
                .toList();
    }


    private RankingDto getRankingForFighter(Fighter fighter) {
        return RankingDto.builder()
                .name(fighter.getName())
                .score(fighter.getEloRating())
                .build();
    }
}
