import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class ParallelEngine extends RenderEngine {
    private long frameExecutionTime;
    final private int threadCount;

    public ParallelEngine(int width, int height, int maxIterations, float boundary, float resolutionScale, int threadCount) {
        super(width, height, maxIterations, boundary, resolutionScale);
        this.threadCount = Math.max(1, threadCount);
    }

    @Override
    public int[][] computeMandelbrot(double xMin, double xMax, double yMin, double yMax) {
        long startTime = System.currentTimeMillis();
        
        // calculate scaled dimensions (can't be smaller than 1 pixel)
        int scaledWidth = Math.max(1, (int)(this.width * this.resolutionScale));
        int scaledHeight = Math.max(1, (int)(this.height * this.resolutionScale));

        // how many "slices" to divide screen into
        double xStep = (xMax - xMin) / scaledWidth;
        double yStep = (yMax - yMin) / scaledHeight;

        // Create a thread pool with a fixed number of threads
        ExecutorService executor = Executors.newFixedThreadPool(this.threadCount);
        // using invoke all to parallelize the computation
        List<Callable<Void>> tasks = new ArrayList<>();

        // store the result of each thread
        int[][] scaledBuffer = new int[scaledHeight][scaledWidth];
        
        // split up the work into rows
        // the more threads the fewer rows each thread will handle
        int chunkSize = Math.max(1, scaledHeight / this.threadCount);
        
        for (int startRow = 0; startRow < scaledHeight; startRow += chunkSize) {
            int finalStartRow = startRow;
            int endRow = Math.min(startRow + chunkSize, scaledHeight);
            
            // add a task for each row to the list of tasks
            tasks.add(() -> {
                for (int y = finalStartRow; y < endRow; y++) {
                    double yPos = yMin + yStep * y;
                    for (int x = 0; x < scaledWidth; x++) {
                        double xPos = xMin + xStep * x;
                        Complex complex = new Complex(xPos, yPos);
                        scaledBuffer[y][x] = this.calculateIteration(complex);
                    }
                }
                return null;
            });
        }
            
        try {
            // execute all tasks and wait for completion
            executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            System.err.println("Computation was interrupted");
        } finally {
            // shutdown the executor once complete
            executor.shutdown();
        }

        // create the final pixel buffer
        int[][] pixelBuffer = new int[this.width][this.height];

        // combine the thread results and scale each appropriately to the original dimensions
        IntStream.range(0, scaledHeight).parallel().forEach(scaledY -> {
            // map the scaled Y-coordinate to the corresponding range in the original buffer
            int originalStartY = (scaledY * this.height) / scaledHeight;
            int originalEndY = ((scaledY + 1) * this.height) / scaledHeight;

            for (int scaledX = 0; scaledX < scaledWidth; scaledX++) {
                //  map the scaled X-coordinate to the corresponding range in the original buffer
                int originalStartX = (scaledX * this.width) / scaledWidth;
                int originalEndX = ((scaledX + 1) * this.width) / scaledWidth;

                // retrieve the computed iteration value for the current scaled pixel
                int iterationValue = scaledBuffer[scaledY][scaledX];

                // Fill the corresponding range in the original buffer with the iteration value
                for (int originalY = originalStartY; originalY < originalEndY; originalY++) {
                    for (int originalX = originalStartX; originalX < originalEndX; originalX++) {
                        pixelBuffer[originalX][originalY] = iterationValue;
                    }
                }
            }
        });

        this.frameExecutionTime = System.currentTimeMillis() - startTime;
        return pixelBuffer;
    }

    @Override
    public long getFrameExecutionTime() {
        return this.frameExecutionTime;
    }
}