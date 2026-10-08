import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.GridLayout;
import java.util.ArrayList;

public class CostsFrame {

    JFrame frame;
    Product product;

    String directInfo = "Direct costs are expenses that can be directly attributed to the production of specific goods or "
            + "services. These costs typically vary with production volume. Examples include:\n"
            + "- Raw materials (e.g., wood for furniture, fabric for clothing)\n"
            + "- Manufacturing supplies (e.g., nails, glue, packaging materials)\n"
            + "- Commissions tied to specific sales";

    String indirectInfo = "Indirect costs are overhead expenses that support the overall business but are not directly tied to "
            + "producing specific products or services. These costs often remain relatively constant regardless "
            + "of production volume. Examples include:\n"
            + "- Rent or mortgage for facilities\n"
            + "- Utilities (electricity, water, internet)\n"
            + "- Administrative staff salaries\n"
            + "- Marketing and advertising expenses\n"
            + "- Insurance premiums\n"
            + "- Equipment depreciation";

    public CostsFrame(Product product) {
        this.product = product;

        frame = new JFrame("Costs");
        frame.setSize(660, 610);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        new CostSection("Direct Costs", "Direct Cost", directInfo, product.directCosts, 10);
        new CostSection("Indirect Costs", "Indirect Cost", indirectInfo, product.indirectCosts, 255);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(20, 515, 150, 35);

        JButton btnNext = new JButton("Next: Results");
        btnNext.setBounds(470, 515, 150, 35);

        btnBack.addActionListener(e -> {
            frame.dispose();
            new Calculator(product);
        });
        btnNext.addActionListener(e -> next());

        frame.add(btnBack);
        frame.add(btnNext);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void next() {
        if (!product.hasAnyCosts()) {
            JOptionPane.showMessageDialog(frame, "Add at least one direct or indirect cost to continue.",
                    "No costs yet", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        frame.dispose();
        new ResultsFrame(product);
    }

    private CostItem askItem(String itemName, CostItem existing) {
        JTextField txtName = new JTextField(20);
        JTextField txtQuantity = new JTextField("1");
        JComboBox<String> cboUnit = new JComboBox<String>(CostItem.UNIT_LABELS);
        JTextField txtCost = new JTextField("0");

        if (existing != null) {
            txtName.setText(existing.name);
            txtQuantity.setText(Util.num(existing.quantity));
            cboUnit.setSelectedIndex(CostItem.indexOfUnit(existing.unit));
            txtCost.setText(Util.num(existing.unitCost));
        }

        JPanel form = new JPanel(new GridLayout(0, 1, 0, 4));
        form.add(new JLabel(itemName + ":"));
        form.add(txtName);
        form.add(new JLabel("Quantity (you can type things like 3/4 or 2*3):"));
        form.add(txtQuantity);
        form.add(new JLabel("Unit type:"));
        form.add(cboUnit);
        form.add(new JLabel("Cost per unit (" + Util.PESO + "):"));
        form.add(txtCost);

        String title = "Add New " + itemName;
        if (existing != null) {
            title = "Edit " + itemName;
        }

        while (true) {
            int choice = JOptionPane.showConfirmDialog(frame, form, title,
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (choice != JOptionPane.OK_OPTION) {
                return null;
            }

            String name = txtName.getText().trim();
            double quantity = ExpressionParser.evaluate(txtQuantity.getText());
            double cost = -1;
            try {
                cost = Double.parseDouble(txtCost.getText().trim());
            } catch (NumberFormatException e) {
                cost = -1;
            }

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Item name is required");
            } else if (Double.isNaN(quantity)) {
                JOptionPane.showMessageDialog(frame, "Enter a number, or simple math like 3/4");
            } else if (quantity < 0) {
                JOptionPane.showMessageDialog(frame, "Quantity must be a non-negative number");
            } else if (Double.isNaN(cost) || Double.isInfinite(cost) || cost < 0) {
                JOptionPane.showMessageDialog(frame, "Unit cost must be a non-negative number");
            } else {
                String unit = CostItem.UNIT_CODES[cboUnit.getSelectedIndex()];
                return new CostItem(name, Util.round(quantity, 6), unit, cost);
            }
        }
    }

    class CostSection {

        String itemName;
        ArrayList<CostItem> items;
        DefaultTableModel tableModel;
        JTable table;
        JLabel lblTotal;

        CostSection(String title, String itemName, String info, ArrayList<CostItem> items, int y) {
            this.itemName = itemName;
            this.items = items;

            JLabel lblTitle = new JLabel(title);
            lblTitle.setBounds(20, y, 150, 25);

            JButton btnInfo = new JButton("?");
            btnInfo.setBounds(165, y, 45, 25);
            btnInfo.addActionListener(e -> JOptionPane.showMessageDialog(frame, info, title,
                    JOptionPane.INFORMATION_MESSAGE));

            JButton btnAdd = new JButton("+ Add");
            btnAdd.setBounds(520, y, 100, 25);
            btnAdd.addActionListener(e -> addItem());

            String[] columns = {"Item", "Qty", "Unit", "Cost / unit", "Total"};
            // the table is read only, you edit through the popup
            tableModel = new DefaultTableModel(columns, 0) {
                public boolean isCellEditable(int row, int col) {
                    return false;
                }
            };
            table = new JTable(tableModel);
            table.setRowHeight(22);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            JScrollPane scroll = new JScrollPane(table);
            scroll.setBounds(20, y + 32, 600, 120);

            JButton btnEdit = new JButton("Edit");
            btnEdit.setBounds(20, y + 162, 80, 30);
            JButton btnDuplicate = new JButton("Duplicate");
            btnDuplicate.setBounds(105, y + 162, 115, 30);
            JButton btnDelete = new JButton("Delete");
            btnDelete.setBounds(225, y + 162, 95, 30);
            btnEdit.addActionListener(e -> editItem());
            btnDuplicate.addActionListener(e -> duplicateItem());
            btnDelete.addActionListener(e -> deleteItem());

            lblTotal = new JLabel();
            lblTotal.setBounds(380, y + 162, 240, 30);
            lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);

            frame.add(lblTitle);
            frame.add(btnInfo);
            frame.add(btnAdd);
            frame.add(scroll);
            frame.add(btnEdit);
            frame.add(btnDuplicate);
            frame.add(btnDelete);
            frame.add(lblTotal);

            refresh(-1);
        }

        void refresh(int rowToSelect) {
            tableModel.setRowCount(0);
            double total = 0;
            for (CostItem item : items) {
                tableModel.addRow(new Object[]{
                        item.name,
                        Util.num(item.quantity),
                        item.unit,
                        Util.peso(item.unitCost),
                        Util.peso(item.getTotal())
                });
                total = total + item.getTotal();
            }
            lblTotal.setText("Total: " + Util.peso(total));
            if (rowToSelect >= 0 && rowToSelect < tableModel.getRowCount()) {
                table.setRowSelectionInterval(rowToSelect, rowToSelect);
            }
        }

        void addItem() {
            CostItem item = askItem(itemName, null);
            if (item != null) {
                items.add(item);
                refresh(items.size() - 1);
            }
        }

        void editItem() {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(frame, "Select an item in the table first.");
                return;
            }
            CostItem item = askItem(itemName, items.get(row));
            if (item != null) {
                items.set(row, item);
                refresh(row);
            }
        }

        void duplicateItem() {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(frame, "Select an item in the table first.");
                return;
            }
            items.add(row + 1, items.get(row).copy());
            refresh(row + 1);
        }

        void deleteItem() {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(frame, "Select an item in the table first.");
                return;
            }
            items.remove(row);
            refresh(-1);
        }
    }
}
