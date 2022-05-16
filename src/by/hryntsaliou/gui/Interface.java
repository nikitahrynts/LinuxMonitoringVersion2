package by.hryntsaliou.gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;

public class Interface extends JComponent {

    private static final long serialVersionUID = 1L;

    private final ArrayList<ArrayList<Integer>> DataPoints;
    private final int yMin;
    private final int yMax;
    private final int xMin;
    private final int xMax;
    private int uIntVal;
    private int lineNum;

    public Interface(int tMin, int tMax, int yMin,
                     int yMax, int numLines, int updateIntervalMS) {
        this.yMin = yMin;
        this.yMax = yMax;
        this.xMin = tMin;
        this.xMax = tMax;
        this.uIntVal = (int) ((double) this.xMax / (updateIntervalMS / 1000.0));
        DataPoints = new ArrayList<>();
        for (int i = 0; i < numLines; i++) {
            DataPoints.add(new ArrayList<>());
        }


    }

    public void setUInt(int updateIntervalMS) {
        this.uIntVal = (int) ((double) this.xMax / (updateIntervalMS / 1000.0));
    }

    public void addDataPoint(int lineNum, int value) {
        while ((DataPoints.get(lineNum)).size() > this.uIntVal + 1) {
            (DataPoints.get(lineNum)).remove(0);
        }

        DataPoints.get(lineNum).add(value);
    }

    private Color getEnumerableColor(int num) {
        switch (num) {
            case 0:
                return Color.RED;
            case 1:
                return Color.BLUE;
            case 2:
                return Color.GRAY;
            case 3:
                return Color.GREEN;
            case 4:
                return Color.MAGENTA;
            case 5:
                return Color.ORANGE;
            case 6:
                return new Color(255, 80, 5); // Orange-red
            case 7:
                return Color.CYAN;
            case 8:
                return new Color(194, 0, 136); // Purple-red
            default:
                return Color.BLACK;
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int oBot, oTop = 0, oLeft, oRight;
        int height = this.getHeight();
        int width = this.getWidth();
        int x = 0, y = 0;
        if (this.getBorder() != null) {
            Border a = this.getBorder();
            Insets b = a.getBorderInsets(this);
            oBot = b.bottom;
            oTop = b.top;
            oLeft = b.left;
            oRight = b.right;
            width = width - oRight - oLeft;
            height = height - oBot - oTop;
            x += oLeft;
            y += oTop;
        }
        height = (int) (height * .7);
        width -= 40;
        x += 40;
        g2.setColor(Color.WHITE);
        g2.fillRect(x, y, width, height);
        if ((DataPoints.get(0)).size() < 2) {
            g2.setColor(Color.WHITE);
            g2.drawString("Not Enough Data Polled Yet.", (int) (width / 2.0) - 85, (int) (height / 2.0) + oTop);
        } else {
            int index = 0;
            g2.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (ArrayList<Integer> a : DataPoints) {
                g2.setColor(getEnumerableColor(index++));
                for (int i = a.size() - 2; i >= 0; i--) {
                    int x1 = (width + x) - (int) ((a.size() - 1 - (i + 1)) * ((double) width / (this.uIntVal)));
                    int x2 = (width + x) - (int) ((a.size() - 1 - i) * ((double) width / (this.uIntVal)));
                    int y1 = (height + oTop) - (int) (((double) (height) / (yMax - yMin)) * a.get(i + 1));
                    int y2 = (height + oTop) - (int) (((double) (height) / (yMax - yMin)) * a.get(i));
                    g2.drawLine(x1, y1, x2, y2);
                }
            }
            int newy = (int) ((this.getHeight() - (y + height)) / 2.0);
            if (newy > 14) {
                newy += y + height;
                g2.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                for (int i = 0; i < DataPoints.size(); i++) {
                    g2.setColor(getEnumerableColor(i));
                    g2.fillRect(x + 100 * i, newy, 20, 10);
                    g2.setColor(Color.BLACK);
                    g2.drawRect(x + 100 * i, newy, 20, 10);
                    String prefix = "CPU" + (i + 1);
                    if (DataPoints.size() > 1 && i == DataPoints.size() - 1)
                        prefix = "RAM";
                    if (DataPoints.get(i).size() > 0)
                        g2.drawString(prefix + " " + DataPoints.get(i).get(DataPoints.get(i).size() - 1) + "%", x + 22 + 100 * i, newy + 10);
                }
            }
        }
        float[] Dashes = {2.0f, 4.0f};
        g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, Dashes, 0.0f));
        g2.setColor(Color.BLACK);
        g2.drawLine((int) (x + (width / 2.0)), y, (int) (x + (width / 2.0)), y + height);
        g2.drawLine(x, (int) (y + (height / 2.0)), x + width, (int) (y + height / 2.0));
        g2.drawString(String.valueOf(yMax), x - 30, y + 7);
        g2.drawString(String.valueOf((int) (yMax / 2.0)), x - 25, (int) (y + height / 2.0) + 5);
        g2.drawString(String.valueOf(yMin), x - 20, y + height + 5);
        g2.drawString(xMax + " seconds", x, y + height + 12);
        g2.drawString(String.valueOf((int) (xMax / 2.0)), (int) (x + width / 2.0) - 10, y + height + 12);
        g2.drawString(String.valueOf(xMin), x + width - 10, y + height + 12);
        g2.drawRect(x, y, width, height);
    }
}
