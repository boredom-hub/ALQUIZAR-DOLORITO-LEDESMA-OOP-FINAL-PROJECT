# Product Pricing Calculator

A Java Swing desktop app that helps small businesses work out the **selling price, break-even point and profit** of a product. You enter the product details and costs, and the app walks you through the results step by step, ending with a profit chart.

Prices and amounts are shown in Philippine pesos (₱).

## Features

- **Register / Login** screens (accounts are kept in memory only, so they reset when the app closes)
- **4-step wizard**
  1. **Product Details**: product name, pricing strategy, production quantity and expected sales quantity
  2. **Costs**: add, edit, duplicate and delete direct and indirect cost items
  3. **Results**: unit cost, recommended price, break-even point, profit at expected sales, contribution margin ratio and operating leverage
  4. **Chart**: sales revenue, total cost, profit, variable cost and fixed cost plotted by quantity, with the margin of safety shaded and hover tooltips
- **Two pricing strategies**
  - *Cost Plus*: unit cost plus a markup percentage (slider or typed)
  - *Fixed Price*: you set the selling price directly
- **Cost items** with a quantity, a unit type (weight, volume, count, packaging) and a cost per unit. Quantities accept simple math such as `3/4` or `2*(1+0.5)`
- **Info (i) buttons** that explain each metric and show the formula worked out with your own numbers
- Custom dark theme drawn with Java2D, so no image files or external libraries are needed

## How the numbers work

| Metric | Formula |
| --- | --- |
| Unit cost | (Total direct costs + Total indirect costs) / Production quantity |
| Price (Cost Plus) | Unit cost x (1 + Markup % / 100) |
| Price (Fixed Price) | The price you enter |
| Break-even point | Fixed costs / (Price - Variable cost per unit), rounded up |
| Contribution margin ratio | (Price - Variable cost per unit) / Price |
| Operating leverage | Total contribution margin / (Total contribution margin - Fixed costs) |
| Profit at expected sales | (Price x Sales) - (Fixed costs + Variable cost per unit x Sales) |

Direct costs are treated as variable costs and indirect costs as fixed costs.

## Requirements

- Java Development Kit (JDK) 8 or newer (built and tested with JDK 21)
- No external libraries

## Build and run

From the folder containing the `.java` files:

```bash
javac *.java
java MainFrame
```

Or open the folder in any Java IDE (IntelliJ IDEA, Eclipse, NetBeans, VS Code) and run `MainFrame.java`.

## Project structure

| Area | Files |
| --- | --- |
| Entry and accounts | `MainFrame`, `Login`, `Register` |
| Wizard steps | `StepFrame` (shared window shell), `Calculator`, `CostsFrame`, `MetricsFrame`, `ChartFrame` |
| Product Details sections | `Cost`, `Pricing`, `Production`, `Sales` |
| Costs screen | `CostPanel`, `CostDialog`, `CostList`, `CostItem`, `CostType`, `UnitType` |
| Pricing logic | `PricingStrategy` (interface), `CostPlusStrategy`, `FixedPriceStrategy`, `FinancialAnalyzer`, `AnalysisResult`, `ProductPricingModel` |
| Helpers | `Util` (formatting and rounding), `ExpressionParser` (quantity math), `SimpleDocListener` |
| Chart | `ProfitChartPanel` |
| UI components | `UiKit`, `Theme`, `Icons`, `RoundedPanel`, `RoundedButton`, `RoundedTextField`, `RoundedPasswordField`, `InfoButton`, `EndCap`, `ScrollPage`, `NumberFilter`, `DarkSliderUI`, `DarkComboBoxUI`, `DarkListRenderer`, `DarkScrollBarUI` |

## Java concepts used

Classes and objects, inheritance (`extends JFrame`, abstract `StepFrame`), interfaces and polymorphism (`PricingStrategy`), enums, `ArrayList`, encapsulation with getters and setters, lambdas, inner classes, exception handling and Swing layout managers.

## Authors

- Alquizar, John Dominiuqe |
- Dolorito, Vhelle Anthon |
- Ledesma, Akinna |
