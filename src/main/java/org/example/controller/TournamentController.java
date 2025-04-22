package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.FightDto;
import org.example.dto.StringResponse;
import org.example.dto.TournamentDto;
import org.example.service.TournamentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
public class TournamentController {

    private final TournamentService tournamentService;

    @GetMapping
    public List<TournamentDto> getAllTournaments() {
        return tournamentService.getAllTournaments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TournamentDto> getTournamentById(@PathVariable Long id) {
        return ResponseEntity.ok(tournamentService.getTournamentById(id));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PostMapping
    public ResponseEntity<?> createTournament(@RequestBody TournamentDto dto) {
        String result = tournamentService.createTournament(dto);
        return ResponseEntity.ok(new StringResponse(result));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTournament(@PathVariable Long id, @RequestBody TournamentDto dto) {
        String result = tournamentService.updateTournament(id, dto);
        return ResponseEntity.ok(new StringResponse(result));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PostMapping("/{id}/fights")
    public ResponseEntity<?> addFight(@PathVariable Long id, @RequestBody FightDto fightDto) {
        FightDto result = tournamentService.createFight(id, fightDto);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PutMapping("/{tournamentId}/fights/{fightId}")
    public ResponseEntity<?> updateFight(@PathVariable Long tournamentId, @PathVariable Long fightId,
                                                     @RequestBody FightDto fightDto) {
        FightDto result = tournamentService.updateFight(tournamentId, fightId, fightDto);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @DeleteMapping("/{tournamentId}/fights/{fightId}")
    public ResponseEntity<?> deleteFight(@PathVariable Long tournamentId, @PathVariable Long fightId) {
        String result = tournamentService.deleteFight(tournamentId, fightId);
        return ResponseEntity.ok(new StringResponse(result));
    }

    @GetMapping("/recent")
    public List<TournamentDto> getRecentTournaments() {
        LocalDate today = LocalDate.now();
        return tournamentService.findRecentUpTo(today);
    }
}
