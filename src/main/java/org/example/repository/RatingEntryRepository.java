package org.example.repository;

import org.example.entity.RatingEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingEntryRepository extends JpaRepository<RatingEntry, Long> {
    @Query("select fe from RatingEntry fe where fe.fighter.id = :id order by fe.id desc")
    List<RatingEntry> findAllByFighterId(Long id);
}
