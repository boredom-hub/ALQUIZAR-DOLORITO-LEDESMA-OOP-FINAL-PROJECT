import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.Graphics;
import java.awt.Rectangle;

public class DarkComboBoxUI extends BasicComboBoxUI {

    protected JButton createArrowButton() {
        JButton b = new JButton(Icons.of(Icons.Kind.CHEVRON_DOWN, 14, Theme.MUTED));
        b.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 10));
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setOpaque(false);
        return b;
    }

    public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
    }

    protected ComboPopup createPopup() {
        BasicComboPopup popup = new BasicComboPopup(comboBox) {
            protected JScrollPane createScroller() {
                JScrollPane sp = super.createScroller();
                sp.setBorder(null);
                sp.getViewport().setBackground(Theme.BG);
                UiKit.styleScrollBar(sp.getVerticalScrollBar());
                return sp;
            }
        };
        popup.setBorder(BorderFactory.createLineBorder(Theme.BORDER_HI));
        return popup;
    }
}
