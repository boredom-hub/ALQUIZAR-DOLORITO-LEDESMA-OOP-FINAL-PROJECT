import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.AbstractDocument;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class UiKit {


    public static void smooth(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    public static JLabel label(String text, int style, float size, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.font(style, size));
        l.setForeground(color);
        return l;
    }

    public static String esc(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\n", "<br>");
    }

    public static JComponent vLine() {
        JPanel p = new JPanel();
        p.setBackground(Theme.BORDER);
        p.setPreferredSize(new Dimension(1, 1));
        return p;
    }

    public static JComponent hLine() {
        JPanel p = new JPanel();
        p.setBackground(Theme.BORDER);
        p.setPreferredSize(new Dimension(1, 1));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return p;
    }

    public static JPanel labelRow(JLabel label, JComponent info) {
        JPanel row = transparent(new FlowLayout(FlowLayout.LEFT, 0, 0));
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 6));
        row.add(label);
        row.add(info);
        return row;
    }

    public static JPanel labelRow(String text, InfoButton info) {
        return labelRow(label(text, Font.BOLD, 13, Theme.TEXT), info);
    }

    public static JPanel transparent(LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setOpaque(false);
        return p;
    }

    public static Component vgap(int px) {
        return Box.createVerticalStrut(px);
    }

    public static JScrollPane scroll(JComponent view) {
        JScrollPane sp = new JScrollPane(view, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setBorder(null);
        sp.setBackground(Theme.BG);
        sp.getViewport().setBackground(Theme.BG);
        styleScrollBar(sp.getVerticalScrollBar());
        sp.getVerticalScrollBar().setUnitIncrement(20);
        return sp;
    }

    public static void styleScrollBar(JScrollBar bar) {
        bar.setUI(new DarkScrollBarUI());
        bar.setBackground(Theme.BG);
    }


    public static RoundedButton primaryButton(String text, Icon icon) {
        RoundedButton b = new RoundedButton(text, icon, Theme.ON_GREEN, Theme.GREEN, Theme.GREEN_HI, null, 8, 7, 14);
        b.setFont(Theme.font(Font.BOLD, 13));
        return b;
    }

    public static RoundedButton outlineButton(String text) {
        return new RoundedButton(text, null, Theme.TEXT, null, Theme.SURFACE, Theme.BORDER, 8, 7, 16);
    }

    public static RoundedButton iconButton(Icon icon, String tooltip) {
        RoundedButton b = new RoundedButton(null, icon, Theme.TEXT, null, Theme.SURFACE, null, 6, 7, 8);
        b.setToolTipText(tooltip);
        return b;
    }

    public static RoundedButton segmentButton(String text) {
        RoundedButton b = new RoundedButton(text, null, Theme.TEXT, null, Theme.SURFACE, null, 0, 0, 12);
        b.setFont(Theme.font(Font.PLAIN, 12));
        return b;
    }

    public static void limitToNumber(JTextField field, boolean allowDecimal) {
        AbstractDocument doc = (AbstractDocument) field.getDocument();
        doc.setDocumentFilter(new NumberFilter(allowDecimal));
    }

    public static void selectAllOnFocus(final JTextField f) {
        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                f.selectAll();
            }
        });
    }

    public static RoundedPanel segment(RoundedTextField field, Component[] trailing) {
        RoundedPanel box = new RoundedPanel(new BorderLayout(), 10);
        box.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        field.setFramed(false);
        box.add(field, BorderLayout.CENTER);
        if (trailing.length > 0) {
            JPanel east = transparent(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.VERTICAL;
            c.weighty = 1;
            c.gridy = 0;
            int x = 0;
            for (Component t : trailing) {
                c.gridx = x;
                x++;
                east.add(vLine(), c);
                c.gridx = x;
                x++;
                east.add(t, c);
            }
            box.add(east, BorderLayout.EAST);
        }
        return box;
    }

    public static JSlider slider(int min, int max, int value) {
        JSlider s = new JSlider(min, max, value);
        s.setUI(new DarkSliderUI(s));
        s.setOpaque(false);
        s.setFocusable(true);
        s.setPreferredSize(new Dimension(200, 24));
        return s;
    }

    public static RoundedPanel comboBox(JComboBox<Object> combo, ListCellRenderer<Object> renderer) {
        combo.setUI(new DarkComboBoxUI());
        combo.setBorder(BorderFactory.createEmptyBorder());
        combo.setOpaque(false);
        combo.setBackground(Theme.BG);
        combo.setForeground(Theme.TEXT);
        if (renderer == null) {
            combo.setRenderer(new DarkListRenderer());
        } else {
            combo.setRenderer(renderer);
        }
        combo.setMaximumRowCount(12);
        RoundedPanel box = new RoundedPanel(new BorderLayout(), 10);
        box.add(combo, BorderLayout.CENTER);
        return box;
    }
}
