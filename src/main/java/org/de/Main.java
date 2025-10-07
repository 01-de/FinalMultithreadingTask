package org.de;


import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        DataProcessor processor = new DataProcessor(10);

        for (int i = 1; i <= 100; i++) {
            List<Integer> numbers = Arrays.asList(i, i + 1, i + 2);
            processor.calculateSumTask(numbers);
        }

        while (processor.getActiveTasksCount() > 0) {
            System.out.println("Active tasks: " + processor.getActiveTasksCount());
            Thread.sleep(500);
        }

        for (int i = 1; i <= 100; i++) {
            String name = new StringBuilder("task").append(i).toString();
            System.out.println("--Result--");
            System.out.println(name + " result: " + processor.getResult(name));
        }

        processor.shutDown();
    }
}