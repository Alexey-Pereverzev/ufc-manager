package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.FightDto;
import org.example.dto.StringResponse;
import org.example.dto.TournamentDto;
import org.example.exceptions.InputDataErrorException;
import org.example.service.TournamentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
public class TournamentController {

    private final TournamentService tournamentService;

    @GetMapping
    public List<TournamentDto> getAllTournaments() {
        return tournamentService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TournamentDto> getTournamentById(@PathVariable Long id) {
        return ResponseEntity.ok(tournamentService.getTournamentById(id));
    }

    @PostMapping
    public ResponseEntity<?> createTournament(@RequestBody TournamentDto dto) throws InputDataErrorException {
        String result = tournamentService.createTournament(dto);
        return ResponseEntity.ok(new StringResponse(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTournament(@PathVariable Long id, @RequestBody TournamentDto dto) {
        String result = tournamentService.updateTournament(id, dto);
        return ResponseEntity.ok(new StringResponse(result));
    }

    @PostMapping("/{id}/fights")
    public ResponseEntity<?> addFight(@PathVariable Long id, @RequestBody FightDto fightDto) {
        String result = tournamentService.createFight(id, fightDto);
        return ResponseEntity.ok(new StringResponse(result));
    }

    @PutMapping("/{tournamentId}/fights/{fightId}")
    public ResponseEntity<?> updateFight(@PathVariable Long tournamentId, @PathVariable Long fightId,
                                                     @RequestBody FightDto fightDto) {
        String result = tournamentService.updateFight(tournamentId, fightId, fightDto);
        return ResponseEntity.ok(new StringResponse(result));
    }

    @DeleteMapping("/{tournamentId}/fights/{fightId}")
    public ResponseEntity<?> deleteFight(@PathVariable Long tournamentId, @PathVariable Long fightId) {
        String result = tournamentService.deleteFight(tournamentId, fightId);
        return ResponseEntity.ok(new StringResponse(result));
    }
}
