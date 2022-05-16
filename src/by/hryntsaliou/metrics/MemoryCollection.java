package by.hryntsaliou.metrics;

import by.hryntsaliou.gui.SystemMonitorWindow;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class MemoryCollection extends Collector {

    private final int numCores;

    public MemoryCollection(SystemMonitorWindow theInterface, int cpuCores) {
        super(theInterface);
        numCores = cpuCores;
    }

    @Override
    public void readData() {
        ArrayList<String> arrayList = readFile();
        showMemoryInfo(arrayList);
    }

    private ArrayList<String> readFile() {
        ArrayList<String> values = null;
        BufferedReader bufferedReader;
        String line;
        try {
            bufferedReader = new BufferedReader(new FileReader("/proc/meminfo"));
            values = new ArrayList<>();

            while ((line = bufferedReader.readLine()) != null) {
                values.add(line);
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }

    private void showMemoryInfo(ArrayList<String> lines) {
        int totalMem = -1,
                memFree = -1,
                memActive = -1,
                memInactive = -1,
                swapTotal = -1,
                swapFree = -1,
                dirtyPages = -1,
                writeBack = -1;
        for (String line : lines) {
            String[] tokens = line.split(" +");
            if (tokens.length < 2) continue;
            String identifier = tokens[0];
            String value = tokens[1];
            switch (identifier) {
                case "MemTotal:":
                    totalMem = Integer.parseInt(value);
                    break;
                case "MemFree:":
                    memFree = Integer.parseInt(value);
                    break;
                case "Active:":
                    memActive = Integer.parseInt(value);
                    break;
                case "Inactive:":
                    memInactive = Integer.parseInt(value);
                    break;
                case "SwapTotal:":
                    swapTotal = Integer.parseInt(value);
                    break;
                case "SwapFree:":
                    swapFree = Integer.parseInt(value);
                    break;
                case "Dirty:":
                    dirtyPages = Integer.parseInt(value);
                    break;
                case "Writeback:":
                    writeBack = Integer.parseInt(value);
                    break;
            }
        }
        calculateMemoryUsage(totalMem, memFree);
        userInterface.updateMemoryInfo(totalMem, memFree, memActive, memInactive,
                swapTotal, swapFree, dirtyPages, writeBack);
    }

    private void calculateMemoryUsage(int totalMem, int memFree) {
        double usedMemory = totalMem - memFree;
        int memoryUsage = (int) (usedMemory / totalMem * 100);
        userInterface.getCPUGraph().addDataPoint(numCores, memoryUsage);
    }
}
