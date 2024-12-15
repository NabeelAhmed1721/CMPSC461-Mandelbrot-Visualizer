import org.jocl.*;
import static org.jocl.CL.*;

public class ParallelEngineGPU extends RenderEngine {
    private long frameExecutionTime;
    private cl_context context;
    private cl_command_queue commandQueue;
    private cl_program program;
    private cl_kernel kernel;

    // kernel source code
    // surprised this was easier to code than I thought. literally just copied from multi-threading with some changes
    // setting up OpenCL was harder lol
    private static final String MANDELBROT_KERNEL = 
        """
        __kernel void mandelbrot_kernel(
           const int width,
           const int height,
           const int maxIterations,
           const float boundary,
           const double xMin,
           const double xMax,
           const double yMin,
           const double yMax,
           __global int* output) {
           int x = get_global_id(0);
           int y = get_global_id(1);
        
           if (x >= width || y >= height) return;
        
           // Calculate complex point
           double xStep = (xMax - xMin) / width;
           double yStep = (yMax - yMin) / height;
        
           double real = xMin + x * xStep;
           double imag = yMin + y * yStep;
        
           double zx = 0.0;
           double zy = 0.0;
           int iteration = 0;
        
           while (zx * zx + zy * zy < boundary * boundary && iteration < maxIterations) {
               double xtemp = zx * zx - zy * zy + real;
               zy = 2 * zx * zy + imag;
               zx = xtemp;
               iteration++;
           }
        
           output[y * width + x] = iteration;
        }
        """;

    public ParallelEngineGPU(int width, int height, int maxIterations, float boundary, float resolutionScale) {
        super(width, height, maxIterations, boundary, resolutionScale);
        initOpenCL();
    }

    private void initOpenCL() {
        // platform + device + context + command queue setup
        // got this from some docs + S.O.F
        try {
            cl_platform_id[] platformIds = new cl_platform_id[1];
            int[] platformCount = new int[1];
            
            // setup platform
            clGetPlatformIDs(1, platformIds, platformCount);
            
            // just use first platform
            cl_platform_id platform = platformIds[0];

            // create context
            context = clCreateContext(
                null,
                1, 
                new cl_device_id[] { 
                    OpenCLUtils.getFirstDevice(platform) 
                }, 
                null,
                null,
                null
            );

            // command queue with properties
            cl_device_id device = OpenCLUtils.getFirstDevice(platform);
            commandQueue = clCreateCommandQueueWithProperties(context, device, null, null);

            // compile the kernel
            program = clCreateProgramWithSource(
                    context,
                    1, 
                    new String[] { MANDELBROT_KERNEL }, 
                    null,
                    null
                );
            
            clBuildProgram(program, 0, null, null, null, null);

            // Create the kernel
            kernel = clCreateKernel(program, "mandelbrot_kernel", null);

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize OpenCL", e);
        }
    }

    @Override
    public int[][] computeMandelbrot(double xMin, double xMax, double yMin, double yMax) {
        long startTime = System.currentTimeMillis();

        // rendering scaling
        int scaledWidth = Math.max(1, (int)(this.width * this.resolutionScale));
        int scaledHeight = Math.max(1, (int)(this.height * this.resolutionScale));

        // output buffer
        int[] output = new int[scaledWidth * scaledHeight];
        cl_mem outputBuffer = clCreateBuffer(context, 
            CL_MEM_WRITE_ONLY, 
            Sizeof.cl_int * output.length, 
            null, null);

        // kernel arguments
        // TODO: create helper method this was annoying to do...
        clSetKernelArg(kernel, 0, Sizeof.cl_int, Pointer.to(new int[] { scaledWidth }));
        clSetKernelArg(kernel, 1, Sizeof.cl_int, Pointer.to(new int[] { scaledHeight }));
        clSetKernelArg(kernel, 2, Sizeof.cl_int, Pointer.to(new int[] { this.maxIterations }));
        clSetKernelArg(kernel, 3, Sizeof.cl_float, Pointer.to(new float[] { this.boundary }));
        clSetKernelArg(kernel, 4, Sizeof.cl_double, Pointer.to(new double[] { xMin }));
        clSetKernelArg(kernel, 5, Sizeof.cl_double, Pointer.to(new double[] { xMax }));
        clSetKernelArg(kernel, 6, Sizeof.cl_double, Pointer.to(new double[] { yMin }));
        clSetKernelArg(kernel, 7, Sizeof.cl_double, Pointer.to(new double[] { yMax }));
        clSetKernelArg(kernel, 8, Sizeof.cl_mem, Pointer.to(outputBuffer));

        long[] globalWorkSize = new long[] { scaledWidth, scaledHeight };

        // queue kernel for execution
        clEnqueueNDRangeKernel(commandQueue, kernel, 2, null, 
            globalWorkSize, null, 0, null, null);

        // read the output buffer
        clEnqueueReadBuffer(commandQueue, outputBuffer, CL_TRUE, 0, 
            Sizeof.cl_int * output.length, Pointer.to(output), 0, null, null);

        // create the final pixel buffer
        int[][] pixelBuffer = new int[this.width][this.height];

        for (int scaledY = 0; scaledY < scaledHeight; scaledY++) {
            int originalStartY = (scaledY * this.height) / scaledHeight;
            int originalEndY = ((scaledY + 1) * this.height) / scaledHeight;

            for (int scaledX = 0; scaledX < scaledWidth; scaledX++) {
                int originalStartX = (scaledX * this.width) / scaledWidth;
                int originalEndX = ((scaledX + 1) * this.width) / scaledWidth;

                // retrieve the computed iteration value
                int iterationValue = output[scaledY * scaledWidth + scaledX];

                // fill the corresponding range in the original buffer
                // idk how i got this to work... but it does...
                for (int originalY = originalStartY; originalY < originalEndY; originalY++) {
                    for (int originalX = originalStartX; originalX < originalEndX; originalX++) {
                        pixelBuffer[originalX][originalY] = iterationValue;
                    }
                }
            }
        }

        // flush memory
        clReleaseMemObject(outputBuffer);

        this.frameExecutionTime = System.currentTimeMillis() - startTime;
        return pixelBuffer;
    }

    @Override
    public long getFrameExecutionTime() {
        return this.frameExecutionTime;
    }

    // Cleanup method to release OpenCL resources
    public void cleanup() {
        try {
            if (kernel != null) clReleaseKernel(kernel);
            if (program != null) clReleaseProgram(program);
            if (commandQueue != null) clReleaseCommandQueue(commandQueue);
            if (context != null) clReleaseContext(context);
        } catch (Exception e) {
            System.err.println("Error cleaning up OpenCL resources: " + e.getMessage());
        }
    }

    // helper class to get GPU (or CPU for fallback)
    private static class OpenCLUtils {
        public static cl_device_id getFirstDevice(cl_platform_id platform) {
            int[] deviceCount = new int[1];
            clGetDeviceIDs(platform, CL_DEVICE_TYPE_GPU, 0, null, deviceCount);
            
            if (deviceCount[0] > 0) {
                cl_device_id[] devices = new cl_device_id[deviceCount[0]];
                clGetDeviceIDs(platform, CL_DEVICE_TYPE_GPU, deviceCount[0], devices, null);
                return devices[0];
            }
            
            // fallback to CPU if no GPU found
            clGetDeviceIDs(platform, CL_DEVICE_TYPE_CPU, 0, null, deviceCount);
            cl_device_id[] devices = new cl_device_id[deviceCount[0]];
            clGetDeviceIDs(platform, CL_DEVICE_TYPE_CPU, deviceCount[0], devices, null);
            return devices[0];
        }
    }
}