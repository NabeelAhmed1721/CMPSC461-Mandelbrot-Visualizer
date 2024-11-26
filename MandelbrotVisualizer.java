public class MandelbrotVisualizer {
    // 1080p - Ultra High Resolution
    // static final int WIDTH = 1920;
    // static final int HEIGHT = 1080;
    
    // 720p - High Resolution
    static final int WIDTH = 1280;
    static final int HEIGHT = 720;
    
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
        canvas.setRenderEngine(new ParallelEngine(
            WIDTH,
            HEIGHT,
            7500, // iteration
            2.0f, // bounds
            1f, // resolution scale
            11 // number of threads
        ));    

        // "starting" position. These values are best to get a good overview of the fractal these values shift as you zoom/pan the screen
        // canvas.setPlane(-2.0f, 1.0f, -1.0f, 1.0f);

        // SPECIAL COORDS
        // Vortex
        canvas.setPlane(-0.6847020228775615f, -0.6847017318389533f, -0.2995489513767755f, -0.29954880585747146f);
        // initial render
        canvas.render();

        // launch GUI with canvas
        gui.add(canvas);
        gui.show();
    }
}