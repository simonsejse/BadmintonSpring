package com.simonsejse.badmintonworldrecord.utilities;

import java.time.Duration;
import java.time.LocalDateTime;

public class DateUtil {

    public static String calculateDifferenceBetweenDates(LocalDateTime now, LocalDateTime before){
        Duration duration = Duration.between(before, now);
        return String.format("%02dd:%02dt:%02dm:%02ds", duration.toDaysPart(), duration.toHoursPart(), duration.toMinutesPart(), duration.toSecondsPart());
    }
}
