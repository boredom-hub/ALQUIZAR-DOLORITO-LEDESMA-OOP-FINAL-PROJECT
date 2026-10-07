import javax.swing.*;
import java.awt.*;

public class Sales {

    Production production;
    JPanel panel;
    RoundedTextField quantityField;
    RoundedButton button50;
    RoundedButton button75;
    RoundedButton button100;

    public Sales(Production production) {
        this.production = production;

        quantityField = new RoundedTextField(10);
        UiKit.limitToNumber(quantityField, false);
        UiKit.selectAllOnFocus(quantityField);

        button50 = UiKit.segmentButton("50%");
        button75 = UiKit.segmentButton("75%");
        button100 = UiKit.segmentButton("100%");

        button50.addActionListener(e -> {
            setPercent(50);
        });

        button75.addActionListener(e -> {
            setPercent(75);
        });

        button100.addActionListener(e -> {
            setPercent(100);
        });

        panel = UiKit.transparent(new BorderLayout(0, 6));
        panel.add(UiKit.labelRow("Sales Quantity", new InfoButton("Sales Quantity",
                "The number of units you plan to sell.")), BorderLayout.NORTH);
        panel.add(UiKit.segment(quantityField, new Component[]{button50, button75, button100}),
                BorderLayout.CENTER);
    }

    public void setPercent(int percent) {
        int total;
        try {
            total = Integer.parseInt(production.getQuantity().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(panel, "Enter the production quantity first.",
                    "Sales quantity", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        long sales = Math.round(total * (percent / 100.0));
        quantityField.setText(String.valueOf(sales));
    }

    public String getQuantity() {
        return quantityField.getText();
    }

    public void setQuantity(int quantity) {
        quantityField.setText(String.valueOf(quantity));
    }

    public JPanel getPanel() {
        return panel;
    }
}
