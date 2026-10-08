import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// step 4 of 4: the profit analysis chart
public class ChartFrame {

    JFrame frame;
    Product product;
    JLabel lblHover;

    public ChartFrame(Product product) {
        this.product = product;
        product.calculate();

        frame = new JFrame("Chart");
        frame.setSize(940, 700);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblTitle = new JLabel("Step 4 of 4 - Profit Analysis Chart (the shaded area is the Margin of Safety)");
        lblTitle.setBounds(0, 10, 930, 25);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblWarn = new JLabel("");
        lblWarn.setBounds(20, 36, 890, 22);
        lblWarn.setForeground(Color.RED);
        if (!product.valid) {
            lblWarn.setText("Nothing to plot yet: " + product.message);
        }

        lblHover = new JLabel("Move the mouse over the chart to see the values.");
        lblHover.setBounds(20, 540, 890, 25);

        ChartPanel chart = new ChartPanel();
        chart.setBounds(20, 62, 890, 475);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(20, 600, 150, 35);

        JButton btnNew = new JButton("New Product");
        btnNew.setBounds(760, 600, 150, 35);

        btnBack.addActionListener(e -> {
            frame.dispose();
            new ResultsFrame(product);
        });
        btnNew.addActionListener(e -> newProduct());

        frame.add(lblTitle);
        frame.add(lblWarn);
        frame.add(chart);
        frame.add(lblHover);
        frame.add(btnBack);
        frame.add(btnNew);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // starts over with an empty form (asks first)
    private void newProduct() {
        int choice = JOptionPane.showConfirmDialog(frame,
                "Start a new product? The current product's details will be cleared.",
                "New product", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            frame.dispose();
            new Calculator();
        }
    }

    // draws the chart with Java2D (Swing doesnt have a chart component)
    // all the lines are straight so we compute them from price, fixed cost and variable cost per unit
    class ChartPanel extends JPanel {

        String[] names = {"Sales Revenue", "Total Cost", "Profit", "Variable Cost", "Fixed Cost"};
        Color[] colors = {new Color(0x22C55E), new Color(0xEF4444), new Color(0x3B82F6),
                new Color(0xF97316), Color.GRAY};

        double price = 0;
        double fixed = 0;
        double variable = 0;
        double breakEven = 0;
        int expected;
        double xMax;
        double yLo;
        double yHi;
        double yStep;

        int left = 95;
        int top = 40;
        int right = 25;
        int bottom = 50;

        ChartPanel() {
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

            if (product.valid) {
                price = product.price;
                fixed = product.totalIndirect;
                variable = product.unitVariable;
                breakEven = product.breakEven;
            }
            expected = product.sales;

            // x goes up to the bigger of production / sales plus 20%
            int max = Math.max(product.production, expected);
            xMax = Math.max(1, max + Math.ceil(max * 0.2));

            // work out the y range
            double lo = 0;
            double hi = 0;
            for (int s = 0; s < 5; s++) {
                lo = Math.min(lo, Math.min(value(s, 0), value(s, xMax)));
                hi = Math.max(hi, Math.max(value(s, 0), value(s, xMax)));
            }
            if (hi - lo < 0.000000001) {
                lo = 0;
                hi = 4;
            }
            yStep = niceStep(hi - lo, 5);
            yLo = Math.floor(lo / yStep) * yStep;
            yHi = Math.ceil(hi / yStep) * yStep;

            addMouseMotionListener(new MouseAdapter() {
                public void mouseMoved(MouseEvent e) {
                    showValues(e.getX());
                }
            });
        }

        // value of one line at quantity q
        double value(int series, double q) {
            if (series == 0) {
                return price * q;
            } else if (series == 1) {
                return fixed + variable * q;
            } else if (series == 2) {
                return price * q - (fixed + variable * q);
            } else if (series == 3) {
                return variable * q;
            } else {
                return fixed;
            }
        }

        // rounds the step to 1, 2 or 5 x a power of ten so the labels look nice
        double niceStep(double range, int ticks) {
            double raw = range / ticks;
            double magnitude = Math.pow(10, Math.floor(Math.log10(raw)));
            double n = raw / magnitude;
            double nice = 10;
            if (n <= 1) {
                nice = 1;
            } else if (n <= 2) {
                nice = 2;
            } else if (n <= 5) {
                nice = 5;
            }
            return nice * magnitude;
        }

        double px(double q) {
            return left + q / xMax * (getWidth() - left - right);
        }

        double py(double v) {
            int h = getHeight() - top - bottom;
            return top + h - (v - yLo) / (yHi - yLo) * h;
        }

        // shows the exact values under the chart for the quantity the mouse is on
        void showValues(int mouseX) {
            int plotWidth = getWidth() - left - right;
            if (mouseX < left || mouseX > left + plotWidth) {
                lblHover.setText("Move the mouse over the chart to see the values.");
                return;
            }
            long q = Math.round((mouseX - left) / (double) plotWidth * xMax);
            String text = "Quantity: " + q;
            for (int s = 0; s < 5; s++) {
                text = text + "   |   " + names[s] + ": " + Util.peso(value(s, q));
            }
            lblHover.setText(text);
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int plotX = left;
            int plotY = top;
            int plotW = getWidth() - left - right;
            int plotH = getHeight() - top - bottom;
            FontMetrics fm = g2.getFontMetrics();

            // grid lines and the money labels on the left
            g2.setStroke(new BasicStroke(1f));
            for (double v = yLo; v <= yHi + yStep / 2; v += yStep) {
                int y = (int) Math.round(py(v));
                g2.setColor(new Color(0xDDDDDD));
                g2.drawLine(plotX, y, plotX + plotW, y);
                g2.setColor(Color.BLACK);
                String label = Util.peso(v);
                g2.drawString(label, plotX - 8 - fm.stringWidth(label), y + 4);
            }

            // quantity labels at the bottom
            long xStep = Math.max(1, Math.round(niceStep(xMax, 8)));
            for (long q = 0; q <= xMax; q += xStep) {
                int x = (int) Math.round(px(q));
                g2.setColor(new Color(0xDDDDDD));
                g2.drawLine(x, plotY, x, plotY + plotH);
                g2.setColor(Color.BLACK);
                String label = String.valueOf(q);
                g2.drawString(label, x - fm.stringWidth(label) / 2, plotY + plotH + 16);
            }

            // shaded margin of safety (between break-even and expected sales)
            boolean neverBreaksEven = breakEven < 0;
            boolean positive = !neverBreaksEven && expected > breakEven;
            double start = 0;
            double end = expected;
            if (!neverBreaksEven) {
                start = Math.min(breakEven, expected);
                end = Math.max(breakEven, expected);
            }
            start = Math.max(0, Math.min(start, xMax));
            end = Math.max(0, Math.min(end, xMax));
            if (positive) {
                g2.setColor(new Color(34, 197, 94, 60));
            } else {
                g2.setColor(new Color(239, 68, 68, 60));
            }
            g2.fillRect((int) px(start), plotY, (int) (px(end) - px(start)), plotH);

            // the 5 lines
            Shape oldClip = g2.getClip();
            g2.setClip(plotX, plotY - 2, plotW + 2, plotH + 4);
            for (int s = 0; s < 5; s++) {
                g2.setColor(colors[s]);
                if (s >= 3) {
                    g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                            10f, new float[]{5f, 5f}, 0f));
                } else {
                    g2.setStroke(new BasicStroke(2f));
                }
                g2.drawLine((int) px(0), (int) py(value(s, 0)), (int) px(xMax), (int) py(value(s, xMax)));
            }
            g2.setClip(oldClip);

            // vertical lines for break-even and expected sales
            g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                    10f, new float[]{3f, 3f}, 0f));
            if (breakEven >= 0 && breakEven <= xMax) {
                int x = (int) px(breakEven);
                g2.setColor(Color.DARK_GRAY);
                g2.drawLine(x, plotY, x, plotY + plotH);
                g2.drawString("Break-even: " + Util.fixed(breakEven, 0) + " units", x + 5, plotY + plotH - 10);
            }
            int ex = (int) px(expected);
            g2.setColor(new Color(0x2563EB));
            g2.drawLine(ex, plotY, ex, plotY + plotH);
            g2.drawString("Expected Sales: " + expected, ex + 5, plotY + plotH - 28);

            // margin of safety label
            g2.setColor(Color.BLACK);
            if (positive) {
                g2.drawString("Margin of Safety", (int) px(start) + 4, plotY + 18);
            } else {
                g2.drawString("Negative Margin", (int) px(start) + 4, plotY + 18);
            }

            // axes and titles
            g2.setStroke(new BasicStroke(1.5f));
            g2.setColor(Color.BLACK);
            g2.drawLine(plotX, plotY, plotX, plotY + plotH);
            g2.drawLine(plotX, plotY + plotH, plotX + plotW, plotY + plotH);
            String xTitle = "Quantity Produced";
            g2.drawString(xTitle, plotX + (plotW - fm.stringWidth(xTitle)) / 2, plotY + plotH + 38);
            g2.drawString("Amount (" + Util.PESO + ")", 8, 22);

            // legend at the top
            int lx = plotX + 90;
            for (int s = 0; s < 5; s++) {
                g2.setColor(colors[s]);
                g2.drawLine(lx, 18, lx + 20, 18);
                g2.setColor(Color.BLACK);
                g2.drawString(names[s], lx + 26, 22);
                lx = lx + 26 + fm.stringWidth(names[s]) + 20;
            }
        }
    }
}
