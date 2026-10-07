import javax.swing.Icon;
import javax.swing.JComponent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class EndCap extends JComponent {

    private Icon icon;

    public EndCap(Icon icon) {
        this.icon = icon;
        setPreferredSize(new Dimension(icon.getIconWidth() + 28, 10));
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        UiKit.smooth(g2);
        g2.setClip(0, 0, getWidth(), getHeight());
        g2.setColor(Theme.SURFACE);
        g2.fillRoundRect(-20, 0, getWidth() + 20, getHeight(), 9, 9);
        icon.paintIcon(this, g2, (getWidth() - icon.getIconWidth()) / 2,
                (getHeight() - icon.getIconHeight()) / 2);
        g2.dispose();
    }
}
