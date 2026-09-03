package localdatetime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class DateTimeDemo {
    public static void main(String[] args) {
        // 1. Get the current date and time
        LocalDateTime now = LocalDateTime.now();
        System.out.println("Current date and time: " + now); // 2025-12-21T15:30:20.123

        // 2. Format the date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);
        System.out.println("Formatted date and time: " + formattedDateTime); // 2025-12-21 15:30:20
    }
}
