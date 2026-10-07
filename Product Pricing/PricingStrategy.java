public interface PricingStrategy {

    String getName();

    double getTargetValue();

    double recommendedPrice(double unitCost);

    String formula();

    String explain(double unitCost, double price);
}
