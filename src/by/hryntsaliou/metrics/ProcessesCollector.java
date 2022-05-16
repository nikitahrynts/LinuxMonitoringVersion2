package by.hryntsaliou.metrics;

import by.hryntsaliou.gui.SystemMonitorWindow;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class ProcessesCollector extends Collector {

    public ProcessesCollector(SystemMonitorWindow theInterface) {
        super(theInterface);
    }

    @Override
    public void readData() {
        userInterface.removeAllRowsFromProcList();
        File theFile = new File("/proc/");
        ArrayList<String[]> allpids = new ArrayList<>();
        for (File f : Objects.requireNonNull(theFile.listFiles())) {
            int dirNumber;
            try {
                dirNumber = Integer.parseInt(f.getName());
            } catch (NumberFormatException e) {
                continue;
            }
            ArrayList<String> currentProcess = readFile(dirNumber);
            String[] dataToDisplay = tokenize(currentProcess);
            allpids.add(dataToDisplay);
        }
        for (String[] pid : allpids) {
            userInterface.addRowToProcList(pid);
        }
    }

    private ArrayList<String> readFile(int pidNumber) {
        BufferedReader reader;
        ArrayList<String> currentProcess = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pidNumber + "/status"));
            currentProcess = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                currentProcess.add(currentLine);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return currentProcess;
    }

    private String[] tokenize(ArrayList<String> process) {
        String name = null, pid = null, state = null,
                threads = null, volctx = null, nonvol = null;
        for (String lineOfData : process) {
            String[] tokens = lineOfData.split("\t");
            if (tokens.length < 2) continue;
            String id = tokens[0].trim();
            String value = tokens[1];

            switch (id) {
                case "Name:":
                    name = value;
                    break;
                case "Pid:":
                    pid = value;
                    break;
                case "State:":
                    state = value;
                    break;
                case "Threads:":
                    threads = value;
                    break;
                case "voluntary_ctxt_switches:":
                    volctx = value;
                    break;
                case "nonvoluntary_ctxt_switches:":
                    nonvol = value;
                    break;
            }
        }
        return new String[]{name, pid, state, threads, volctx, nonvol};
    }
}
