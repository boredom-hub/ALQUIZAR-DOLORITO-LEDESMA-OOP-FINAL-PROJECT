public class CostItem {

    private String name;
    private double quantity;
    private UnitType unit;
    private double unitCost; 

    public CostItem(String name, double quantity, UnitType unit, double unitCost) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.unitCost = unitCost;
    }

    public String getName() {
        return name;
    }

    public double getQuantity() {
        return quantity;
    }

    public UnitType getUnit() {
        return unit;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public void setQuantity(double quantity) {
        if (quantity < 0) {
            quantity = 0;
        }
        this.quantity = quantity;
    }

    public double getTotal() {
        return quantity * unitCost;
    }

    public CostItem copy() {
        return new CostItem(name, quantity, unit, unitCost);
    }
}
