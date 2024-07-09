package edu.cda.rmagQuizzSpringMVCThymeleaf.model;

public class Timer {
    private long startTime;
    private boolean running;

    public Timer() {
        startTime = 0;
        running = false;
    }

    public void start() {
        startTime = System.currentTimeMillis();
        running = true;
    }

    public void stop() {
        running = false;
    }

    public long getTimeElapsed() {
        if (!running) {
            return 0;
        }
        return (System.currentTimeMillis() - startTime) / 1000;
    }
}
