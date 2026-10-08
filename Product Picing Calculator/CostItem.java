public class CostItem {

    public static String[] UNIT_CODES = {"kg", "g", "mg", "lb", "oz", "l", "ml", "fl_oz", "cup", "tbsp", "tsp",
            "pcs", "dozen", "pack", "box", "bag", "can", "bottle", "jar"};
    public static String[] UNIT_LABELS = {"Weight - Kilograms (kg)", "Weight - Grams (g)",
            "Weight - Milligrams (mg)", "Weight - Pounds (lb)", "Weight - Ounces (oz)",
            "Volume - Liters (L)", "Volume - Milliliters (mL)", "Volume - Fluid Ounces (fl oz)",
            "Volume - Cups", "Volume - Tablespoons", "Volume - Teaspoons",
            "Count - Pieces", "Count - Dozen",
            "Packaging - Pack", "Packaging - Box", "Packaging - Bag", "Packaging - Can",
            "Packaging - Bottle", "Packaging - Jar"};

    public String name;
    public double quantity;
    public String unit;       
    public double unitCost;  

    public CostItem(String name, double quantity, String unit, double unitCost) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.unitCost = unitCost;
    }

    public double getTotal() {
        return quantity * unitCost;
    }

    public CostItem copy() {
        return new CostItem(name, quantity, unit, unitCost);
    }

    public static int indexOfUnit(String code) {
        for (int i = 0; i < UNIT_CODES.length; i++) {
            if (UNIT_CODES[i].equals(code)) {
                return i;
            }
        }
        return 0;
    }
}
