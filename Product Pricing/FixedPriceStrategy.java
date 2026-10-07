
public class FixedPriceStrategy implements PricingStrategy {

    private double price;

    public FixedPriceStrategy(double price) {
        this.price = price;
    }

    public String getName() {
        return "Fixed Price";
    }

    public double getTargetValue() {
        return price;
    }

    public double recommendedPrice(double unitCost) {
        return Util.round(price, 2);
    }

    public String formula() {
        return "Selling Price = the fixed price you entered";
    }

    public String explain(double unitCost, double price) {
        return "Fixed price = " + Util.peso(price);
    }
}
