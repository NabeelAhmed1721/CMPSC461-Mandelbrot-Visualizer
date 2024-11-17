public class SequentialEngine extends RenderEngine {
    private long frameExecutionTime;

    public SequentialEngine(int width, int height, int maxIterations, float boundary, float resolutionScale) {
        super(width, height, maxIterations, boundary, resolutionScale);
    }

    @Override
    public int[][] computeMandelbrot(double xMin, double xMax, double yMin, double yMax) {
        long startTime = System.currentTimeMillis();
        int[][] pixelBuffer = new int[this.width][this.height];

        // calculate scaled dimensions (can't be smaller than 1 pixel)
        // also truncate scaled width/height to fit into integer array bounds
        int scaledWidth = Math.max(1, (int)(this.width * this.resolutionScale));
        int scaledHeight = Math.max(1, (int)(this.height * this.resolutionScale));

        // how many "slices" to divide screen into
        double xStep = (xMax - xMin) / scaledWidth;
        double yStep = (yMax - yMin) / scaledHeight;

        for (int x = 0; x < scaledWidth; x++) {
            for (int y = 0; y < scaledHeight; y++) {
                // xPos and yPos are mathematical positions of the pixel
                double xPos = xMin + xStep * x;
                double yPos = yMin + yStep * y;
                Complex complex = new Complex(xPos, yPos);
                // calculate fractal iteration for the given pixel position
                int iteration = calculateIteration(complex);

                // add iteration value to pixel buffer
                // depending on resolution scale, copy value over onto pixel "block"
                // THIS IS SO BAD AND INEFFICIENT this is like O(n^4) 😭
                int startX = (x * this.width) / scaledWidth;
                int startY = (y * this.height) / scaledHeight;
                int endX = ((x + 1) * this.width) / scaledWidth;
                int endY = ((y + 1) * this.height) / scaledHeight;

                for (int sx = startX; sx < endX; sx++) {
                    for (int sy = startY; sy < endY; sy++) {
                        pixelBuffer[sx][sy] = iteration;
                    }
                }
                // pixelBuffer[x][y] = iteration;
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