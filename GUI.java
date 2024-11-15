import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * GUI helper class
 */
public class GUI {
    private final int WIDTH;
    private final int HEIGHT;
    private final JFrame frame;

    public GUI(int width, int height, String title) {
        WIDTH = width;
        HEIGHT = height;

        frame = new JFrame(title); 
        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setPreferredSize(new java.awt.Dimension(WIDTH, HEIGHT));
    }

    public void add(JPanel panel) {
        frame.add(panel);
    }

    public void show() {
        frame.setVisible(true);
    }
}