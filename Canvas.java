import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class Canvas extends JPanel implements ActionListener {
    public Canvas(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        
        // Handle mouse events here
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Mouse clicked at " + e.getX() + ", " + e.getY());
            }
        });
    }

    private void render(Graphics g) {
        // TODO: remove everything here and render mandelbrot set
        
        // All rendering is done here
        Dimension rectangeSize = new Dimension(400, 300);

        // Example for now; just draw a green rectangle
        g.setColor(Color.GREEN);
        g.fillRect(
            (int) (getWidth() / 2.0 - rectangeSize.width / 2.0),
            (int) (getHeight() / 2.0 - rectangeSize.height / 2.0),
            rectangeSize.width,
            rectangeSize.height 
        ); 
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.render(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}