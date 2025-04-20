package org.example.repository;

import org.example.entity.WeightClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeightClassRepository extends JpaRepository<WeightClass, Long> {
    Optional<WeightClass> findByNumber(int number);

    Optional<WeightClass> findByName(String weightClass);

    Optional<WeightClass> findByNameIgnoreCase(String weightClass);
}
