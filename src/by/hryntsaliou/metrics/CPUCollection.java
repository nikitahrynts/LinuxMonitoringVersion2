package by.hryntsaliou.metrics;

import by.hryntsaliou.gui.SystemMonitorWindow;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CPUCollection extends Collector {

    private final int numCores;

    public CPUCollection(SystemMonitorWindow theInterface, int cpuCores) {
        super(theInterface);
        numCores = cpuCores;
    }

    @Override
    public void readData() {
        ArrayList<String> firstRead, secondRead;
        if ((firstRead = getProcessorValues()) == null) {
            return;
        }
        waitBetweenRead();
        if ((secondRead = getProcessorValues()) == null) {
            return;
        }
        calculateAndGraph(firstRead, secondRead);
    }

    private void waitBetweenRead() {
        try {
            wait(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> getProcessorValues() {
        ArrayList<String> values;
        BufferedReader reader;
        String line;
        try {
            reader = new BufferedReader(new FileReader("/proc/stat"));
            values = new ArrayList<>();
            line = reader.readLine();
            while (!line.contains("intr")) {
                line = reader.readLine();
                if (line.contains("cpu")) {
                    String[] lineValues = line.split(" ");
                    StringBuilder temp = new StringBuilder();
                    for (int i = 1; i < lineValues.length; i++) {
                        temp.append(lineValues[i]).append(" ");
                    }
                    values.add(temp.toString());
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return values;
    }

    private void calculateAndGraph(ArrayList<String> first, ArrayList<String> second) {
        for (int i = 0; i < numCores; i++) {
            String[] firstTokens = first.get(i).split(" ");
            String[] secondTokens = second.get(i).split(" ");
            double user = Double.parseDouble(secondTokens[0]);
            double userPrevious = Double.parseDouble(firstTokens[0]);
            double userDiff = Math.abs(user - userPrevious);
            double system = Double.parseDouble(secondTokens[1]);
            double systemPrevious = Double.parseDouble(firstTokens[1]);
            double systemDiff = Math.abs(system - systemPrevious);
            double nice = Double.parseDouble(secondTokens[2]);
            double nicePrevious = Double.parseDouble(firstTokens[2]);
            double niceDiff = Math.abs(nice - nicePrevious);
            double idle = Double.parseDouble(secondTokens[3]);
            double idlePrevious = Double.parseDouble(firstTokens[3]);
            double idleDiff = Math.abs(idle - idlePrevious);
            double numerator = systemDiff + userDiff;
            double denominator = numerator + idleDiff;
            int cpuUtil = (int) ((numerator / denominator) * 100.0);
            userInterface.getCPUGraph().addDataPoint(i, cpuUtil);
            userInterface.getCPUGraph().repaint();
        }
    }
}
