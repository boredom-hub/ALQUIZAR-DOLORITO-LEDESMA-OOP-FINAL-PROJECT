import javax.swing.*;

public class ResultsFrame {

    JFrame frame;
    Product product;
    JEditorPane output;

    public ResultsFrame(Product product) {
        this.product = product;
        product.calculate();

        frame = new JFrame("Results");
        frame.setSize(640, 640);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblStep = new JLabel("Step 3 of 4 - Results");
        lblStep.setBounds(0, 10, 630, 30);
        lblStep.setHorizontalAlignment(SwingConstants.CENTER);

        // an editor pane can show html, so the values can be bold and bigger than the rest
        output = new JEditorPane();
        output.setContentType("text/html");
        output.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        output.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 13));
        output.setEditable(false);
        output.setMargin(new java.awt.Insets(8, 10, 8, 10));

        JScrollPane scroll = new JScrollPane(output);
        scroll.setBounds(20, 50, 585, 480);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(20, 550, 150, 35);

        JButton btnNext = new JButton("Next: Chart");
        btnNext.setBounds(455, 550, 150, 35);

        btnBack.addActionListener(e -> {
            frame.dispose();
            new CostsFrame(product);
        });
        btnNext.addActionListener(e -> {
            frame.dispose();
            new ChartFrame(product);
        });

        frame.add(lblStep);
        frame.add(scroll);
        frame.add(btnBack);
        frame.add(btnNext);

        showResults();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // one result: the label, the value (big and bold), then the formula and calculation in small grey text
    private String block(String label, String value, String formula, String calculation) {
        return "<p><b><font size=\"4\">" + label + ": </font></b><b><font size=\"5\">" + value + "</font></b><br>"
                + "<font size=\"3\" color=\"#555555\">&nbsp;&nbsp;&nbsp;Formula: " + formula + "<br>"
                + "&nbsp;&nbsp;&nbsp;Calculation: " + calculation + "</font></p>";
    }

    private String warning(String message) {
        return "<p><b><font color=\"#CC0000\">Warning: " + message + "</font></b></p>";
    }

    // so a product name with < or & doesnt break the html
    private String esc(String text) {
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private void showResults() {
        Product p = product;
        String text = "<html><body>";
        text = text + "<b><font size=\"5\">" + esc(p.name) + "</font></b><br>"
                + p.strategy + " strategy  -  " + p.production + " units produced<br>";

        if (p.production > 0) {
            text = text + block("Unit Cost", Util.peso(p.unitCost),
                    "(Total Direct Costs + Total Indirect Costs) / Production Quantity",
                    "(" + Util.peso(p.totalDirect) + " + " + Util.peso(p.totalIndirect) + ") / "
                            + p.production + " = " + Util.peso(p.unitCost));

            if (p.strategy.equals("Cost Plus")) {
                text = text + block("Recommended Price", Util.peso(p.price),
                        "Unit Cost x (1 + Markup % / 100)",
                        Util.peso(p.unitCost) + " x (1 + " + Util.num(p.strategyValue) + " / 100) = "
                                + Util.peso(p.price));
            } else {
                text = text + block("Recommended Price", Util.peso(p.price),
                        "Selling Price = the fixed price you entered",
                        "Fixed price = " + Util.peso(p.price));
            }
        }

        if (!p.valid) {
            text = text + warning(p.message) + "</body></html>";
            output.setText(text);
            return;
        }

        if (p.contributionMargin < 0) {
            text = text + warning("The price is below the variable cost per unit, so every unit sold loses "
                    + "money and the business can never break even. Try a higher markup or price.");
        }

        double margin = p.price - p.unitVariable;
        text = text + block("Break-even Point", Util.fixed(p.breakEven, 2) + " units",
                "Fixed Costs / (Price per Unit - Variable Cost per Unit)",
                Util.peso(p.totalIndirect) + " / (" + Util.peso(p.price) + " - " + Util.peso(p.unitVariable)
                        + ") = " + Util.fixed(p.totalIndirect / margin, 2) + ", rounded up to "
                        + Util.fixed(p.breakEven, 0) + " units");

        double pct = p.sales * 100.0 / p.production;
        text = text + block("Expected Sales", p.sales + " units",
                "The sales quantity you entered in the Product Details step",
                p.sales + " units (" + Util.fixed(pct, 1) + "% of the " + p.production + " units produced)");

        text = text + block("Profit at Expected Sales", Util.peso(p.profit),
                "(Price x Expected Sales) - (Fixed Costs + Variable Cost per Unit x Expected Sales)",
                "(" + Util.peso(p.price) + " x " + p.sales + ") - (" + Util.peso(p.totalIndirect) + " + "
                        + Util.peso(p.unitVariable) + " x " + p.sales + ") = " + Util.peso(p.profit));

        text = text + block("Contribution Margin Ratio", Util.fixed(p.cmRatio * 100, 2) + "%",
                "(Contribution Margin per Unit / Price) x 100",
                "(" + Util.peso(p.contributionMargin) + " / " + Util.peso(p.price) + ") x 100 = "
                        + Util.fixed(p.cmRatio * 100, 2) + "%");

        double totalCm = p.sales * p.contributionMargin;
        String cm = "(" + p.sales + " x " + Util.peso(p.contributionMargin) + ")";
        String leverageCalc = cm + " / (" + cm + " - " + Util.peso(p.totalIndirect) + ") = "
                + Util.fixed(p.leverage, 2);
        if (Math.abs(totalCm - p.totalIndirect) < 0.005) {
            leverageCalc = leverageCalc + "  (shown as 0 when sales exactly cover fixed costs)";
        }
        text = text + block("Operating Leverage", Util.fixed(p.leverage, 2),
                "Total Contribution Margin / (Total Contribution Margin - Fixed Costs)", leverageCalc);

        text = text + "</body></html>";
        output.setText(text);
        output.setCaretPosition(0);
    }
}
