import java.awt.Color;

public class ColorScheme {
    final private double maxIterations;
    
    // Color palette parameters
    private static final int PALETTE_SIZE = 500;
    private Color[] colorPalette;

    public ColorScheme(int maxIterations) {
        this.maxIterations = maxIterations;
        generateColorPalette(getDefaultGradientColors());
    }

    private static Color[] getDefaultGradientColors() {
        return new Color[] {
            // some colors I got off the internet
            new Color(0, 0, 0),
            new Color(32, 0, 64),
            new Color(64, 0, 128),
            new Color(128, 0, 255),
            new Color(255, 64, 0),
            new Color(255, 255, 255)
        };
    }

    private void generateColorPalette(Color[] gradientColors) {
        colorPalette = new Color[PALETTE_SIZE];
        
        // calculate segments for interpolation
        int segmentSize = PALETTE_SIZE / (gradientColors.length - 1);
        
        for (int segment = 0; segment < gradientColors.length - 1; segment++) {
            Color startColor = gradientColors[segment];
            Color endColor = gradientColors[segment + 1];
            
            // fill the segment with interpolated colors
            for (int i = 0; i < segmentSize; i++) {
                double t = (double) i / segmentSize;
                int paletteIndex = segment * segmentSize + i;
                
                if (paletteIndex < PALETTE_SIZE) {
                    colorPalette[paletteIndex] = interpolateColor(startColor, endColor, t);
                }
            }
        }
        
        // ensure the last color is set (in case of rounding issues)
        if (colorPalette[PALETTE_SIZE - 1] == null) {
            colorPalette[PALETTE_SIZE - 1] = gradientColors[gradientColors.length - 1];
        }
    }

    public Color mapValueToColor(double value) {
        if (value >= maxIterations) {
            return Color.BLACK;
        }

        // smooth iteration count
        // CREDIT: https://en.wikipedia.org/wiki/Plotting_algorithms_for_the_Mandelbrot_set#Exponentially_mapped_and_cyclic_iterations
        double log_zn = Math.log(value) / Math.log(2);
        double nu = Math.log(log_zn / Math.log(2)) / Math.log(2);
        double smoothIteration = value + 1 - nu;

        // map to color palette
        return getColorFromPalette(smoothIteration);
    }

    private Color getColorFromPalette(double smoothIteration) {
        // Normalize iteration to palette size
        double normalizedIteration = smoothIteration % PALETTE_SIZE;
        
        // Integer and fractional parts
        int index1 = (int) Math.floor(normalizedIteration);
        int index2 = (index1 + 1) % PALETTE_SIZE;
        double frac = normalizedIteration - Math.floor(normalizedIteration);

        // Interpolate between two colors
        return interpolateColor(
            colorPalette[index1], 
            colorPalette[index2], 
            frac
        );
    }

    private static Color interpolateColor(Color c1, Color c2, double t) {
        int r = (int) (c1.getRed() + t * (c2.getRed() - c1.getRed()));
        int g = (int) (c1.getGreen() + t * (c2.getGreen() - c1.getGreen()));
        int b = (int) (c1.getBlue() + t * (c2.getBlue() - c1.getBlue()));
        return new Color(
            Math.max(0, Math.min(255, r)),
            Math.max(0, Math.min(255, g)), 
            Math.max(0, Math.min(255, b))
        );
    }
}