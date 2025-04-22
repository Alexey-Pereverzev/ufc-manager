package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.CountryDto;
import org.example.service.CountryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    @GetMapping
    public List<CountryDto> getAllCountries() {
        return countryService.findAll();
    }
}
