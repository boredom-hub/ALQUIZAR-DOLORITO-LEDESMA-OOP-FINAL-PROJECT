public class ProductPricingModel {

    private String productName = "";
    private PricingStrategy strategy = new CostPlusStrategy(0);
    private int plannedProductionQuantity;
    private int forecastedSalesQuantity;
    private CostList directCosts = new CostList(CostType.DIRECT);
    private CostList indirectCosts = new CostList(CostType.INDIRECT);

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public PricingStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(PricingStrategy strategy) {
        this.strategy = strategy;
    }

    public int getPlannedProductionQuantity() {
        return plannedProductionQuantity;
    }

    public void setPlannedProductionQuantity(int quantity) {
        this.plannedProductionQuantity = quantity;
    }

    public int getForecastedSalesQuantity() {
        return forecastedSalesQuantity;
    }

    public void setForecastedSalesQuantity(int quantity) {
        this.forecastedSalesQuantity = quantity;
    }

    public CostList getDirectCosts() {
        return directCosts;
    }

    public CostList getIndirectCosts() {
        return indirectCosts;
    }

    public boolean hasAnyCosts() {
        return !directCosts.isEmpty() || !indirectCosts.isEmpty();
    }
}
