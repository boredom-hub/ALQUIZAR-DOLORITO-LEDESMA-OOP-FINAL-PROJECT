import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class RoundedButton extends JButton {

    private Color fill;
    private Color hoverFill;
    private Color border;
    private int arc;

    public RoundedButton(String text, Icon icon, Color foreground, Color fill, Color hoverFill,
                         Color border, int arc, int padV, int padH) {
        super(text, icon);
        this.fill = fill;
        this.hoverFill = hoverFill;
        this.border = border;
        this.arc = arc;
        setForeground(foreground);
        setFont(Theme.font(Font.PLAIN, 13));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setRolloverEnabled(true);
        setIconTextGap(6);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(padV, padH, padV, padH));
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        UiKit.smooth(g2);
        boolean hot = getModel().isRollover() || getModel().isPressed();
        Color f = fill;
        if (hot) {
            f = hoverFill;
        }
        if (f != null) {
            g2.setColor(f);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
        }
        if (border != null) {
            g2.setColor(border);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
        }
        g2.dispose();
        super.paintComponent(g);
    }
}
