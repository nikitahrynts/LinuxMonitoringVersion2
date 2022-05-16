package by.hryntsaliou.metrics;

import java.util.ArrayList;

public class Scheduler extends Thread {

    private final ArrayList<Collector> tasks;
    private int delayInMS;

    public Scheduler(int delay, ArrayList<Collector> taskList) {
        tasks = taskList;
        delayInMS = delay;
    }

    public void updateDelay(int newDelay) {
        delayInMS = newDelay;
    }

    private synchronized void delay() {
        try {
            this.wait(delayInMS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void run() {
        for (Collector h : tasks) {
            h.start();
        }
        while (true) {
            delay();
            for (Collector h : tasks) {
                h.collect();
            }
        }
    }
}
