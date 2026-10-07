import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Calculator extends StepFrame {

    private RoundedTextField txtName = new RoundedTextField(20);
    private Cost cost = new Cost();
    private Pricing pricing = new Pricing(cost);
    private Production production = new Production();
    private Sales sales = new Sales(production);

    public Calculator() {
        this(new ProductPricingModel());
    }

    public Calculator(ProductPricingModel model) {
        super(model, 1, 4, "Product Details", 760, 600);
        setBackLabel("Log out");
        setNextLabel("Next: Costs");
        setBody(buildBody());
        loadFromModel();
        showFrame();
    }

    private JComponent buildBody() {
        RoundedPanel card = new RoundedPanel(new GridBagLayout(), 16);
        card.setBorder(BorderFactory.createEmptyBorder(22, 24, 24, 24));

        place(card, UiKit.labelRow("Product Name", new InfoButton("Product Name",
                "The name of the product you are pricing.")), 0, 0, 2, 6, GridBagConstraints.CENTER);
        place(card, txtName, 0, 1, 2, 18, GridBagConstraints.CENTER);

        place(card, cost.getPanel(), 0, 2, 1, 18, GridBagConstraints.CENTER);
        place(card, pricing.getPanel(), 1, 2, 1, 18, GridBagConstraints.NORTH);

        place(card, production.getPanel(), 0, 3, 1, 0, GridBagConstraints.CENTER);
        place(card, sales.getPanel(), 1, 3, 1, 0, GridBagConstraints.CENTER);

        JPanel page = new ScrollPage(new BorderLayout());
        page.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        page.add(card, BorderLayout.NORTH);
        return UiKit.scroll(page);
    }

    private static void place(JPanel parent, Component c, int x, int y, int width, int bottomGap, int anchor) {
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = x;
        g.gridy = y;
        g.gridwidth = width;
        g.weightx = 0.5;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = anchor;
        int left = 0;
        int right = 0;
        if (x == 1 && width == 1) {
            left = 12;
        }
        if (x == 0 && width == 1) {
            right = 12;
        }
        g.insets = new Insets(0, left, bottomGap, right);
        parent.add(c, g);
    }

    private static int parseWholeNumber(String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static double parseDecimal(String text) {
        try {
            double v = Double.parseDouble(text.trim());
            if (Double.isInfinite(v)) {
                return Double.NaN;
            }
            return v;
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    private void loadFromModel() {
        txtName.setText(model.getProductName());
        production.setQuantity(model.getPlannedProductionQuantity());
        sales.setQuantity(model.getForecastedSalesQuantity());

        PricingStrategy s = model.getStrategy();
        if (s instanceof FixedPriceStrategy) {
            pricing.setSelected("Fixed Price");
            cost.loadFixedPrice(s.getTargetValue());
        } else {
            pricing.setSelected("Cost Plus");
            cost.loadMarkup(s.getTargetValue());
        }
        cost.setStrategy(pricing.getSelected());
    }

    protected void onBack() {
        dispose();
        new MainFrame();
    }

    protected void onNext() {
        ArrayList<String> problems = new ArrayList<String>();

        String name = txtName.getText().trim();
        if (name.isEmpty()) {
            problems.add("Product name is required.");
        }

        int productionQty = parseWholeNumber(production.getQuantity());
        if (productionQty <= 0) {
            problems.add("Production quantity must be a whole number greater than 0.");
        }

        int salesQty = parseWholeNumber(sales.getQuantity());
        if (salesQty < 0) {
            problems.add("Sales quantity must be a whole number (0 or more).");
        } else if (productionQty > 0 && salesQty > productionQty) {
            problems.add("Sales quantity cannot exceed production quantity.");
        }

        boolean costPlus = pricing.getSelected().equals("Cost Plus");
        double target = parseDecimal(cost.getValueText());
        if (Double.isNaN(target) || target < 0) {
            if (costPlus) {
                problems.add("Cost plus markup must be a number of 0 or more.");
            } else {
                problems.add("Fixed price must be a number of 0 or more.");
            }
        }

        if (!problems.isEmpty()) {
            showProblems(problems);
            return;
        }

        model.setProductName(name);
        model.setPlannedProductionQuantity(productionQty);
        model.setForecastedSalesQuantity(salesQty);
        if (costPlus) {
            model.setStrategy(new CostPlusStrategy(target));
        } else {
            model.setStrategy(new FixedPriceStrategy(target));
        }

        dispose();
        new CostsFrame(model);
    }
}
