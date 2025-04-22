package org.example.repository;

import org.example.entity.Fighter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("fighterRepository")
public interface FighterRepository extends JpaRepository<Fighter, Long> {

    Optional<Fighter> findByName(String nameFirst);

//    List<Fighter> findByWeightClassNumber(int number);
//
//    List<Fighter> findByWeightClassNumberIn(List<Integer> integers);

}
