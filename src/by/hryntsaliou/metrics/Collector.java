package by.hryntsaliou.metrics;

import by.hryntsaliou.gui.SystemMonitorWindow;

public abstract class Collector extends Thread {

    protected SystemMonitorWindow userInterface;

    public Collector(SystemMonitorWindow theInterface) {
        userInterface = theInterface;
    }

    public synchronized void collect() {
        this.notifyAll();
    }

    private synchronized void pause() {
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public abstract void readData();

    public synchronized void run() {
        while (true) {
            this.pause();
            this.readData();
        }
    }
}
