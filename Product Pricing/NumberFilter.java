import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class NumberFilter extends DocumentFilter {

    private boolean allowDecimal;

    public NumberFilter(boolean allowDecimal) {
        this.allowDecimal = allowDecimal;
    }

    private boolean ok(String s) {
        if (allowDecimal) {
            return s.matches("\\d{0,9}(\\.\\d{0,4})?");
        }
        return s.matches("\\d{0,9}");
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String str, AttributeSet attr)
            throws BadLocationException {
        String cur = fb.getDocument().getText(0, fb.getDocument().getLength());
        if (ok(cur.substring(0, offset) + str + cur.substring(offset))) {
            super.insertString(fb, offset, str, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String str, AttributeSet attr)
            throws BadLocationException {
        String cur = fb.getDocument().getText(0, fb.getDocument().getLength());
        String insert = str;
        if (insert == null) {
            insert = "";
        }
        if (ok(cur.substring(0, offset) + insert + cur.substring(offset + length))) {
            super.replace(fb, offset, length, str, attr);
        }
    }
}
