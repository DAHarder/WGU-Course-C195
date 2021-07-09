package utilities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * Time utility.  Used to populate the time combo boxes used in the Appointments Add and Modify scenes.
 */
public class TimeUtil {
    //Formater to format the time object into something readable
    public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");

    /**
     * returns an observable list of time objects to populate time combo boxes.
     * @return an observable list of time objects.
     */
    public static ObservableList<String> populateTime() {
        ObservableList<String> timeList = FXCollections.observableArrayList();
        LocalTime start = LocalTime.of(0,0);
        LocalTime end = LocalTime.of(23,30);

        while(start.isBefore(end.plusSeconds(1))) {
                timeList.add(timeFormatter.format(start));
                start = start.plusMinutes(15);
        }
        timeList.add(timeFormatter.format(LocalTime.of(23,45)));
        return timeList;
    }

    public static LocalDateTime localTimeToEST (LocalDateTime time) {
        ZonedDateTime zdt = ZonedDateTime.of(time.toLocalDate(), time.toLocalTime(), TimeZone.getDefault().toZoneId());
        ZoneId ETZone = ZoneId.of("America/New_York");
        Instant GMT = zdt.toInstant();
        ZonedDateTime localTimeToEST = GMT.atZone(ETZone);
        LocalDateTime timeReturn = localTimeToEST.toLocalDateTime();

        return timeReturn;



    }

}
