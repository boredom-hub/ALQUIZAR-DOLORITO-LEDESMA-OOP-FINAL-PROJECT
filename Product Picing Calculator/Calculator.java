import javax.swing.*;
import java.util.ArrayList;

public class Calculator {

    JFrame frame;
    Product product;
    JTextField txtName;
    JComboBox<String> cboStrategy;
    JLabel lblValue;
    JTextField txtValue;
    JTextField txtProduction;
    JTextField txtSales;

    public Calculator() {
        this(new Product());
    }

    public Calculator(Product product) {
        this.product = product;

        frame = new JFrame("Product Details");
        frame.setSize(560, 400);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblStep = new JLabel("Step 1 of 4 - Product Details");
        lblStep.setBounds(0, 10, 550, 30);
        lblStep.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblName = new JLabel("Product Name: ");
        lblName.setBounds(30, 55, 180, 30);
        txtName = new JTextField();
        txtName.setBounds(220, 55, 300, 30);

        JLabel lblStrategy = new JLabel("Pricing Strategy: ");
        lblStrategy.setBounds(30, 100, 180, 30);
        cboStrategy = new JComboBox<String>(new String[]{"Cost Plus", "Fixed Price"});
        cboStrategy.setBounds(220, 100, 300, 30);

        lblValue = new JLabel("Cost Plus markup (%): ");
        lblValue.setBounds(30, 145, 180, 30);
        txtValue = new JTextField("0");
        txtValue.setBounds(220, 145, 300, 30);

        JLabel lblProduction = new JLabel("Production Quantity: ");
        lblProduction.setBounds(30, 190, 180, 30);
        txtProduction = new JTextField("0");
        txtProduction.setBounds(220, 190, 300, 30);

        JLabel lblSales = new JLabel("Sales Quantity: ");
        lblSales.setBounds(30, 235, 180, 30);
        txtSales = new JTextField("0");
        txtSales.setBounds(220, 235, 70, 30);

        JButton btn50 = new JButton("50%");
        btn50.setBounds(295, 235, 70, 30);
        JButton btn75 = new JButton("75%");
        btn75.setBounds(370, 235, 70, 30);
        JButton btn100 = new JButton("100%");
        btn100.setBounds(445, 235, 75, 30);

        JButton btnBack = new JButton("Log out");
        btnBack.setBounds(30, 300, 150, 35);

        JButton btnNext = new JButton("Next: Costs");
        btnNext.setBounds(370, 300, 150, 35);

        cboStrategy.addActionListener(e -> strategyChanged());
        btn50.addActionListener(e -> setSalesPercent(50));
        btn75.addActionListener(e -> setSalesPercent(75));
        btn100.addActionListener(e -> setSalesPercent(100));

        btnBack.addActionListener(e -> {
            frame.dispose();
            new MainFrame();
        });
        btnNext.addActionListener(e -> next());

        frame.add(lblStep);
        frame.add(lblName);
        frame.add(txtName);
        frame.add(lblStrategy);
        frame.add(cboStrategy);
        frame.add(lblValue);
        frame.add(txtValue);
        frame.add(lblProduction);
        frame.add(txtProduction);
        frame.add(lblSales);
        frame.add(txtSales);
        frame.add(btn50);
        frame.add(btn75);
        frame.add(btn100);
        frame.add(btnBack);
        frame.add(btnNext);

        loadFromProduct();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

  
    private void loadFromProduct() {
        txtName.setText(product.name);
        txtProduction.setText(String.valueOf(product.production));
        txtSales.setText(String.valueOf(product.sales));
        cboStrategy.setSelectedItem(product.strategy);
        txtValue.setText(Util.num(product.strategyValue));
    }


    private void strategyChanged() {
        if (cboStrategy.getSelectedItem().equals("Fixed Price")) {
            lblValue.setText("Fixed Price (" + Util.PESO + "): ");
        } else {
            lblValue.setText("Cost Plus markup (%): ");
        }
        txtValue.setText("0");
    }


    private void setSalesPercent(int percent) {
        int total;
        try {
            total = Integer.parseInt(txtProduction.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Enter the production quantity first.");
            return;
        }
        txtSales.setText(String.valueOf(Math.round(total * (percent / 100.0))));
    }


    private int parseWholeNumber(String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void next() {
        ArrayList<String> problems = new ArrayList<String>();

        String name = txtName.getText().trim();
        if (name.isEmpty()) {
            problems.add("Product name is required.");
        }

        int production = parseWholeNumber(txtProduction.getText());
        if (production <= 0) {
            problems.add("Production quantity must be a whole number greater than 0.");
        }

        int sales = parseWholeNumber(txtSales.getText());
        if (sales < 0) {
            problems.add("Sales quantity must be a whole number (0 or more).");
        } else if (production > 0 && sales > production) {
            problems.add("Sales quantity cannot exceed production quantity.");
        }

        boolean costPlus = cboStrategy.getSelectedItem().equals("Cost Plus");
        double value = -1;
        try {
            value = Double.parseDouble(txtValue.getText().trim());
        } catch (NumberFormatException e) {
            value = -1;
        }
        if (Double.isNaN(value) || Double.isInfinite(value) || value < 0) {
            if (costPlus) {
                problems.add("Cost plus markup must be a number of 0 or more.");
            } else {
                problems.add("Fixed price must be a number of 0 or more.");
            }
        }

        if (!problems.isEmpty()) {
            String text = "";
            for (String p : problems) {
                text = text + "- " + p + "\n";
            }
            JOptionPane.showMessageDialog(frame, text.trim(), "Please check your input",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }


        product.name = name;
        product.production = production;
        product.sales = sales;
        product.strategy = (String) cboStrategy.getSelectedItem();
        product.strategyValue = value;

        frame.dispose();
        new CostsFrame(product);
    }
}
