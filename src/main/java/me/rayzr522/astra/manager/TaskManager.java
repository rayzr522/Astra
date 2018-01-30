package me.rayzr522.astra.manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public enum TaskManager {
    INSTANCE;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    public void execute(Runnable runnable) {
        executorService.execute(runnable);
    }
}
