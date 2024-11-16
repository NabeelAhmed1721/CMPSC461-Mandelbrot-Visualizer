public class SequentialEngine extends RenderEngine {
    private long frameExecutionTime;

    public SequentialEngine(int width, int height, int maxIterations, int boundary) {
        super(width, height, maxIterations, boundary);
    }

    @Override
    public int[][] computeMandelbrot(double xMin, double xMax, double yMin, double yMax) {
        long startTime = System.currentTimeMillis();
        int[][] pixelBuffer = new int[width][height];

        // how many "slices" to divide screen into
        double xStep = (xMax - xMin) / width;
        double yStep = (yMax - yMin) / height;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // xPos and yPos are mathmetical positions of the pixel
                double xPos = xMin + xStep * x;
                double yPos = yMin + yStep * y;

                Complex complex = new Complex(xPos, yPos);

                int iteration = calculateIteration(complex);
                pixelBuffer[x][y] = iteration;
            }
        }
    
        this.frameExecutionTime = System.currentTimeMillis() - startTime;
        return pixelBuffer;
    }

    private int calculateIteration(Complex complex) {
        Complex z = new Complex(0, 0);
        int iteration = 0;
        while (z.abs() < this.boundary && iteration < this.maxIterations) {
            // z = z^2 + c
            z = complex.add(z.power(2));
            iteration++;
        }
        return iteration;
    }

    @Override
    public long getFrameExecutionTime() {
        return this.frameExecutionTime;
    }
}