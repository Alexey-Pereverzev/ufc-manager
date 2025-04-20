package org.example.util;

import org.example.entity.Fighter;
import org.example.entity.RatingEntry;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class FightUtil {
    public static int getIntDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return (calendar.get(Calendar.YEAR) * 365) + (calendar.get(Calendar.MONTH) * 12)
                + calendar.get(Calendar.DATE);
    }

    public static String createUniqueDescription(String firstFighterName, String secondFighterName, LocalDate localDate) {
        int intDate = getIntDate(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        if (firstFighterName.compareTo(secondFighterName)<=0)
            return (firstFighterName + " - " + secondFighterName + " - " + intDate);
        else return secondFighterName + " - " + firstFighterName + " - " + intDate;
    }

    public static double getRanking(Fighter fighter, int sequenceInTotal, Set<RatingEntry> allRatingEntries) {
        return findAllByFighter(fighter, allRatingEntries).stream()
                .filter(entry -> entry.getFight() != null && entry.getFight().getSequenceInTotal() < sequenceInTotal)
                .max(Comparator.comparingInt(entry -> entry.getFight().getSequenceInTotal()))
                .map(RatingEntry::getRanking)
                .orElse(1000.0);
    }

    private static List<RatingEntry> findAllByFighter(Fighter fighter, Set<RatingEntry> allRatingEntries) {
        String targetName = fighter.getName();
        return allRatingEntries.stream()
                .filter(entry -> {
                    Fighter f = entry.getFighter();
                    return f != null && targetName.equals(f.getName());
                })
                .toList();
    }


}
