public class SequentialEngine extends RenderEngine {
    private long frameExecutionTime;

    public SequentialEngine(int width, int height, int maxIterations) {
        super(width, height, maxIterations);
    }

    @Override
    public int[][] computeMandelbrot(double xMin, double xMax, double yMin, double yMax) {
        long startTime = System.currentTimeMillis();
        int[][] pixelBuffer = new int[width][height];

        // implementation here!
        
        this.frameExecutionTime = System.currentTimeMillis() - startTime;
        return pixelBuffer;
    }

    @Override
    public long getFrameExecutionTime() {
        return this.frameExecutionTime;
    }
}