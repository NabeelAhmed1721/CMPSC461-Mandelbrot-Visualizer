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
        // TODO: have canvas automatically resize to fit the window, intead of passing in width and height
        Canvas canvas = new Canvas(WIDTH, HEIGHT);

        // start with sequential rendering
        RenderEngine sequential = new SequentialEngine(
            WIDTH,
            HEIGHT,
            200, // iteration (lower = higher performance but can't "dive" as deeply into fractal)
            2.0f, // bounds (lower = higher performance but less detailed fractal)
            1f // resolution scale (lower = higher performance)
        );

        canvas.setRenderEngine(sequential);    

        // WORK IN PROGRESS (DOES NOT WORK RIGHT NOW ☹️):
        // RenderEngine parallel = new ParallelEngine(
        //     WIDTH,
        //     HEIGHT,
        //     500, // iteration
        //     2.0f, // bounds
        //     1f, // resolution scale
        //     64 // number of threads
        // );

        // canvas.setRenderEngine(parallel);    

        // "starting" position. These values are best to get a good overview of the fractal
        // these values shift as you zoom/pan the screen
        canvas.setPlane(-2.0f, 1.0f, -1.0f, 1.0f);

        // initial render
        canvas.render();

        // launch GUI with canvas
        gui.add(canvas);
        gui.show();
    }
}