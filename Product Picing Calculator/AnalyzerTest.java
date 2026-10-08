public class AnalyzerTest {

    static int pass = 0;
    static int fail = 0;

    static void check(String test, String what, double expected, double actual) {
        if (Math.abs(expected - actual) <= 0.000001 * Math.max(1, Math.abs(expected))) {
            pass++;
        } else {
            fail++;
            System.out.println("FAIL " + test + " " + what + ": expected " + expected + " but got " + actual);
        }
    }

    static void run(String test, Product m, boolean shouldBeValid, double[] e) {
        m.calculate();
        if (m.valid != shouldBeValid) {
            fail++;
            System.out.println("FAIL " + test + " valid flag: expected " + shouldBeValid);
            return;
        }
        if (!shouldBeValid) {
            pass++;
            return;
        }
        check(test, "unit cost", e[0], m.unitCost);
        check(test, "price", e[1], m.price);
        check(test, "break-even", e[2], m.breakEven);
        check(test, "contribution margin", e[3], m.contributionMargin);
        check(test, "cm ratio", e[4], m.cmRatio);
        check(test, "operating leverage", e[5], m.leverage);
        check(test, "profit", e[6], m.profit);
    }

    public static void main(String[] args) {
        {
            Product m = new Product();
            m.name = "Northwood basketballs (textbook)";
            m.production = 60000;
            m.sales = 60000;
            m.strategy = "Fixed Price";
            m.strategyValue = 25;
            m.directCosts.add(new CostItem("Variable expenses", 60000, "pcs", 15));
            m.indirectCosts.add(new CostItem("Fixed expenses", 1, "pack", 375000));
            run("T1", m, true, new double[]{21.25, 25.0, 37500.0, 10.0, 0.4, 2.67, 225000.0});
        }
        {
            Product m = new Product();
            m.name = "Unit CM ₱20 on ₱50 price, fixed ₱12,000 (calculator example)";
            m.production = 1000;
            m.sales = 1000;
            m.strategy = "Fixed Price";
            m.strategyValue = 50;
            m.directCosts.add(new CostItem("Variable costs", 1000, "pcs", 30));
            m.indirectCosts.add(new CostItem("Fixed costs", 1, "pack", 12000));
            run("T2", m, true, new double[]{42.0, 50.0, 600.0, 20.0, 0.4, 2.5, 8000.0});
        }
        {
            Product m = new Product();
            m.name = "Operating leverage of 4 (textbook-style)";
            m.production = 10000;
            m.sales = 10000;
            m.strategy = "Fixed Price";
            m.strategyValue = 100;
            m.directCosts.add(new CostItem("Variable costs", 10000, "pcs", 60));
            m.indirectCosts.add(new CostItem("Fixed costs", 1, "pack", 300000));
            run("T3", m, true, new double[]{90.0, 100.0, 7500.0, 40.0, 0.4, 4.0, 100000.0});
        }
        {
            Product m = new Product();
            m.name = "Pandesal batch";
            m.production = 500;
            m.sales = 450;
            m.strategy = "Cost Plus";
            m.strategyValue = 40;
            m.directCosts.add(new CostItem("Flour", 25, "kg", 38));
            m.directCosts.add(new CostItem("Yeast", 0.5, "kg", 320));
            m.directCosts.add(new CostItem("Sugar", 3, "kg", 62));
            m.directCosts.add(new CostItem("Cooking oil", 4, "l", 110));
            m.indirectCosts.add(new CostItem("Baker wages", 1, "pack", 1500));
            m.indirectCosts.add(new CostItem("LPG", 2, "can", 950));
            m.indirectCosts.add(new CostItem("Stall rent", 1, "pack", 3000));
            run("M1", m, true, new double[]{16.27, 22.78, 332.0, 19.31, 0.8477, 3.8, 2288.6});
        }
        {
            Product m = new Product();
            m.name = "Calamansi juice bottles";
            m.production = 300;
            m.sales = 225;
            m.strategy = "Fixed Price";
            m.strategyValue = 35;
            m.directCosts.add(new CostItem("Calamansi", 30, "kg", 70));
            m.directCosts.add(new CostItem("Sugar", 8, "kg", 62));
            m.directCosts.add(new CostItem("Bottles", 300, "pcs", 4.5));
            m.directCosts.add(new CostItem("Labels", 300, "pcs", 1.25));
            m.indirectCosts.add(new CostItem("Permit", 1, "pack", 1200));
            m.indirectCosts.add(new CostItem("Delivery", 1, "pack", 800));
            run("M2", m, true, new double[]{21.07, 35.0, 98.0, 20.6, 0.5886, 1.76, 2634.25});
        }
        {
            Product m = new Product();
            m.name = "Custom T-shirt print";
            m.production = 120;
            m.sales = 120;
            m.strategy = "Cost Plus";
            m.strategyValue = 60;
            m.directCosts.add(new CostItem("Blank shirt", 120, "pcs", 85));
            m.directCosts.add(new CostItem("Ink", 0.3, "l", 1800));
            m.indirectCosts.add(new CostItem("Printer depreciation", 1, "pack", 2400));
            m.indirectCosts.add(new CostItem("Workspace", 1, "pack", 1500));
            run("M3", m, true, new double[]{122.0, 195.2, 37.0, 105.7, 0.5415, 1.44, 8784.0});
        }
        {
            Product m = new Product();
            m.name = "Rounding tie (201 / 200 units)";
            m.production = 200;
            m.sales = 100;
            m.strategy = "Cost Plus";
            m.strategyValue = 0;
            m.directCosts.add(new CostItem("Item", 201, "pcs", 1));
            m.indirectCosts.add(new CostItem("Overhead", 1, "pack", 100));
            run("M4", m, true, new double[]{1.51, 1.51, 199.0, 0.51, 0.3377, 1.04, -49.5});
        }
        {
            Product m = new Product();
            m.name = "Handmade soap (price too low)";
            m.production = 80;
            m.sales = 60;
            m.strategy = "Fixed Price";
            m.strategyValue = 12;
            m.directCosts.add(new CostItem("Oils", 4, "kg", 300));
            m.directCosts.add(new CostItem("Lye", 0.5, "kg", 250));
            m.indirectCosts.add(new CostItem("Molds", 1, "pack", 800));
            run("M5", m, true, new double[]{26.56, 12.0, -175.0, -4.56, -0.38, 0.25, -1073.75});
        }
        {
            Product m = new Product();
            m.name = "Zero margin (price = variable cost)";
            m.production = 100;
            m.sales = 50;
            m.strategy = "Cost Plus";
            m.strategyValue = 0;
            m.directCosts.add(new CostItem("Item", 100, "pcs", 5));
            run("M6", m, false, null);
        }
        {
            Product m = new Product();
            m.name = "Cake with math quantities";
            m.production = 12;
            m.sales = 12;
            m.strategy = "Cost Plus";
            m.strategyValue = 35;
            m.directCosts.add(new CostItem("Flour", 0.75, "kg", 52.5));
            m.directCosts.add(new CostItem("Butter", 3, "pack", 78.25));
            m.directCosts.add(new CostItem("Eggs", 12, "pcs", 8.5));
            m.indirectCosts.add(new CostItem("Electricity", 1, "pack", 180));
            run("M7", m, true, new double[]{46.34, 62.56, 6.0, 31.22, 0.499, 1.92, 194.595});
        }
        {
            Product m = new Product();
            m.name = "Zero sales forecast";
            m.production = 100;
            m.sales = 0;
            m.strategy = "Cost Plus";
            m.strategyValue = 20;
            m.directCosts.add(new CostItem("Item", 100, "pcs", 5));
            m.indirectCosts.add(new CostItem("Rent", 1, "pack", 2000));
            run("M8", m, true, new double[]{25.0, 30.0, 80.0, 25.0, 0.8333, 0.0, -2000.0});
        }

        System.out.println(pass + " checks passed, " + fail + " failed");
    }
}
