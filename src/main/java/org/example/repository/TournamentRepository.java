package org.example.repository;

import org.example.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {

    @Query("SELECT MAX(f.sequenceInTotal) FROM Fight f WHERE f.tournament.id = :tournamentId")
    Integer findMaxSequenceInTotalByTournamentId(Long tournamentId);

    List<Tournament> findByDateBeforeOrderByDateDesc(LocalDate date);
}
