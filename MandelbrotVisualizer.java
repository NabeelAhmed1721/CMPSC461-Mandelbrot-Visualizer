public class MandelbrotVisualizer {
    // 1080p - Ultra High Resolution
    static final int WIDTH = 1920;
    static final int HEIGHT = 1080;
    
    // 720p - High Resolution
    // static final int WIDTH = 1280;
    // static final int HEIGHT = 720;
    
    // 480p - Medium Resolution
    // static final int WIDTH = 854;
    // static final int HEIGHT = 480;
    
    // 144p - Low Resolution
    // static final int WIDTH = 192;
    // static final int HEIGHT = 144;

    public static void main(String[] args) {
        
        GUI gui = new GUI(WIDTH, HEIGHT, "Mandelbrot Visualizer");
        Canvas canvas = new Canvas(WIDTH, HEIGHT); // TODO: have canvas automatically resize to fit the window, intead of passing in width and height

        // sequential rendering
        // canvas.setRenderEngine(new SequentialEngine(
        //     WIDTH,
        //     HEIGHT,
        //     2500, // iteration (lower = higher performance but can't "dive" as deeply into fractal)
        //     2f, // bounds (lower = higher performance but less detailed fractal)
        //     1f // resolution scale (lower = higher performance)
        // ));    

        // multithreaded rendering
        // canvas.setRenderEngine(new ParallelEngine(
        //     WIDTH,
        //     HEIGHT,
        //     20000, // iteration
        //     2.0f, // bounds
        //     0.75f, // resolution scale
        //     6 // number of threads
        // ));
        
        // gpu rendering
        canvas.setRenderEngine(new ParallelEngineGPU(
            WIDTH,
            HEIGHT,
            50000, // iteration
	    // 5000,
            2.0f, // bounds
            1.0f // resolution scale
        ));    

        // "starting" position. These values are best to get a good overview of the fractal these values shift as you zoom/pan the screen
        // canvas.setPlane(-2.0f, 1.0f, -1.0f, 1.0f);

        // SPECIAL COORDS
        // Vortex
        // canvas.setPlane(
        //     -0.6847018852010041,
        //     -0.6847018852009084,
        //     -0.2995488788802855,
        //     -0.2995488788802376
        // );
        // Circles
        canvas.setPlane(
            -0.7498839734911263,
            -0.7498825524038125,
            0.023017205749902025,
            0.023017916293558927
        );
        
        // initial render
        canvas.render();

        // launch GUI with canvas
        gui.add(canvas);
        gui.show();
    }
}