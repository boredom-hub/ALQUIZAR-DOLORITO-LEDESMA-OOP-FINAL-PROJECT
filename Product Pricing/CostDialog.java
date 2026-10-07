import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class CostDialog extends JDialog {

    private RoundedTextField txtName = new RoundedTextField(20);
    private RoundedTextField txtQuantity = new RoundedTextField(8);
    private RoundedTextField txtUnitCost = new RoundedTextField(8);
    private JComboBox<Object> cboUnit = new JComboBox<Object>();
    private JLabel lblPreview = UiKit.label(" ", Font.PLAIN, 11, Theme.FAINT);
    private JLabel lblCostPer = UiKit.label("Cost per kg", Font.BOLD, 13, Theme.TEXT);
    private JLabel errName = errorLabel();
    private JLabel errQuantity = errorLabel();
    private JLabel errUnitCost = errorLabel();

    private CostItem result = null;

    private CostDialog(Window owner, CostType type, CostItem existing) {
        super(owner, existing == null ? "Add New " + type.getItemName() : "Edit " + type.getItemName(),
                Dialog.ModalityType.APPLICATION_MODAL);
        String itemLabel = type.getItemName();
        buildUnitCombo();

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Theme.BG);
        content.setBorder(BorderFactory.createEmptyBorder(22, 24, 22, 24));

        if (existing == null) {
            content.add(left(UiKit.label("Add New " + itemLabel, Font.BOLD, 17, Theme.TEXT)));
        } else {
            content.add(left(UiKit.label("Edit " + itemLabel, Font.BOLD, 17, Theme.TEXT)));
        }
        content.add(Box.createVerticalStrut(4));
        if (existing == null) {
            content.add(left(UiKit.label("Enter the details for the new " + itemLabel.toLowerCase() + ".",
                    Font.PLAIN, 12, Theme.MUTED)));
        } else {
            content.add(left(UiKit.label("Change the details of this " + itemLabel.toLowerCase() + ".",
                    Font.PLAIN, 12, Theme.MUTED)));
        }
        content.add(Box.createVerticalStrut(16));

        content.add(left(UiKit.label(itemLabel, Font.BOLD, 13, Theme.TEXT)));
        content.add(Box.createVerticalStrut(6));
        content.add(left(txtName));
        content.add(left(errName));
        content.add(Box.createVerticalStrut(10));

        content.add(left(buildQuantityRow()));
        content.add(left(errQuantity));
        content.add(Box.createVerticalStrut(10));

        content.add(left(lblCostPer));
        content.add(Box.createVerticalStrut(6));
        JPanel costRow = UiKit.transparent(new BorderLayout(8, 0));
        costRow.add(UiKit.label(Util.PESO, Font.BOLD, 15, Theme.MUTED), BorderLayout.WEST);
        costRow.add(txtUnitCost, BorderLayout.CENTER);
        content.add(left(costRow));
        content.add(left(errUnitCost));
        content.add(Box.createVerticalStrut(18));

        RoundedButton submit;
        if (existing == null) {
            submit = UiKit.primaryButton("Add " + itemLabel, null);
        } else {
            submit = UiKit.primaryButton("Save Changes", null);
        }
        submit.addActionListener(e -> save());
        content.add(left(submit));
        submit.setMaximumSize(new Dimension(Integer.MAX_VALUE, submit.getPreferredSize().height));

        UiKit.limitToNumber(txtUnitCost, true);
        wireEvents();
        fill(existing);

        setContentPane(content);
        getRootPane().setDefaultButton(submit);
        getRootPane().registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        setResizable(false);
        pack();
        setSize(Math.max(getWidth(), 440), getHeight());
        setLocationRelativeTo(owner);
    }

    public static CostItem ask(Component parent, CostType type, CostItem existing) {
        Window owner = SwingUtilities.getWindowAncestor(parent);
        CostDialog dialog = new CostDialog(owner, type, existing);
        dialog.setVisible(true);
        return dialog.result;
    }

    private JComponent buildQuantityRow() {
        JPanel row = UiKit.transparent(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.NORTH;
        g.weightx = 0.5;

        g.gridx = 0;
        g.gridy = 0;
        g.insets = new Insets(0, 0, 6, 8);
        row.add(UiKit.label("Quantity", Font.BOLD, 13, Theme.TEXT), g);
        g.gridx = 1;
        g.insets = new Insets(0, 8, 6, 0);
        row.add(UiKit.label("Unit Type", Font.BOLD, 13, Theme.TEXT), g);

        g.gridx = 0;
        g.gridy = 1;
        g.insets = new Insets(0, 0, 0, 8);
        JPanel qty = UiKit.transparent(new BorderLayout(0, 3));
        qty.add(txtQuantity, BorderLayout.NORTH);
        qty.add(lblPreview, BorderLayout.CENTER);
        row.add(qty, g);
        g.gridx = 1;
        g.insets = new Insets(0, 8, 0, 0);
        row.add(UiKit.comboBox(cboUnit, new UnitRenderer()), g);
        return row;
    }

    private void buildUnitCombo() {
        DefaultComboBoxModel<Object> unitModel = new DefaultComboBoxModel<Object>() {
            public void setSelectedItem(Object item) {
                if (item instanceof UnitType) {
                    super.setSelectedItem(item);
                }
            }
        };
        String category = "";
        for (UnitType u : UnitType.values()) {
            if (!u.getCategory().equals(category)) {
                category = u.getCategory();
                unitModel.addElement(category);
            }
            unitModel.addElement(u);
        }
        cboUnit.setModel(unitModel);
        unitModel.setSelectedItem(UnitType.values()[0]);
    }

    private static JLabel errorLabel() {
        JLabel l = UiKit.label(" ", Font.PLAIN, 11, Theme.RED);
        l.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
        return l;
    }

    private static JComponent left(JComponent c) {
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        return c;
    }

    private void wireEvents() {
        cboUnit.addActionListener(e -> lblCostPer.setText("Cost per " + selectedUnit().getCode()));
        txtQuantity.getDocument().addDocumentListener(new SimpleDocListener(this::updatePreview));
    }

    private UnitType selectedUnit() {
        Object o = cboUnit.getSelectedItem();
        if (o instanceof UnitType) {
            return (UnitType) o;
        }
        return UnitType.values()[0];
    }

    private void updatePreview() {
        String text = txtQuantity.getText().trim();
        double v = ExpressionParser.evaluate(text);
        if (Double.isNaN(v)) {
            lblPreview.setText(" ");
            return;
        }
        boolean plainNumber = text.matches("\\d*\\.?\\d*");
        if (plainNumber) {
            lblPreview.setText(" ");
        } else {
            lblPreview.setText("= " + Util.num(Util.round(v, 4)));
        }
    }

    private void fill(CostItem existing) {
        if (existing == null) {
            txtQuantity.setText("1");
            txtUnitCost.setText("0");
            cboUnit.setSelectedItem(UnitType.values()[0]);
        } else {
            txtName.setText(existing.getName());
            txtQuantity.setText(Util.num(existing.getQuantity()));
            txtUnitCost.setText(Util.num(existing.getUnitCost()));
            cboUnit.setSelectedItem(existing.getUnit());
        }
        lblCostPer.setText("Cost per " + selectedUnit().getCode());
    }

    private void save() {
        errName.setText(" ");
        errQuantity.setText(" ");
        errUnitCost.setText(" ");
        boolean ok = true;

        String name = txtName.getText().trim();
        if (name.isEmpty()) {
            errName.setText("Item name is required");
            ok = false;
        }

        double quantity = ExpressionParser.evaluate(txtQuantity.getText());
        if (Double.isNaN(quantity)) {
            errQuantity.setText("Enter a number, or simple math like 3/4");
            ok = false;
        } else if (quantity < 0) {
            errQuantity.setText("Quantity must be a non-negative number");
            ok = false;
        }

        double unitCost = 0;
        try {
            unitCost = Double.parseDouble(txtUnitCost.getText().trim());
        } catch (NumberFormatException e) {
            errUnitCost.setText("Unit cost must be a non-negative number");
            ok = false;
        }

        if (ok) {
            result = new CostItem(name, Util.round(quantity, 6), selectedUnit(), unitCost);
            dispose();
        }
    }

    private class UnitRenderer extends DarkListRenderer {
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean selected, boolean focus) {
            boolean heading = value instanceof String;
            JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, selected && !heading, focus);
            if (heading) {
                l.setText(((String) value).toUpperCase());
                l.setFont(Theme.font(Font.BOLD, 11));
                l.setForeground(Theme.FAINT);
                l.setBorder(BorderFactory.createEmptyBorder(8, 12, 3, 12));
            } else if (index >= 0) {
                l.setBorder(BorderFactory.createEmptyBorder(6, 22, 6, 12));
            }
            return l;
        }
    }
}
