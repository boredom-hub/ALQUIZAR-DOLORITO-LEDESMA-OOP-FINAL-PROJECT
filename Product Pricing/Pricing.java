import javax.swing.*;
import java.awt.*;

public class Pricing {

    Cost cost;
    JPanel panel;
    JComboBox<Object> strategyBox;

    public Pricing(Cost cost) {
        this.cost = cost;

        strategyBox = new JComboBox<Object>(new Object[]{"Cost Plus", "Fixed Price"});

        strategyBox.addActionListener(e -> {
            String selected = String.valueOf(strategyBox.getSelectedItem());
            this.cost.setStrategy(selected);
        });

        panel = UiKit.transparent(new BorderLayout(0, 6));
        panel.add(UiKit.labelRow("Pricing Strategy", 
            new InfoButton("Pricing Strategy", "Choose a pricing strategy for your product. \"Cost Plus\" adds a markup to the cost of "
                        + "production, while \"Fixed Price\" sets a predetermined price.")), BorderLayout.NORTH);
        panel.add(UiKit.comboBox(strategyBox, null), BorderLayout.CENTER);
    }

    public String getSelected() {
        return String.valueOf(strategyBox.getSelectedItem());
    }

    public void setSelected(String strategy) {
        strategyBox.setSelectedItem(strategy);
    }

    public JPanel getPanel() {
        return panel;
    }
}
