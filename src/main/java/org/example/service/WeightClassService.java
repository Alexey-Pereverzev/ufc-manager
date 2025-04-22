package org.example.service;
import lombok.RequiredArgsConstructor;
import org.example.entity.WeightClass;
import org.example.repository.WeightClassRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WeightClassService {

    private final WeightClassRepository weightClassRepository;

    public List<String> getAll() {
        return weightClassRepository.findAll().stream()
                .map(WeightClass::getName)
                .filter(name -> !name.equalsIgnoreCase("NA"))
                .toList();
    }
}
