import javax.swing.*;
import java.awt.*;

public class CostPanel extends JPanel {

    private CostList costs;
    private JPanel content = UiKit.transparent(new BorderLayout());

    public CostPanel(CostList costs) {
        super(new BorderLayout(0, 12));
        setOpaque(false);
        this.costs = costs;

        add(buildHeader(), BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
        rebuild();
    }

    private JComponent buildHeader() {
        CostType type = costs.getType();
        JPanel header = UiKit.transparent(new BorderLayout());

        JPanel titleRow = UiKit.labelRow(UiKit.label(type.getTitle(), Font.BOLD, 18, Theme.TEXT),
                new InfoButton(type.getTitle(), type.getInfo()));

        JPanel text = UiKit.transparent(new BorderLayout(0, 4));
        text.add(titleRow, BorderLayout.NORTH);
        text.add(UiKit.label(type.getSubtitle(), Font.PLAIN, 12, Theme.TEXT), BorderLayout.CENTER);

        header.add(text, BorderLayout.CENTER);
        JPanel east = UiKit.transparent(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        east.add(addButton());
        header.add(east, BorderLayout.EAST);
        return header;
    }

    private RoundedButton addButton() {
        RoundedButton add = UiKit.primaryButton("Add", Icons.of(Icons.Kind.PLUS, 14, Theme.ON_GREEN));
        add.addActionListener(e -> addNew());
        return add;
    }

    private JComponent emptyState() {
        RoundedPanel box = new RoundedPanel(new BorderLayout(), 14);
        box.setDashed(true);
        box.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        JPanel col = UiKit.transparent(null);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));

        JLabel icon = new JLabel(Icons.of(Icons.Kind.COINS, 34, Theme.MUTED));
        JLabel name = UiKit.label(costs.getType().getTitle(), Font.PLAIN, 13, Theme.TEXT);
        JLabel desc = new JLabel("<html><div style='width:340px;text-align:center'>"
                + UiKit.esc(costs.getType().getEmptyText()) + "</div></html>");
        desc.setFont(Theme.font(Font.PLAIN, 11));
        desc.setForeground(Theme.FAINT);
        RoundedButton add = addButton();

        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        name.setAlignmentX(Component.CENTER_ALIGNMENT);
        desc.setAlignmentX(Component.CENTER_ALIGNMENT);
        add.setAlignmentX(Component.CENTER_ALIGNMENT);

        col.add(icon);
        col.add(Box.createVerticalStrut(8));
        col.add(name);
        col.add(Box.createVerticalStrut(6));
        col.add(desc);
        col.add(Box.createVerticalStrut(14));
        col.add(add);
        box.add(col, BorderLayout.CENTER);
        return box;
    }

    private JComponent itemList() {
        RoundedPanel card = new RoundedPanel(new BorderLayout(), 14);
        card.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        JPanel rows = UiKit.transparent(null);
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));
        for (int i = 0; i < costs.size(); i++) {
            if (i > 0) {
                rows.add(UiKit.hLine());
            }
            rows.add(new CostRow(costs.get(i), i));
        }
        card.add(rows, BorderLayout.CENTER);
        return card;
    }

    private void rebuild() {
        content.removeAll();
        if (costs.isEmpty()) {
            content.add(emptyState(), BorderLayout.CENTER);
        } else {
            content.add(itemList(), BorderLayout.CENTER);
        }
        content.revalidate();
        content.repaint();
    }

    private void addNew() {
        CostItem item = CostDialog.ask(this, costs.getType(), null);
        if (item != null) {
            costs.add(item);
            rebuild();
        }
    }

    private class CostRow extends JPanel {
        private CostItem item;
        private int index;
        private RoundedButton qtyButton = new RoundedButton("", null, Theme.GREEN, Theme.BG,
                Theme.SURFACE, Theme.GREEN, 10, 0, 0);
        private JLabel lblName = UiKit.label("", Font.BOLD, 13, Theme.TEXT);
        private JLabel lblUnit = UiKit.label("", Font.PLAIN, 11, Theme.MUTED);

        private JLabel lblTotal = new JLabel() {
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                d.width = Math.max(d.width, 120);
                return d;
            }
        };

        CostRow(CostItem item, int index) {
            super(new BorderLayout(14, 0));
            this.item = item;
            this.index = index;
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

            qtyButton.setPreferredSize(new Dimension(52, 44));
            qtyButton.setFont(Theme.font(Font.BOLD, 13));
            qtyButton.addActionListener(e -> showQuantityPopup());
            add(qtyButton, BorderLayout.WEST);

            JPanel text = UiKit.transparent(new BorderLayout(0, 2));
            text.add(lblName, BorderLayout.CENTER);
            text.add(lblUnit, BorderLayout.SOUTH);
            add(text, BorderLayout.CENTER);

            add(buildActions(), BorderLayout.EAST);
            refresh();
        }

        private JComponent buildActions() {
            RoundedPanel group = new RoundedPanel(new GridBagLayout(), 10);
            group.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
            lblTotal.setFont(Theme.font(Font.PLAIN, 13));
            lblTotal.setForeground(Theme.TEXT);
            lblTotal.setHorizontalAlignment(SwingConstants.CENTER);
            lblTotal.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));

            RoundedButton edit = UiKit.iconButton(Icons.of(Icons.Kind.EDIT, 16, Theme.TEXT), "Edit");
            RoundedButton copy = UiKit.iconButton(Icons.of(Icons.Kind.COPY, 16, Theme.TEXT), "Duplicate");
            RoundedButton trash = UiKit.iconButton(Icons.of(Icons.Kind.TRASH, 16, Theme.TEXT), "Remove");
            edit.addActionListener(e -> editItem());
            copy.addActionListener(e -> {
                costs.duplicate(index);
                rebuild();
            });
            trash.addActionListener(e -> {
                costs.remove(index);
                rebuild();
            });

            GridBagConstraints g = new GridBagConstraints();
            g.fill = GridBagConstraints.VERTICAL;
            g.weighty = 1;
            Component[] parts = {lblTotal, UiKit.vLine(), edit, UiKit.vLine(), copy, UiKit.vLine(), trash};
            for (int i = 0; i < parts.length; i++) {
                g.gridx = i;
                group.add(parts[i], g);
            }
            return group;
        }

        private void refresh() {
            qtyButton.setText(Util.num(item.getQuantity()) + "x");
            lblName.setText(item.getName());
            lblUnit.setText(Util.peso(item.getUnitCost()) + "/" + item.getUnit().getCode());
            lblTotal.setText(Util.peso(item.getTotal()));
        }

        private void showQuantityPopup() {
            JPopupMenu popup = new JPopupMenu();
            popup.setBackground(Theme.SURFACE);
            popup.setBorder(BorderFactory.createLineBorder(Theme.BORDER_HI));
            JPanel box = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 2));
            box.setBackground(Theme.SURFACE);
            RoundedButton minus = UiKit.iconButton(Icons.of(Icons.Kind.MINUS, 16, Theme.TEXT), "Less");
            RoundedButton plus = UiKit.iconButton(Icons.of(Icons.Kind.PLUS, 16, Theme.TEXT), "More");
            minus.addActionListener(e -> step(-1));
            plus.addActionListener(e -> step(1));
            box.add(minus);
            box.add(plus);
            popup.add(box);
            popup.show(qtyButton, qtyButton.getWidth() + 6,
                    (qtyButton.getHeight() - popup.getPreferredSize().height) / 2);
        }

        private void step(int delta) {
            item.setQuantity(Util.round(item.getQuantity() + delta, 6));
            refresh();
        }

        private void editItem() {
            CostItem edited = CostDialog.ask(CostPanel.this, costs.getType(), item);
            if (edited != null) {
                costs.replace(index, edited);
                rebuild();
            }
        }
    }
}
