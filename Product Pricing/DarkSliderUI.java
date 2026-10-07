import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class DarkSliderUI extends BasicSliderUI {

    public DarkSliderUI(JSlider slider) {
        super(slider);
    }

    protected Dimension getThumbSize() {
        return new Dimension(18, 18);
    }

    public void paintTrack(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        UiKit.smooth(g2);
        int h = 6;
        int y = trackRect.y + (trackRect.height - h) / 2;
        g2.setColor(Theme.SURFACE);
        g2.fillRoundRect(trackRect.x - 2, y, trackRect.width + 4, h, h, h);
        g2.dispose();
    }

    public void paintThumb(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        UiKit.smooth(g2);
        g2.setColor(Theme.BG);
        g2.fillOval(thumbRect.x, thumbRect.y, thumbRect.width - 1, thumbRect.height - 1);
        g2.setColor(Theme.GREEN);
        g2.setStroke(new BasicStroke(2f));
        g2.drawOval(thumbRect.x + 1, thumbRect.y + 1, thumbRect.width - 3, thumbRect.height - 3);
        g2.dispose();
    }

    public void paintFocus(Graphics g) {
    }
}
