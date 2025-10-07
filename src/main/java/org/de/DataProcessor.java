package org.de;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class DataProcessor {
    ExecutorService service;
    Map<String, Future<Integer>> taskResults;
    final AtomicInteger taskCounter;
    final AtomicInteger activeTasks;

    public DataProcessor(int threadPoolSize) {
        this.taskCounter = new AtomicInteger(0);
        this.activeTasks = new AtomicInteger(0);
        this.taskResults = new HashMap<>();
        this.service = Executors.newFixedThreadPool(threadPoolSize);
    }

    public String calculateSumTask(List<Integer> numbers) {
        int taskNumber = taskCounter.incrementAndGet();
        String taskName = new StringBuilder("task").append(taskNumber).toString();

        CalculateSumTask task = new CalculateSumTask(taskName, numbers);

        int active = activeTasks.incrementAndGet();

        Future<Integer> future = service.submit(() -> {
            try {
                return task.call();
            } finally {
                activeTasks.decrementAndGet();
            }
        });

        synchronized (taskResults) {
            taskResults.put(taskName, future);
        }
        return taskName;
    }

    public int getActiveTasksCount() {
        return activeTasks.get();
    }

    public Optional<Integer> getResult(String resultName) {
        Future<Integer> integerFuture;
        synchronized (taskResults) {
            integerFuture = taskResults.get(resultName);
        }
        if (integerFuture == null) {
            return Optional.empty();
        }

        if (!integerFuture.isDone()) {
            return Optional.empty();
        }

        try {
            return Optional.of(integerFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return Optional.empty();
        }

    }

    public void shutDown() {
        service.shutdown();
    }
}
