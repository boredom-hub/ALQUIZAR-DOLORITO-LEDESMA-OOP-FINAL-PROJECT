import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InfoButton extends JComponent {

    private String title;
    private String body;

    public InfoButton(String title, String body) {
        this.title = title;
        this.body = body;
        setPreferredSize(new Dimension(18, 18));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                showPopover();
            }
        });
    }

    public void setContent(String title, String body) {
        this.title = title;
        this.body = body;
    }

    private void showPopover() {
        StringBuilder html = new StringBuilder("<html><body style='width:280px'>");
        html.append("<b style='font-size:13px;color:#fafafa'>").append(UiKit.esc(title)).append("</b><br>");
        String[] lines = body.split("\n");
        for (String line : lines) {
            if (line.startsWith("# ")) {
                html.append("<div style='margin-top:6px;color:#fafafa'><b>").append(UiKit.esc(line.substring(2)))
                        .append("</b></div>");
            } else {
                html.append("<div style='color:#a1a1aa'>").append(UiKit.esc(line)).append("</div>");
            }
        }
        html.append("</body></html>");

        JLabel content = new JLabel(html.toString());
        content.setFont(Theme.font(Font.PLAIN, 12));
        content.setForeground(Theme.MUTED);
        content.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

        JPopupMenu popup = new JPopupMenu();
        popup.setBackground(Theme.SURFACE);
        popup.setBorder(BorderFactory.createLineBorder(Theme.BORDER_HI));
        JPanel holder = new JPanel(new BorderLayout());
        holder.setBackground(Theme.SURFACE);
        holder.add(content);
        popup.add(holder);
        popup.show(this, 0, getHeight() + 4);
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        UiKit.smooth(g2);
        g2.setColor(Theme.MUTED);
        g2.setStroke(new BasicStroke(1.3f));
        int d = Math.min(getWidth(), getHeight()) - 4;
        int x = (getWidth() - d) / 2;
        int y = (getHeight() - d) / 2;
        g2.drawOval(x, y, d, d);
        int cx = x + d / 2;
        g2.drawLine(cx, y + d / 2 - 1, cx, y + d - 4);
        g2.fillOval(cx - 1, y + 3, 2, 2);
        g2.dispose();
    }
}
