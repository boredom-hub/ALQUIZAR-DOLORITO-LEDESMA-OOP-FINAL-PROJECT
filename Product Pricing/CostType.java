public enum CostType {

    DIRECT("Direct Costs", "Direct Cost",
            "Costs that are directly related to the production of the product.",
            "Costs directly associated with product production (e.g., materials, ingredients)",
            "Direct costs are expenses that can be directly attributed to the production of specific goods or "
            + "services. These costs typically vary with production volume. Examples include:\n"
            + "- Raw materials (e.g., wood for furniture, fabric for clothing)\n"
            + "- Manufacturing supplies (e.g., nails, glue, packaging materials)\n"
            + "- Commissions tied to specific sales"),

    INDIRECT("Indirect Costs", "Indirect Cost",
            "Costs that are not directly tied to the production of the product.",
            "Overhead costs not directly tied to production (e.g., rent, utilities)",
            "Indirect costs are overhead expenses that support the overall business but are not directly tied to "
            + "producing specific products or services. These costs often remain relatively constant regardless "
            + "of production volume. Examples include:\n"
            + "- Rent or mortgage for facilities\n"
            + "- Utilities (electricity, water, internet)\n"
            + "- Administrative staff salaries\n"
            + "- Marketing and advertising expenses\n"
            + "- Insurance premiums\n"
            + "- Equipment depreciation");

    private final String title;
    private final String itemName;
    private final String subtitle;
    private final String emptyText;
    private final String info;

    CostType(String title, String itemName, String subtitle, String emptyText, String info) {
        this.title = title;
        this.itemName = itemName;
        this.subtitle = subtitle;
        this.emptyText = emptyText;
        this.info = info;
    }

    public String getTitle() {
        return title;
    }

    public String getItemName() {
        return itemName;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getEmptyText() {
        return emptyText;
    }

    public String getInfo() {
        return info;
    }
}
