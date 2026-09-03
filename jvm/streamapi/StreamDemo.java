package streamapi;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StreamDemo {
    public static void main(String[] args) {
        List<Integer> numList = Arrays.asList(1, 2, 3, 4, 5, 6, 6, 7);

        // 1. Filter, remove duplicates, and sort
        List<Integer> filterList = numList.stream()
                .filter(num -> num % 2 == 0) // Keep even numbers
                .distinct() // Remove duplicates
                .sorted((a, b) -> b - a) // Sort in descending order
                .collect(Collectors.toList());
        System.out.println("Filtered, distinct, and sorted: " + filterList); // [6, 4, 2]

        // 2. Map and sum
        int sum = numList.stream()
                .map(num -> num * 2) // Double each number
                .reduce(0, Integer::sum); // Calculate the sum
        System.out.println("Mapped sum: " + sum); // (2 + 4 + 6 + 8 + 10 + 12 + 12 + 14) = 68

        // 3. Group
        Map<Boolean, List<Integer>> groupMap = numList.stream()
                .collect(Collectors.groupingBy(num -> num > 3)); // Group by whether each number is greater than 3
        System.out.println("Grouped result: " + groupMap); // {false=[1, 2, 3], true=[4, 5, 6, 6, 7]}
    }
}
