public class ParallelEngine extends RenderEngine {
    private long frameExecutionTime;

    public ParallelEngine(int width, int height, int maxIterations, float boundary, float resolutionScale) {
        super(width, height, maxIterations, boundary, resolutionScale);
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