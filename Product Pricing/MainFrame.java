import javax.swing.*;
import java.awt.*;

public class MainFrame {

    JFrame frame;

    public MainFrame() {

        frame = new JFrame("Product Pricing Calculator");
        frame.setSize(460, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Theme.BG);

        JPanel page = new JPanel(new GridBagLayout());
        page.setBackground(Theme.BG);

        JPanel box = UiKit.transparent(null);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));

        box.add(left(UiKit.label("Product Pricing Calculator", Font.BOLD, 22, Theme.TEXT)));
        box.add(Box.createVerticalStrut(4));
        box.add(left(UiKit.label("Work out your price, break-even point and profit.",
                Font.PLAIN, 12, Theme.MUTED)));
        box.add(Box.createVerticalStrut(20));

        RoundedPanel card = new RoundedPanel(null, 16);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(22, 24, 24, 24));

        card.add(left(UiKit.label("Welcome", Font.BOLD, 17, Theme.TEXT)));
        card.add(Box.createVerticalStrut(4));
        card.add(left(UiKit.label("Choose an option to continue.", Font.PLAIN, 12, Theme.MUTED)));
        card.add(Box.createVerticalStrut(18));

        RoundedButton btnLogin = UiKit.primaryButton("Login", null);
        RoundedButton btnRegister = UiKit.outlineButton("Register");

        btnLogin.addActionListener(e -> {
            frame.dispose();
            new Login();
        });

        btnRegister.addActionListener(e -> {
            frame.dispose();
            new Register();
        });

        card.add(left(btnLogin));
        card.add(Box.createVerticalStrut(10));
        card.add(left(btnRegister));
        stretch(btnLogin);
        stretch(btnRegister);

        card.setPreferredSize(new Dimension(360, card.getPreferredSize().height));

        box.add(left(card));
        page.add(box);
        frame.add(page);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JComponent left(JComponent c) {
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        return c;
    }

    private static void stretch(JComponent c) {
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, c.getPreferredSize().height));
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}
