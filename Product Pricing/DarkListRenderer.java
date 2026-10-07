import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.Component;
import java.awt.Font;

public class DarkListRenderer extends DefaultListCellRenderer {

    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                  boolean selected, boolean focus) {
        JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, selected, false);
        l.setBorder(BorderFactory.createEmptyBorder(7, 12, 7, 12));
        l.setFont(Theme.font(Font.PLAIN, 14));
        l.setForeground(Theme.TEXT);
        if (index < 0) {
            l.setOpaque(false);
        } else {
            l.setOpaque(true);
            if (selected) {
                l.setBackground(Theme.SURFACE);
            } else {
                l.setBackground(Theme.BG);
            }
        }
        return l;
    }
}
