import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;

public class RoundedPanel extends JPanel {

    private int arc;
    private Color fill = Theme.BG;
    private Color line = Theme.BORDER;
    private boolean dashed = false;

    public RoundedPanel(LayoutManager layout, int arc) {
        super(layout);
        this.arc = arc;
        setOpaque(false);
    }

    public void setDashed(boolean dashed) {
        this.dashed = dashed;
        repaint();
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        UiKit.smooth(g2);

        int w = getWidth() - 1;
        int h = getHeight() - 1;

        g2.setColor(fill);
        g2.fillRoundRect(0, 0, w, h, arc, arc);
        g2.setColor(line);
        
        if (dashed) {
            g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, new float[]{4f, 4f}, 0f));
        } else {
            g2.setStroke(new BasicStroke(1f));
        }
        g2.drawRoundRect(0, 0, w, h, arc, arc);
        g2.dispose();
        super.paintComponent(g);
    }
}
