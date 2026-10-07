import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class DarkScrollBarUI extends BasicScrollBarUI {

    protected void configureScrollBarColors() {
        thumbColor = new Color(0x3F3F46);
        trackColor = Theme.BG;
    }

    private JButton zero() {
        JButton b = new JButton();
        Dimension d = new Dimension(0, 0);
        b.setPreferredSize(d);
        b.setMinimumSize(d);
        b.setMaximumSize(d);
        return b;
    }

    protected JButton createDecreaseButton(int orientation) {
        return zero();
    }

    protected JButton createIncreaseButton(int orientation) {
        return zero();
    }

    protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
        g.setColor(Theme.BG);
        g.fillRect(r.x, r.y, r.width, r.height);
    }

    protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
        if (r.isEmpty() || !scrollbar.isEnabled()) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g.create();
        UiKit.smooth(g2);
        g2.setColor(thumbColor);
        g2.fillRoundRect(r.x + 2, r.y + 2, r.width - 4, r.height - 4, 8, 8);
        g2.dispose();
    }

    public Dimension getPreferredSize(JComponent c) {
        return new Dimension(12, 12);
    }
}
