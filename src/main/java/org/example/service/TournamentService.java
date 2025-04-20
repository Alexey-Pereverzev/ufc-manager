package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.FightDto;
import org.example.dto.TournamentDto;
import org.example.entity.*;
import org.example.exceptions.InputDataErrorException;
import org.example.exceptions.ResourceNotFoundException;
import org.example.repository.*;
import org.example.util.FightUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final FightRepository fightRepository;
    private final FighterRepository fighterRepository;
    private final WeightClassRepository weightClassRepository;
    private final RatingEntryRepository ratingEntryRepository;

    public List<TournamentDto> getAllTournaments() {
        return tournamentRepository.findAll().stream().map(this::toDto).toList();
    }

    public TournamentDto getTournamentById(Long id) {
        Tournament tournament = tournamentRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Tournament with id " + id + " not found"));
        return toDto(tournament);
    }


    @Transactional
    public String createTournament(TournamentDto dto) throws InputDataErrorException {
        Tournament tournament = Tournament.builder()
                .name(dto.getName())
                .location(dto.getLocation())
                .date(dto.getDate())
                .build();

        tournament = tournamentRepository.save(tournament);     //  save tournament first

        if (dto.getFights() != null && !dto.getFights().isEmpty()) {
            int nextSequence = tournamentRepository.findMaxSequenceInTotalByTournamentId(tournament.getId());
            List<Fight> fights = new ArrayList<>();

            for (FightDto fightDto : dto.getFights()) {
                Fighter fighter1 = fighterRepository.findByName(fightDto.getFighter1())
                        .orElseThrow(() -> new ResourceNotFoundException("Fighter1 not found: " + fightDto.getFighter1()));
                Fighter fighter2 = fighterRepository.findByName(fightDto.getFighter2())
                        .orElseThrow(() -> new ResourceNotFoundException("Fighter2 not found: " + fightDto.getFighter2()));
                WeightClass weightClass = weightClassRepository.findByName(fightDto.getWeightClass())
                        .orElseThrow(() -> new ResourceNotFoundException("Weight class not found: " + fightDto.getWeightClass()));

                Fight fight = Fight.builder()
                        .tournament(tournament)
                        .fighter1(fighter1)
                        .fighter2(fighter2)
                        .weightClass(weightClass)
                        .result(fightDto.getResult())
                        .date(fightDto.getDate())
                        .sequenceInTotal(++nextSequence)
                        .uniqueDescription(FightUtil.createUniqueDescription(fighter1.getName(), fighter2.getName(), tournament.getDate()))
                        .build();

                fights.add(fight);
            }
            fightRepository.saveAll(fights);
        }
        return "Tournament " + tournament.getName() + " created successfully";
    }


    @Transactional
    public String updateTournament(Long id, TournamentDto dto) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament with id " + id + " not found"));
        tournament.setName(dto.getName());
        tournament.setLocation(dto.getLocation());
        tournament.setDate(dto.getDate());

        Map<Long, Fight> existingFights = tournament.getFights().stream()           // current tournament fights
                .collect(Collectors.toMap(Fight::getId, Function.identity()));

        int globalSequence = fightRepository.findGlobalMaxSequenceInTotal();
        List<Fight> updatedFights = new ArrayList<>();
        for (FightDto fightDto : dto.getFights()) {
            Fight fight;
            boolean isNew = fightDto.getId() == null;

            if (!isNew && existingFights.containsKey(fightDto.getId())) {
                fight = existingFights.get(fightDto.getId());       //  existing fight
            } else {
                fight = new Fight();                                //  new fight
                fight.setTournament(tournament);
                fight.setSequenceInTotal(++globalSequence);
            }
            fight.setDate(fightDto.getDate());
            fight.setResult(fightDto.getResult());
            fight.setFighter1(fighterRepository.findByName(fightDto.getFighter1())
                    .orElseThrow(() -> new ResourceNotFoundException("Fighter1 not found")));
            fight.setFighter2(fighterRepository.findByName(fightDto.getFighter2())
                    .orElseThrow(() -> new ResourceNotFoundException("Fighter2 not found")));
            fight.setWeightClass(weightClassRepository.findByName(fightDto.getWeightClass())
                    .orElseThrow(() -> new ResourceNotFoundException("Weight class not found")));
            fight.setUniqueDescription(FightUtil.createUniqueDescription(
                    fight.getFighter1().getName(), fight.getFighter2().getName(), fight.getDate()));
            updatedFights.add(fight);
        }
        fightRepository.saveAll(updatedFights);
        tournament.setFights(updatedFights);
        Tournament saved = tournamentRepository.save(tournament);
        return "Tournament " + saved.getName() + " updated successfully";
    }



    public String createFight(Long tournamentId, FightDto dto) {
        Fight fight = new Fight();
        FightDto saved = calculateAndSaveFight(tournamentId, dto, fight);
        return "Fight '" + saved.getFighter1() + " vs. " + saved.getFighter2() + "' added successfully";
    }

    public String updateFight(Long tournamentId, Long fightId, FightDto dto) {
        Fight fight = fightRepository.findById(fightId).orElseThrow(() ->
                new ResourceNotFoundException("Fight with id " + fightId + " not found"));
        FightDto saved = calculateAndSaveFight(tournamentId, dto, fight);
        return "Fight '" + saved.getFighter1() + " vs. " + saved.getFighter2() + "' updated successfully";
    }

    @Transactional
    private FightDto calculateAndSaveFight(Long tournamentId, FightDto dto, Fight fight) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow();
        Set<RatingEntry> allRatingEntries = new HashSet<>(ratingEntryRepository.findAll());
        fight.setTournament(tournament);
        fight.setDate(dto.getDate());
        fight.setResult(dto.getResult());
        Integer maxFightNumber = tournamentRepository.findMaxSequenceInTotalByTournamentId(tournamentId);
        String uniqueDescription = FightUtil.createUniqueDescription(dto.getFighter1(), dto.getFighter2(), tournament.getDate());
        fight.setUniqueDescription(uniqueDescription);
        fight.setSequenceInTotal(maxFightNumber + 1);
        Fighter fighter1 = fighterRepository.findByName(dto.getFighter1())
                .orElseThrow(() -> new ResourceNotFoundException("Fighter1 not found"));
        Fighter fighter2 = fighterRepository.findByName(dto.getFighter2())
                .orElseThrow(() -> new ResourceNotFoundException("Fighter2 not found"));
        WeightClass weightClass = weightClassRepository.findByName(dto.getWeightClass())
                .orElse(weightClassRepository.findByName("NA").orElseThrow(() ->
                        new ResourceNotFoundException("Weightclass not found")));
        double firstRank = FightUtil.getRanking(fighter1, maxFightNumber + 1, allRatingEntries);
        double secondRank = FightUtil.getRanking(fighter2, maxFightNumber + 1, allRatingEntries);
        double newFirstRank;
        double newSecondRank;
        if (!dto.getResult().isEmpty() && !dto.getResult().contains("No contest")) {
            if (!dto.getResult().contains("Draw")) {
                fight.setWinner(fighter1);
            }
            double v1 = 1 + Math.pow(10, (secondRank - firstRank) / 400);     // simplified formula
            double v2 = 1 + Math.pow(10, (firstRank - secondRank) / 400);
            newFirstRank = firstRank + 200 * (1 - 1 / v1);
            newSecondRank = secondRank + 200 * (-1 / v2);
            firstRank = newFirstRank;                           //  ELO recalculation if there is a winner
            secondRank = newSecondRank;
        }
        fight.setFighter1(fighter1);
        fight.setFighter2(fighter2);
        Fight saved = fightRepository.save(fight);
        RatingEntry entry1 = RatingEntry.builder()
                .ranking(firstRank)
                .fighter(fighter1)
                .fight(saved)
                .weightClass(weightClass)
                .build();
        RatingEntry entry2 = RatingEntry.builder()
                .ranking(secondRank)
                .fighter(fighter2)
                .fight(saved)
                .weightClass(weightClass)
                .build();
        ratingEntryRepository.save(entry1);
        ratingEntryRepository.save(entry2);
        return toFightDto(saved);
    }



    @Transactional
    public String deleteFight(Long tournamentId, Long fightId) {
        Fight fight = fightRepository.findById(fightId).orElseThrow(() ->
                new ResourceNotFoundException("Fight with id " + fightId + " not found"));
        fightRepository.deleteById(fightId);
        return "Fight '" + fight.getFighter1() + " vs. " + fight.getFighter2() + "' deleted successfully";
    }

    // ================== DTO MAPPING ====================

    public TournamentDto toDto(Tournament entity) {
        List<FightDto> fightDtos = entity.getFights() == null ? List.of() :
                entity.getFights().stream()
                        .sorted(Comparator.comparingInt(Fight::getSequenceInTotal))
                        .map(this::toFightDto)
                        .toList();
        return TournamentDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .location(entity.getLocation())
                .date(entity.getDate())
                .fights(fightDtos)
                .build();
    }

//    public Tournament toEntity(TournamentDto dto) {
//        return Tournament.builder()
//                .name(dto.getName())
//                .location(dto.getLocation())
//                .date(dto.getDate())
//                .build();
//    }

    public FightDto toFightDto(Fight entity) {
        return FightDto.builder()
                .id(entity.getId())
                .fighter1(entity.getFighter1().getName())
                .fighter2(entity.getFighter2().getName())
                .result(entity.getResult())
                .weightClass(entity.getWeightClass().getName())
                .date(entity.getDate())
                .build();
    }

    public List<TournamentDto> getAll() {
        return null;
    }
}

