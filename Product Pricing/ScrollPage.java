import javax.swing.JPanel;
import javax.swing.Scrollable;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;

public class ScrollPage extends JPanel implements Scrollable {

    public ScrollPage(LayoutManager layout) {
        super(layout);
        setBackground(Theme.BG);
    }

    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    public int getScrollableUnitIncrement(Rectangle r, int orientation, int direction) {
        return 20;
    }

    public int getScrollableBlockIncrement(Rectangle r, int orientation, int direction) {
        return Math.max(40, r.height - 40);
    }

    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}
