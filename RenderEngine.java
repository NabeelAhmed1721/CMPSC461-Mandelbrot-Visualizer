// Implemented by the parallel and sequential renderers
public abstract class RenderEngine {
    protected int width;
    protected int height;
    protected int maxIterations;
    protected float boundary;
    protected float resolutionScale = 1.0f; // 1 = full resolution, < 1 = lower resolution

    public RenderEngine(int width, int height, int maxIterations, float boundary, float resolutionScale) {
        this.width = width;
        this.height = height;
        this.maxIterations = maxIterations;
        this.boundary = boundary;
        this.resolutionScale = resolutionScale;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setResolutionScale(float scale) {
        this.resolutionScale = scale;
    }

    public abstract int[][] computeMandelbrot(double xMin, double xMax, double yMin, double yMax);

    // TODO: for measuring performance 
    public abstract long getFrameExecutionTime();
}