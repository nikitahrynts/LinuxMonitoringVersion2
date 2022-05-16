package by.hryntsaliou;

import by.hryntsaliou.gui.SystemMonitorWindow;
import by.hryntsaliou.metrics.*;

import java.util.ArrayList;

public class Main {

    private static int delay = 1000;
    private static int pidDelay = 6000;
    private static Scheduler processorScheduler;
    private static Scheduler pidScheduler;
    private static int cpuCores;

    public static void setUpdateInterval(int updateInterval) {
        delay = updateInterval;
        processorScheduler.updateDelay(delay);
    }

    public static void main(String[] args) {
        cpuCores = Runtime.getRuntime().availableProcessors();
        SystemMonitorWindow mySysMon = new SystemMonitorWindow(cpuCores);
        ArrayList<Collector> processorArray = new ArrayList<Collector>();
        processorArray.add(new CPUCollection(mySysMon, cpuCores));
        processorArray.add(new MemoryCollection(mySysMon, cpuCores));
        processorScheduler = new Scheduler(delay, processorArray);
        processorScheduler.start();
        ArrayList<Collector> pidArray = new ArrayList<Collector>();
        pidArray.add(new ProcessesCollector(mySysMon));
        pidScheduler = new Scheduler(pidDelay, pidArray);
        pidScheduler.start();
    }
}
