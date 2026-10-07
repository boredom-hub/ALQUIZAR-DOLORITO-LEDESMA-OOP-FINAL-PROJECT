import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

public abstract class StepFrame extends JFrame {

    protected ProductPricingModel model;

    private JPanel bodyHolder = new JPanel(new BorderLayout());
    private RoundedButton backButton = UiKit.outlineButton("Back");
    private RoundedButton nextButton = UiKit.primaryButton("Next", null);

    public StepFrame(ProductPricingModel model, int step, int totalSteps, String stepTitle,
                     int width, int height) {
        super("Product Pricing Calculator - " + stepTitle);
        this.model = model;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Theme.BG);
        getContentPane().setLayout(new BorderLayout());
        bodyHolder.setBackground(Theme.BG);
        getContentPane().add(buildHeader(step, totalSteps, stepTitle), BorderLayout.NORTH);
        getContentPane().add(bodyHolder, BorderLayout.CENTER);
        getContentPane().add(buildFooter(), BorderLayout.SOUTH);

        backButton.addActionListener(e -> onBack());
        nextButton.addActionListener(e -> onNext());

        setSize(width, height);
        setMinimumSize(new Dimension(640, 460));
    }

    protected abstract void onBack();

    protected abstract void onNext();

    protected void setBody(JComponent body) {
        bodyHolder.removeAll();
        bodyHolder.add(body, BorderLayout.CENTER);
    }

    protected void setBackLabel(String text) {
        backButton.setText(text);
    }

    protected void setNextLabel(String text) {
        nextButton.setText(text);
    }


    protected void showFrame() {
        setLocationRelativeTo(null);
        setVisible(true);
    }

    protected void showProblems(List<String> problems) {
        String text = "";
        for (String p : problems) {
            text = text + "- " + p + "\n";
        }
        JOptionPane.showMessageDialog(this, text.trim(), "Please check your input",
                JOptionPane.WARNING_MESSAGE);
    }

    private JComponent buildHeader(int step, int totalSteps, String stepTitle) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BG);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER),
                BorderFactory.createEmptyBorder(14, 24, 14, 24)));

        JPanel text = UiKit.transparent(new GridLayout(2, 1, 0, 2));
        text.add(UiKit.label("Product Pricing Calculator", Font.BOLD, 18, Theme.TEXT));
        text.add(UiKit.label("Step " + step + " of " + totalSteps + "  -  " + stepTitle,
                Font.PLAIN, 12, Theme.MUTED));
        header.add(text, BorderLayout.WEST);
        header.add(new StepDots(step, totalSteps), BorderLayout.EAST);
        return header;
    }

    private JComponent buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Theme.BG);
        footer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER),
                BorderFactory.createEmptyBorder(12, 24, 12, 24)));
        footer.add(backButton, BorderLayout.WEST);
        footer.add(nextButton, BorderLayout.EAST);
        return footer;
    }

    private class StepDots extends JComponent {
        private int step;
        private int total;

        StepDots(int step, int total) {
            this.step = step;
            this.total = total;
            setPreferredSize(new Dimension(total * 34, 30));
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            UiKit.smooth(g2);
            int y = getHeight() / 2 - 3;
            for (int i = 1; i <= total; i++) {
                if (i <= step) {
                    g2.setColor(Theme.GREEN);
                } else {
                    g2.setColor(Theme.BORDER);
                }
                g2.fillRoundRect((i - 1) * 34 + 4, y, 26, 6, 6, 6);
            }
            g2.dispose();
        }
    }
}
