package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.CountryDto;
import org.example.entity.Country;
import org.example.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;

    public List<CountryDto> findAll() {
        return countryRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    private CountryDto toDto(Country country) {
        return new CountryDto(country.getName(), country.getCode());
    }
}
