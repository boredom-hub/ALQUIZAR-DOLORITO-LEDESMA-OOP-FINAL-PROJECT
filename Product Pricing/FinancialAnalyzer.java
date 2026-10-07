public class FinancialAnalyzer {

    public static AnalysisResult analyze(ProductPricingModel model) {
        int planned = model.getPlannedProductionQuantity();
        if (planned <= 0) {
            return new AnalysisResult("Production quantity must be greater than 0.");
        }
        int forecast = model.getForecastedSalesQuantity();
       

        double totalDirect = model.getDirectCosts().getTotal();
        double totalIndirect = model.getIndirectCosts().getTotal();

   
        double unitDirect = Util.round(totalDirect / planned, 2);
        double unitIndirect = Util.round(totalIndirect / planned, 2);
        double unitCost = Util.round(unitDirect + unitIndirect, 2);

   
        double price = model.getStrategy().recommendedPrice(unitCost);

    
        double unitVariable = totalDirect / planned;

        double marginPerUnit = price - unitVariable;
    
        if (Math.abs(marginPerUnit) < 0.000000001) {
            return new AnalysisResult("The price equals the variable cost per unit, so the contribution "
                    + "margin is zero and the break-even point cannot be calculated. Try a higher markup or price.");
        }
        if (price == 0) {
            return new AnalysisResult("The recommended price is zero. Enter a markup or a fixed price.");
        }


        double ratio = totalIndirect / marginPerUnit;
        double breakEven = Math.ceil(ratio);
 
        double nearest = Math.round(ratio);
        if (Math.abs(ratio - nearest) < 0.0000000001 * Math.max(1, Math.abs(ratio))) {
            breakEven = nearest;
        }
        breakEven = breakEven + 0.0;    // turns -0.0 into 0.0
        double contributionMargin = Util.round(marginPerUnit, 2);
        double cmRatio = Util.round(contributionMargin / price, 4);

       
        double totalCm = forecast * contributionMargin;
        double leverage = 0;
        if (Math.abs(totalCm - totalIndirect) >= 0.0000001) {
            leverage = Math.abs(Util.round(totalCm / (totalCm - totalIndirect), 2));
        }

    
        double revenue = price * forecast;
        double cost = totalIndirect + unitVariable * forecast;
        double profit = revenue - cost;

        return new AnalysisResult(totalDirect, totalIndirect, unitCost, unitVariable, price, breakEven,
                contributionMargin, cmRatio, leverage, profit);
    }
}
