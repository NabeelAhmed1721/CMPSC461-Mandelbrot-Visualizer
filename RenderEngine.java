// Implemented by the parallel and sequential renderers
public abstract class RenderEngine {
    protected int width;
    protected int height;
    protected int maxIterations;

    public RenderEngine(int width, int height, int maxIterations) {
        this.width = width;
        this.height = height;
        this.maxIterations = maxIterations;
    }

    public abstract int[][] computeMandelbrot(double xMin, double xMax, double yMin, double yMax);

    // TODO: for measuring performance 
    public abstract long getFrameExecutionTime();
}