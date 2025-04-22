package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.CountryDto;
import org.example.dto.FighterDto;
import org.example.entity.Fight;
import org.example.entity.Fighter;
import org.example.entity.RatingEntry;
import org.example.exceptions.ResourceNotFoundException;
import org.example.repository.CountryRepository;
import org.example.repository.FighterRepository;
import org.example.repository.WeightClassRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FighterService {

    private final FighterRepository fighterRepository;
    private final CountryRepository countryRepository;
    private final WeightClassRepository weightClassRepository;


    public String create(FighterDto dto) {
        Fighter fighter = toEntity(dto);
        if (dto.getWeightClass() != null) {
            weightClassRepository.findByNameIgnoreCase(dto.getWeightClass())
                    .ifPresent(weightClass -> {
                        fighter.setMinWeightClassNumber(weightClass.getNumber());
                        fighter.setMaxWeightClassNumber(weightClass.getNumber());
                    });
        }
        fighter.setEloRating(1000);
        fighter.setRatingEntries(new ArrayList<>());
        FighterDto saved = toDto(fighterRepository.save(fighter));
        return "Fighter " + saved.getName() + " created successfully";
    }

    public String update(Long id, FighterDto dto) {
        Fighter fighter = fighterRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Fighter with id=" + id + " not found"));

        fighter.setName(dto.getName());
        fighter.setUfcRecord(dto.getUfcRecord());
        fighter.setMmaRecord(dto.getMmaRecord());
        fighter.setActive("Active".equalsIgnoreCase(dto.getStatus()));
        if (dto.getCountry() != null && dto.getCountry().getCode() != null) {
            countryRepository.findByCodeIgnoreCase(dto.getCountry().getCode())
                    .ifPresent(fighter::setCountry);
        }
        if (dto.getWeightClass() != null) {
            weightClassRepository.findByNameIgnoreCase(dto.getWeightClass())
                    .ifPresent(weightClass -> {
                        int newNumber = weightClass.getNumber();
                        if (fighter.getMinWeightClassNumber() == 0 || newNumber < fighter.getMinWeightClassNumber()) {
                            fighter.setMinWeightClassNumber(newNumber);
                        }
                        if (fighter.getMaxWeightClassNumber() == 0 || newNumber > fighter.getMaxWeightClassNumber()) {
                            fighter.setMaxWeightClassNumber(newNumber);
                        }
                    });
        }
        FighterDto saved = toDto(fighterRepository.save(fighter));
        return "Fighter " + saved.getName() + " updated successfully";
    }

    public String delete(Long id) {
        Fighter fighter = fighterRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Fighter with id=" + id + " not found"));
        String result = "Fighter " + fighter.getName() + " deleted successfully";
        fighterRepository.deleteById(id);
        return result;
    }

    public FighterDto toDto(Fighter fighter) {
        String weightClassName = "NA";
        List<RatingEntry> entries = fighter.getRatingEntries();
        if (entries != null && !entries.isEmpty()) {
            Optional<RatingEntry> latestWithWeightClass = entries.stream()
                    .filter(e -> e.getWeightClass() != null && !"NA".equalsIgnoreCase(e.getWeightClass().getName()))
                    .max(Comparator.comparing(r -> {
                        Fight fight = r.getFight();
                        return fight != null ? fight.getSequenceInTotal() : 0;
                    }));
            if (latestWithWeightClass.isPresent()) {
                weightClassName = latestWithWeightClass.get().getWeightClass().getName();
            }
        }
        CountryDto countryDto = null;
        if (fighter.getCountry() != null) {
            countryDto = CountryDto.builder()
                    .name(fighter.getCountry().getName())
                    .code(fighter.getCountry().getCode())
                    .build();
        }
        return FighterDto.builder()
                .name(fighter.getName())
                .country(countryDto)
                .weightClass(weightClassName)
                .ufcRecord(fighter.getUfcRecord())
                .mmaRecord(fighter.getMmaRecord())
                .status(fighter.isActive() ? "Active" : "Released")
                .build();
    }

    public Fighter toEntity(FighterDto dto) {
        Fighter fighter = new Fighter();
        fighter.setName(dto.getName());
        fighter.setUfcRecord(dto.getUfcRecord());
        fighter.setMmaRecord(dto.getMmaRecord());
        fighter.setActive("Active".equalsIgnoreCase(dto.getStatus()));
        if (dto.getCountry() != null && dto.getCountry().getCode() != null) {
            countryRepository.findByCodeIgnoreCase(dto.getCountry().getCode())
                    .ifPresent(fighter::setCountry);
        }
        return fighter;
    }

    public List<FighterDto> getAll() {
        return fighterRepository.findAll().stream().map(this::toDto).toList();
    }

}
