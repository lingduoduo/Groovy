import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

public final class Lambda {
    private Lambda() {
    }

    record Job(String roleName, int salary) {
    }

    record Person(String name, int age, Job job) {
        void greet() {
            System.out.printf("Hello, I'm %s%n", name);
        }
    }

    record FoodOrder(String name, BigDecimal cost) {
        FoodOrder(String name, String cost) {
            this(name, new BigDecimal(cost));
        }
    }

    public static void main(String[] args) {
        var employee = new Person("Ling", 30, new Job("MLE", 1_000));
        employee.greet();
        System.out.printf("Role: %s; salary: %d%n",
                employee.job().roleName(), employee.job().salary());

        var colors = List.of("black", "white", "blue", "red");
        var sortedColors = colors.stream()
                .sorted(Comparator.reverseOrder())
                .toList();
        System.out.println("Sorted colors: " + sortedColors);

        var orders = List.of(
                new FoodOrder("Burger", "3.99"),
                new FoodOrder("Fries", "1.99"),
                new FoodOrder("Milkshake", "2.75"));
        var affordableOrders = orders.stream()
                .filter(order -> order.cost().compareTo(new BigDecimal("2.00")) >= 0)
                .sorted(Comparator.comparing(FoodOrder::cost))
                .map(FoodOrder::name)
                .toList();
        System.out.println("Affordable orders: " + affordableOrders);

        var numbers = List.of(1, 2, 3, 4, 10);
        var oddNumbers = numbers.stream()
                .filter(number -> number % 2 != 0)
                .toList();
        int sum = numbers.stream().reduce(0, Integer::sum);
        System.out.println("Odd numbers: " + oddNumbers);
        System.out.println("Sum: " + sum);
    }
}
