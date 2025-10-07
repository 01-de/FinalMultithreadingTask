package org.de;

import java.util.List;
import java.util.concurrent.Callable;

public class CalculateSumTask implements Callable<Integer> {
        String taskName;    // task1
        List<Integer> numbers; // { 1, 2, 3, 4, 5, 6, 7, 8 }

        // Initializing object with task name and list of numbers
        public CalculateSumTask(String taskName, List<Integer> numbers) {
            this.taskName = taskName;
            this.numbers = numbers;
        }

        // Implementing Callable for multithread execution
        @Override
        public Integer call() throws Exception {
            System.out.println("Task name: " + taskName + " " + Thread.currentThread().getName());
            int sum = 0;
            for (Integer number : numbers) {
                sum = sum + number;
            }
            return sum;
        }
    }
