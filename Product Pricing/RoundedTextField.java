import javax.swing.BorderFactory;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class RoundedTextField extends JTextField {

    private boolean framed = true;

    public RoundedTextField(int columns) {
        super(columns);
        setOpaque(false);
        setForeground(Theme.TEXT);
        setCaretColor(Theme.TEXT);
        setSelectionColor(Theme.GREEN_HI);
        setSelectedTextColor(Color.WHITE);
        setFont(Theme.font(Font.PLAIN, 14));
        setBorder(BorderFactory.createEmptyBorder(9, 12, 9, 12));
        addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                repaint();
            }

            public void focusLost(FocusEvent e) {
                repaint();
            }
        });
    }

    public void setFramed(boolean framed) {
        this.framed = framed;
    }

    protected void paintComponent(Graphics g) {
        if (framed) {
            Graphics2D g2 = (Graphics2D) g.create();
            UiKit.smooth(g2);
            g2.setColor(Theme.BG);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            if (hasFocus()) {
                g2.setColor(Theme.BORDER_HI);
            } else {
                g2.setColor(Theme.BORDER);
            }
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            g2.dispose();
        }
        super.paintComponent(g);
    }
}
