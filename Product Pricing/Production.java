import javax.swing.*;
import java.awt.*;

public class Production {

    JPanel panel;
    RoundedTextField quantityField;

    public Production() {
        quantityField = new RoundedTextField(10);
        UiKit.limitToNumber(quantityField, false);
        UiKit.selectAllOnFocus(quantityField);

        panel = UiKit.transparent(new BorderLayout(0, 6));
        panel.add(UiKit.labelRow("Production Quantity", new InfoButton("Production Quantity",
                "The number of units you plan to produce.")), BorderLayout.NORTH);
        panel.add(UiKit.segment(quantityField,
                new Component[]{new EndCap(Icons.of(Icons.Kind.BOX, 18, Theme.MUTED))}), BorderLayout.CENTER);
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
