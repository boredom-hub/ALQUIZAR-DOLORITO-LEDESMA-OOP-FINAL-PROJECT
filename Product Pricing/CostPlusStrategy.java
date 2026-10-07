public class CostPlusStrategy implements PricingStrategy {

    private double markupPercent;

    public CostPlusStrategy(double markupPercent) {
        this.markupPercent = markupPercent;
    }

    public String getName() {
        return "Cost Plus";
    }

    public double getTargetValue() {
        return markupPercent;
    }

    public double recommendedPrice(double unitCost) {
        double price = unitCost * (1 + markupPercent / 100);
        return Util.round(price, 2);
    }

    public String formula() {
        return "Unit Cost x (1 + Markup % / 100)";
    }

    public String explain(double unitCost, double price) {
        return Util.peso(unitCost) + " x (1 + " + Util.num(markupPercent) + " / 100) = " + Util.peso(price);
    }
}
