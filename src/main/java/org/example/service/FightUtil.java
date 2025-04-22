package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.Fight;
import org.example.entity.Fighter;
import org.example.entity.RatingEntry;
import org.example.entity.WeightClass;
import org.example.repository.RatingEntryRepository;
import org.example.repository.WeightClassRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;


@Service
@RequiredArgsConstructor
public class FightUtil {

    private final RatingEntryRepository ratingEntryRepository;
    private final WeightClassRepository weightClassRepository;


    public int getIntDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return (calendar.get(Calendar.YEAR) * 365) + (calendar.get(Calendar.MONTH) * 12)
                + calendar.get(Calendar.DATE);
    }

    public String createUniqueDescription(String firstFighterName, String secondFighterName, LocalDate localDate) {
        int intDate = getIntDate(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        if (firstFighterName.compareTo(secondFighterName)<=0)
            return (firstFighterName + " - " + secondFighterName + " - " + intDate);
        else return secondFighterName + " - " + firstFighterName + " - " + intDate;
    }

    public double getRanking(Fighter fighter, int sequenceInTotal, Set<RatingEntry> allRatingEntries) {
        return findAllByFighter(fighter, allRatingEntries).stream()
                .filter(entry -> entry.getFight() != null && entry.getFight().getSequenceInTotal() < sequenceInTotal)
                .max(Comparator.comparingInt(entry -> entry.getFight().getSequenceInTotal()))
                .map(RatingEntry::getRanking)
                .orElse(1000.0);
    }

    private List<RatingEntry> findAllByFighter(Fighter fighter, Set<RatingEntry> allRatingEntries) {
        String targetName = fighter.getName();
        return allRatingEntries.stream()
                .filter(entry -> {
                    Fighter f = entry.getFighter();
                    return f != null && targetName.equals(f.getName());
                })
                .toList();
    }

    public int getMaxSequenceForDate(Date date) {
        LocalDate targetDate = date==null ? LocalDate.now() : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return ratingEntryRepository.findAll().stream()
                .map(RatingEntry::getFight)
                .filter(Objects::nonNull)
                .filter(fight -> fight.getDate().isBefore(targetDate) || fight.getDate().isEqual(targetDate))
                .mapToInt(Fight::getSequenceInTotal)
                .max()
                .orElse(0);  // Если нет боёв до указанной даты — вернём 0
    }

    public WeightClass getWeightClass(Fighter fighter, int sequenceInTotal, Set<RatingEntry> allRatingEntries) {
        return findAllByFighter(fighter, allRatingEntries).stream()
                .filter(entry -> entry.getFight() != null
                        && entry.getFight().getSequenceInTotal() < sequenceInTotal
                        && entry.getWeightClass() != null
                        && entry.getWeightClass().getNumber() != 0) // exclude NA
                .max(Comparator.comparingInt(entry -> entry.getFight().getSequenceInTotal()))
                .map(RatingEntry::getWeightClass)
                .orElse(weightClassRepository.findByNumber(0).orElse(null)); // fallback to NA
    }


}
