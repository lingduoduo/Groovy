package lambda;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class LambdaDemo {
    public static void main(String[] args) {
        // 1. Consumer: Print a value
        Consumer<String> printConsumer =
                value -> System.out.println("Consumed: " + value);
        printConsumer.accept("Java Lambda");

        // 2. Supplier: Generate a random number
        Supplier<Integer> randomSupplier =
                () -> (int) (Math.random() * 100);
        System.out.println("Generated random number: " + randomSupplier.get());

        // 3. Function: Convert a string to its length
        Function<String, Integer> lengthFunction = String::length;
        System.out.println("String length: "
                + lengthFunction.apply("Hello Java 8"));

        // 4. Predicate: Determine whether a number is greater than 10
        Predicate<Integer> greaterThanTen = number -> number > 10;
        System.out.println("Is 15 greater than 10? "
                + greaterThanTen.test(15));
    }
}