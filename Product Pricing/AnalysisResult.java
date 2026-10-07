public class AnalysisResult {

    private boolean valid;
    private String message;
    private double totalDirectCost;
    private double totalIndirectCost;
    private double unitCost;
    private double unitVariableCost;
    private double recommendedPrice;
    private double breakEvenPoint;
    private double contributionMargin;
    private double contributionMarginRatio;
    private double operatingLeverage;
    private double profitAtExpectedSales;

    public AnalysisResult(String message) {
        this.valid = false;
        this.message = message;
    }
    
    public AnalysisResult(double totalDirectCost, double totalIndirectCost, double unitCost,
                          double unitVariableCost, double recommendedPrice, double breakEvenPoint,
                          double contributionMargin, double contributionMarginRatio,
                          double operatingLeverage, double profitAtExpectedSales) {
        this.valid = true;
        this.message = "";
        this.totalDirectCost = totalDirectCost;
        this.totalIndirectCost = totalIndirectCost;
        this.unitCost = unitCost;
        this.unitVariableCost = unitVariableCost;
        this.recommendedPrice = recommendedPrice;
        this.breakEvenPoint = breakEvenPoint;
        this.contributionMargin = contributionMargin;
        this.contributionMarginRatio = contributionMarginRatio;
        this.operatingLeverage = operatingLeverage;
        this.profitAtExpectedSales = profitAtExpectedSales;
    }

    public boolean isValid()                   { return valid; }
    public String getMessage()                 { return message; }
    public double getTotalDirectCost()         { return totalDirectCost; }
    public double getTotalIndirectCost()       { return totalIndirectCost; }
    public double getUnitCost()                { return unitCost; }
    public double getUnitVariableCost()        { return unitVariableCost; }
    public double getRecommendedPrice()        { return recommendedPrice; }
    public double getBreakEvenPoint()          { return breakEvenPoint; }
    public double getContributionMargin()      { return contributionMargin; }
    public double getContributionMarginRatio() { return contributionMarginRatio; }
    public double getOperatingLeverage()       { return operatingLeverage; }
    public double getProfitAtExpectedSales()   { return profitAtExpectedSales; }
}
