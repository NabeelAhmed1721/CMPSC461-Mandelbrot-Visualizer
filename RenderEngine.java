// Implemented by the parallel and sequential renderers
public abstract class RenderEngine {
    protected int width;
    protected int height;
    protected int maxIterations;
    protected int boundary;

    public RenderEngine(int width, int height, int maxIterations, int boundary) {
        this.width = width;
        this.height = height;
        this.maxIterations = maxIterations;
        this.boundary = boundary;
    }

    public abstract int[][] computeMandelbrot(double xMin, double xMax, double yMin, double yMax);

    // TODO: for measuring performance 
    public abstract long getFrameExecutionTime();
}