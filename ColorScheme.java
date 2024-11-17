import java.awt.Color;

public class ColorScheme {
    final private double minValue;
    final private double maxValue;
    
    public ColorScheme(double minValue, double maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }
    
    public Color mapValueToColor(double value) {
        // ensure value is within range
        value = Math.max(this.minValue, Math.min(this.maxValue, value));

        // gradient stops
        Color stop1 = Color.BLACK; // black
        Color stop2 = Color.RED; // some purple color
        Color stop3 = Color.ORANGE; // reddish orange
        Color stop4 = Color.BLACK; // bright yellow

        // normalize value to range [0, 1]
        double normalizedValue = (value - this.minValue) / (this.maxValue - this.minValue);

        if (normalizedValue <= 0.33) {
            // interpolate between stop1 and stop2
            double t = normalizedValue / 0.33;
            return interpolateColor(stop1, stop2, t);
        } else if (normalizedValue <= 0.66) {
            // interpolate between stop2 and stop3
            double t = (normalizedValue - 0.33) / 0.33;
            return interpolateColor(stop2, stop3, t);
        } else {
            // interpolate between stop3 and stop4
            double t = (normalizedValue - 0.66) / 0.34;
            return interpolateColor(stop3, stop4, t);
        }
    }

    private static Color interpolateColor(Color c1, Color c2, double t) {
        int r = (int) (c1.getRed() + t * (c2.getRed() - c1.getRed()));
        int g = (int) (c1.getGreen() + t * (c2.getGreen() - c1.getGreen()));
        int b = (int) (c1.getBlue() + t * (c2.getBlue() - c1.getBlue()));
        return new Color(r, g, b);
    }
}
