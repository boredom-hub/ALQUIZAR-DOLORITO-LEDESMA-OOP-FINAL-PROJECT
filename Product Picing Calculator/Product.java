import java.util.ArrayList;

public class Product {

    public String name = "";
    public String strategy = "Cost Plus";     
    public double strategyValue = 0;          
    public int production = 0;
    public int sales = 0;
    public ArrayList<CostItem> directCosts = new ArrayList<CostItem>();
    public ArrayList<CostItem> indirectCosts = new ArrayList<CostItem>();

    // results, filled in by calculate()
    public boolean valid = false;
    public String message = "";
    public double totalDirect;
    public double totalIndirect;
    public double unitCost;
    public double unitVariable;
    public double price;
    public double breakEven;
    public double contributionMargin;
    public double cmRatio;
    public double leverage;
    public double profit;

    public boolean hasAnyCosts() {
        return !directCosts.isEmpty() || !indirectCosts.isEmpty();
    }

    public double totalOf(ArrayList<CostItem> list) {
        double sum = 0;
        for (CostItem item : list) {
            sum = sum + item.getTotal();
        }
        return sum;
    }

    public void calculate() {
        valid = false;
        message = "";
        if (production <= 0) {
            message = "Production quantity must be greater than 0.";
            return;
        }

        totalDirect = totalOf(directCosts);
        totalIndirect = totalOf(indirectCosts);

        double unitDirect = Util.round(totalDirect / production, 2);
        double unitIndirect = Util.round(totalIndirect / production, 2);
        unitCost = Util.round(unitDirect + unitIndirect, 2);

        if (strategy.equals("Cost Plus")) {
            price = Util.round(unitCost * (1 + strategyValue / 100), 2);
        } else {
            price = Util.round(strategyValue, 2);
        }

        unitVariable = totalDirect / production;
        double margin = price - unitVariable;

        if (Math.abs(margin) < 0.000000001) {
            message = "The price equals the variable cost per unit, so the contribution margin is zero "
                    + "and the break-even point cannot be calculated. Try a higher markup or price.";
            return;
        }
        if (price == 0) {
            message = "The recommended price is zero. Enter a markup or a fixed price.";
            return;
        }

        double ratio = totalIndirect / margin;
        breakEven = Math.ceil(ratio);

        double nearest = Math.round(ratio);
        if (Math.abs(ratio - nearest) < 0.0000000001 * Math.max(1, Math.abs(ratio))) {
            breakEven = nearest;
        }
        breakEven = breakEven + 0.0; 

        contributionMargin = Util.round(margin, 2);
        cmRatio = Util.round(contributionMargin / price, 4);

        double totalCm = sales * contributionMargin;
        leverage = 0;
        if (Math.abs(totalCm - totalIndirect) >= 0.0000001) {
            leverage = Math.abs(Util.round(totalCm / (totalCm - totalIndirect), 2));
        }

        profit = (price * sales) - (totalIndirect + unitVariable * sales);
        valid = true;
    }
}
