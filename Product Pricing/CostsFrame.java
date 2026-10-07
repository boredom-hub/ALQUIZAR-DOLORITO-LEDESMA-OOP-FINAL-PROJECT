import javax.swing.*;
import java.awt.Component;

public class CostsFrame extends StepFrame {

    public CostsFrame(ProductPricingModel model) {
        super(model, 2, 4, "Costs", 780, 700);
        setBackLabel("Back");
        setNextLabel("Next: Results");

        JPanel page = new ScrollPage(null);
        page.setLayout(new BoxLayout(page, BoxLayout.Y_AXIS));
        page.setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));

        CostPanel direct = new CostPanel(model.getDirectCosts());
        CostPanel indirect = new CostPanel(model.getIndirectCosts());
        direct.setAlignmentX(Component.LEFT_ALIGNMENT);
        indirect.setAlignmentX(Component.LEFT_ALIGNMENT);

        page.add(direct);
        page.add(UiKit.vgap(26));
        page.add(indirect);

        setBody(UiKit.scroll(page));
        showFrame();
    }

    protected void onBack() {
        dispose();
        new Calculator(model);
    }

    protected void onNext() {
        if (!model.hasAnyCosts()) {
            JOptionPane.showMessageDialog(this, "Add at least one direct or indirect cost to continue.",
                    "No costs yet", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        dispose();
        new MetricsFrame(model);
    }
}
