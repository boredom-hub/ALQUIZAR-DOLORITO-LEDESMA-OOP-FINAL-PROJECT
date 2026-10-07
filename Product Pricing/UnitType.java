public enum UnitType {
    KG("Weight", "kg", "Kilograms (kg)"),
    G("Weight", "g", "Grams (g)"),
    MG("Weight", "mg", "Milligrams (mg)"),
    LB("Weight", "lb", "Pounds (lb)"),
    OZ("Weight", "oz", "Ounces (oz)"),

    L("Volume", "l", "Liters (L)"),
    ML("Volume", "ml", "Milliliters (mL)"),
    FL_OZ("Volume", "fl_oz", "Fluid Ounces (fl oz)"),
    CUP("Volume", "cup", "Cups"),
    TBSP("Volume", "tbsp", "Tablespoons"),
    TSP("Volume", "tsp", "Teaspoons"),

    PCS("Count", "pcs", "Pieces"),
    DOZEN("Count", "dozen", "Dozen"),

    PACK("Packaging", "pack", "Pack"),
    BOX("Packaging", "box", "Box"),
    BAG("Packaging", "bag", "Bag"),
    CAN("Packaging", "can", "Can"),
    BOTTLE("Packaging", "bottle", "Bottle"),
    JAR("Packaging", "jar", "Jar");

    private final String category;
    private final String code;
    private final String label;

    UnitType(String category, String code, String label) {
        this.category = category;
        this.code = code;
        this.label = label;
    }

    public String getCategory() {
        return category;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return label;
    }
}
