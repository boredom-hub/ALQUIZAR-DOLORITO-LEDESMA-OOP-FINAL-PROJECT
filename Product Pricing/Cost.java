import javax.swing.*;
import java.awt.*;

public class Cost {

    JPanel panel;
    JLabel label;
    InfoButton info;
    JSlider slider;
    RoundedTextField txtMarkup;
    RoundedTextField txtFixedPrice;
    CardLayout cardLayout;
    JPanel cards;

    boolean costPlus = true;
    boolean syncing = false;

    public Cost() {
        label = UiKit.label("Cost Plus", Font.BOLD, 13, Theme.TEXT);
        info = new InfoButton("", "");

        slider = UiKit.slider(0, 1000, 0);   // tenths of a percent: 0.0% to 100.0%
        txtMarkup = new RoundedTextField(5);
        txtFixedPrice = new RoundedTextField(8);

        UiKit.limitToNumber(txtMarkup, true);
        UiKit.limitToNumber(txtFixedPrice, true);
        UiKit.selectAllOnFocus(txtMarkup);
        UiKit.selectAllOnFocus(txtFixedPrice);

        cardLayout = new CardLayout();
        cards = UiKit.transparent(cardLayout);
        cards.add(buildCostPlusPanel(), "Cost Plus");
        cards.add(buildFixedPricePanel(), "Fixed Price");

        panel = UiKit.transparent(new BorderLayout(0, 6));
        panel.add(UiKit.labelRow(label, info), BorderLayout.NORTH);
        panel.add(cards, BorderLayout.CENTER);

        slider.addChangeListener(e -> {
            if (!syncing) {
                syncing = true;
                txtMarkup.setText(Util.fixed(slider.getValue() / 10.0, 1));
                syncing = false;
            }
        });
        txtMarkup.getDocument().addDocumentListener(new SimpleDocListener(this::markupTyped));

        setStrategy("Cost Plus");
    }

    private JComponent buildCostPlusPanel() {
        JPanel p = UiKit.transparent(new BorderLayout(0, 8));
        p.add(slider, BorderLayout.NORTH);
        JPanel row = UiKit.transparent(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.add(txtMarkup);
        JLabel percent = UiKit.label("%", Font.PLAIN, 13, Theme.MUTED);
        percent.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        row.add(percent);
        p.add(row, BorderLayout.CENTER);
        return p;
    }

    private JComponent buildFixedPricePanel() {
        JPanel row = UiKit.transparent(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel peso = UiKit.label(Util.PESO, Font.BOLD, 15, Theme.MUTED);
        peso.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
        row.add(peso);
        row.add(txtFixedPrice);
        JPanel p = UiKit.transparent(new BorderLayout());
        p.add(row, BorderLayout.NORTH);
        return p;
    }

    private void markupTyped() {
        if (syncing) {
            return;
        }
        try {
            double percent = Double.parseDouble(txtMarkup.getText());
            syncing = true;
            slider.setValue((int) Math.min(slider.getMaximum(), Math.round(percent * 10)));
            syncing = false;
        } catch (NumberFormatException e) {
        }
    }

    public void setStrategy(String strategy) {
        costPlus = !strategy.equals("Fixed Price");
        if (costPlus) {
            label.setText("Cost Plus");
            info.setContent("Cost Plus Strategy",
                    "This slider allows you to set the percentage markup on the cost of production.");
            cardLayout.show(cards, "Cost Plus");
        } else {
            label.setText("Fixed Price");
            info.setContent("Fixed Price Strategy", "Enter the fixed price for your product.");
            cardLayout.show(cards, "Fixed Price");
        }
    }

    public void loadMarkup(double percent) {
        syncing = true;
        txtMarkup.setText(Util.fixed(percent, 1));
        slider.setValue((int) Math.min(slider.getMaximum(), Math.round(percent * 10)));
        txtFixedPrice.setText("0");
        syncing = false;
    }

    public void loadFixedPrice(double price) {
        syncing = true;
        txtFixedPrice.setText(Util.num(price));
        txtMarkup.setText("0.0");
        slider.setValue(0);
        syncing = false;
    }

    public String getValueText() {
        if (costPlus) {
            return txtMarkup.getText();
        }
        return txtFixedPrice.getText();
    }

    public JPanel getPanel() {
        return panel;
    }
}
