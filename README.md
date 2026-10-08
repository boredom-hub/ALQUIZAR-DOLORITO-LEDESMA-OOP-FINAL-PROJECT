# Product Pricing Calculator

A Java Swing desktop app that works out the **selling price, break-even point and profit** of a product. Amounts are in Philippine pesos (₱). Plain Swing only (no external libraries).

## Features

- Register (multiple accounts) and Login
- 4 steps: **Product Details** > **Costs** > **Results** > **Chart**
- Two pricing strategies: **Cost Plus** (markup %) and **Fixed Price**
- Direct and indirect costs: add, edit, duplicate, delete. Quantities accept simple math like `3/4` or `2*(1+0.5)`
- Results show the unit cost, recommended price, break-even point, profit at expected sales, contribution margin ratio and operating leverage, each with its formula and your own numbers
- Profit chart (revenue, total cost, profit, variable cost, fixed cost) with the margin of safety shaded

## Formulas

| Metric | Formula |
| --- | --- |
| Unit cost | (Direct + Indirect costs) / Production quantity |
| Price (Cost Plus) | Unit cost x (1 + Markup % / 100) |
| Break-even point | Fixed costs / (Price - Variable cost per unit), rounded up |
| Contribution margin ratio | (Price - Variable cost per unit) / Price |
| Operating leverage | Total contribution margin / (Total contribution margin - Fixed costs) |
| Profit | (Price x Sales) - (Fixed costs + Variable cost per unit x Sales) |

Direct costs are variable costs and indirect costs are fixed costs.

## Run it

Needs JDK 8 or newer.

```bash
javac *.java
java MainFrame
```

To run the automatic checks: `java AnalyzerTest` (prints `71 checks passed, 0 failed`).

## Files

| File | What it does |
| --- | --- |
| `MainFrame`, `Register`, `Login` | start screen and accounts |
| `Calculator` | step 1, product details |
| `CostsFrame` | step 2, costs (tables and the add / edit popup) |
| `ResultsFrame` | step 3, results |
| `ChartFrame` | step 4, chart |
| `Product`, `CostItem` | the data and the pricing math |
| `Util`, `ExpressionParser` | formatting / rounding and the quantity math |
| `AnalyzerTest` | test cases for the math |

## Authors

* Alquizar, John Dominique
* Dolorito, Vhelle Anthon
* Ledesma, Akinna
