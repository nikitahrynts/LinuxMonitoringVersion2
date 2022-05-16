package by.hryntsaliou.gui;

import by.hryntsaliou.Main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SystemMonitorWindow extends JFrame implements ActionListener {

    private final Interface CPUGraph;
    private static final long serialVersionUID = 1L;
    private final JLabel[] MemoryLabels;
    private final DefaultTableModel TableData;

    public Interface getCPUGraph() {
        return CPUGraph;
    }

    public void addRowToProcList(String[] data) {
        TableData.addRow(data);
    }

    public void removeAllRowsFromProcList() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                while (TableData.getRowCount() >= 1) {
                    TableData.removeRow(0);
                }
            } catch (IndexOutOfBoundsException e) {
            }
            TableData.fireTableDataChanged();
        });
    }

    public void updateMemoryInfo(int totalMem, int memFree, int memActive, int memInactive,
                                 int swapTotal, int swapFree, int dirtyPages, int writeBack) {
        MemoryLabels[0].setText("Memory Total: " + totalMem + " KB");
        MemoryLabels[1].setText("Memory Free: " + memFree + " KB");
        MemoryLabels[2].setText("Memory Active: " + memActive + " KB");
        MemoryLabels[3].setText("Memory Inactive: " + memInactive + " KB");
        MemoryLabels[4].setText("Swap Total: " + swapTotal + " KB");
        MemoryLabels[5].setText("Swap Free: " + swapFree + " KB");
        MemoryLabels[6].setText("Dirty Pages: " + dirtyPages + " KB");
        MemoryLabels[7].setText("Writeback: " + writeBack + " KB");
    }

    public SystemMonitorWindow(int numCores) {
        this.setTitle("Linux System Monitor");
        this.setSize(600, 600);
        this.setMinimumSize(new Dimension(950, 500));
        JPanel windowPanel = new JPanel();
        windowPanel.setLayout(new GridLayout(3, 0));
        JMenuBar menuBar = new JMenuBar();
        JMenu Prefs = new JMenu("Update Interval");
        JMenuItem halfSec = new JMenuItem("1/2 Second");
        halfSec.addActionListener(this);
        Prefs.add(halfSec);
        JMenuItem oneSec = new JMenuItem("1 Second");
        oneSec.addActionListener(this);
        Prefs.add(oneSec);
        JMenuItem fiveSec = new JMenuItem("5 Seconds");
        fiveSec.addActionListener(this);
        Prefs.add(fiveSec);
        menuBar.add(Prefs);
        CPUGraph = new Interface(0, 60, 0, 100, numCores + 1, 500);
        CPUGraph.setBorder(BorderFactory.createTitledBorder("CPU/Memory Usage"));
        windowPanel.add(CPUGraph);
        JPanel MemoryInfo = new JPanel();
        MemoryInfo.setLayout(new GridLayout(4, 1, 5, 5));
        MemoryLabels = new JLabel[8];
        MemoryLabels[0] = new JLabel("Memory Total: N/A");
        MemoryLabels[1] = new JLabel("Memory Free: N/A");
        MemoryLabels[2] = new JLabel("Memory Active: N/A");
        MemoryLabels[3] = new JLabel("Memory Inactive: N/A");
        MemoryLabels[4] = new JLabel("Swap Total: N/A");
        MemoryLabels[5] = new JLabel("Swap Free: N/A");
        MemoryLabels[6] = new JLabel("Dirty Pages: N/A");
        MemoryLabels[7] = new JLabel("Writeback: N/A");
        for (int i = 0; i < 8; i++) {
            MemoryInfo.add(MemoryLabels[i]);
        }
        MemoryInfo.setBorder(BorderFactory.createTitledBorder("Memory Information"));
        windowPanel.add(MemoryInfo);
        JPanel ProcInfo = new JPanel();
        ProcInfo.setLayout(new GridLayout(1, 0));
        ProcInfo.setBorder(BorderFactory.createTitledBorder("Process Information"));
        String[] columnNames = {"Name", "pid", "State", "# Threads", "Vol ctxt sw", "nonVol ctxt sw"};
        String[][] data = {{"N/A", "N/A", "N/A", "N/A", "N/A", "N/A"}};
        TableData = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(TableData);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        ProcInfo.add(scrollPane);
        windowPanel.add(ProcInfo);
        this.add(windowPanel);
        this.setJMenuBar(menuBar);
        this.setLocationByPlatform(true);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        JMenuItem j = (JMenuItem) arg0.getSource();
        int newInterval = 0;
        if (j.getText().equals("1/2 Second"))
            newInterval = 500;
        if (j.getText().equals("1 Second"))
            newInterval = 1000;
        if (j.getText().equals("5 Seconds"))
            newInterval = 5000;
        Main.setUpdateInterval(newInterval);
        this.CPUGraph.setUInt(newInterval);
    }
}
