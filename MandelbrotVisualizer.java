public class MandelbrotVisualizer {
    static final int WIDTH = 1280;
    static final int HEIGHT = 720;
    
    public static void main(String[] args) {
        
        GUI gui = new GUI(WIDTH, HEIGHT, "Mandelbrot Visualizer");
        // TODO: have canvas automatically resize to fit the window, intead of passing in width and height
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        
        gui.add(canvas);
        gui.show();
    }
}