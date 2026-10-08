import java.text.DecimalFormat;

public class Util {

    public static final String PESO = "\u20B1";

    public static String peso(double amount) {
        if (Math.abs(amount) < 0.005) {
            amount = 0;
        }
        String text = new DecimalFormat("#,##0.00").format(Math.abs(amount));
        if (amount < 0) {
            return "-" + PESO + text;
        }
        return PESO + text;
    }

    public static String num(double value) {
        if (value == Math.floor(value) && Math.abs(value) < 1e15) {
            return String.valueOf((long) value);
        }
        return new DecimalFormat("0.######").format(value);
    }

    public static String fixed(double value, int decimals) {
        return String.format("%." + decimals + "f", value);
    }

    public static double round(double value, int places) {
        double factor = Math.pow(10, places);
        double scaled = Math.abs(value) * factor;
        if (scaled > 1e15) {
            return value;
        }
        double rounded = Math.round(scaled + 0.0000001);
        if (rounded == 0) {
            return 0;
        }
        if (value < 0) {
            return -rounded / factor;
        }
        return rounded / factor;
    }
}
