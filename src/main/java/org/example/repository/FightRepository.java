package org.example.repository;

import org.example.entity.Fight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FightRepository extends JpaRepository<Fight, Long> {

    Optional<Fight> findByUniqueDescription(String unique);

    @Query("SELECT COALESCE(MAX(f.sequenceInTotal), 0) FROM Fight f")
    int findGlobalMaxSequenceInTotal();
}
