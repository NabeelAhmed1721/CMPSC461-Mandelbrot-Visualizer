import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import javax.swing.JPanel;

public class Canvas extends JPanel implements ActionListener {
    private int[][] pixelBuffer;
    private RenderEngine renderEngine;
    private double xMin, xMax, yMin, yMax;
    private Point lastDragPoint;
    private boolean isDragging = false;

    public Canvas(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        
        // Mouse click events to help with dragging state
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastDragPoint = e.getPoint();
                isDragging = true;
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                isDragging = false;
            }
        });
        
        // click to drag and pan the plane
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDragging && lastDragPoint != null) {
                    Point currentPoint = e.getPoint();
                    
                    // drag distance in screen coordinates
                    float dx = currentPoint.x - lastDragPoint.x;
                    float dy = currentPoint.y - lastDragPoint.y;
                    
                    // convert to screen distance
                    double xRange = xMax - xMin;
                    double yRange = yMax - yMin;
                    double xOffset = -(dx / getWidth()) * xRange;
                    double yOffset = -(dy / getHeight()) * yRange;
                    
                    // update the plane coordinates
                    xMin += xOffset;
                    xMax += xOffset;
                    yMin += yOffset;
                    yMax += yOffset;
                    
                    // save the current point for next drag calculation
                    lastDragPoint = currentPoint;
                    
                    // render with new plane coordinates
                    render();
                }
            }
        });
    
        // zoom in/out on mouse wheel
        this.addMouseWheelListener((MouseWheelEvent e) -> {
            int rotation = e.getWheelRotation(); // Negative = zoom in, Positive = zoom out

            float mouseXRatio = (float) e.getX() / getWidth();
            float mouseYRatio = (float) e.getY() / getHeight();

            double rangeX = this.xMax - this.xMin;
            double rangeY = this.yMax - this.yMin;

            float zoomFactor = rotation < 0 ? 0.8f : 1.25f;

            // New range based on zoom factor
            double newRangeX = rangeX * zoomFactor;
            double newRangeY = rangeY * zoomFactor;

            // Calculate new bounds with the mouse position as the zoom center
            this.xMin = this.xMin + mouseXRatio * (rangeX - newRangeX);
            this.xMax = this.xMin + newRangeX;
            this.yMin = this.yMin + mouseYRatio * (rangeY - newRangeY);
            this.yMax = this.yMin + newRangeY;

            // Call render with updated bounds
            render();
        });

        // if enter is pressed output plane values
        setFocusable(true);
        requestFocusInWindow();
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    System.out.println("xMin: " + xMin);
                    System.out.println("xMax: " + xMax);
                    System.out.println("yMin: " + yMin);
                    System.out.println("yMax: " + yMax);
                }
            }
        });
    }

    public void setRenderEngine(RenderEngine renderEngine) {
        this.renderEngine = renderEngine;
    }

    public void setPlane(double xMin, double xMax, double yMin, double yMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    // TODO: abstract these values into more friendly variables
    public void render() {
        pixelBuffer = this.renderEngine.computeMandelbrot(
            this.xMin, this.xMax, this.yMin, this.yMax
        );
        // rerender the canvas after every frame
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        ColorScheme colorScheme = new ColorScheme(0, renderEngine.maxIterations);

        // TODO: find a more performant way to do this (look into VolatileImage)
        if (pixelBuffer != null) {
            for (int x = 0; x < pixelBuffer.length; x++) {
                for (int y = 0; y < pixelBuffer[x].length; y++) {
                    // TODO: find a proper color scheme
                    g.setColor(colorScheme.mapValueToColor(pixelBuffer[x][y]));
                    g.fillRect(x, y, 1, 1);
                }
            }                
        }

        // display frame time in the top right corner
        long executionTime = this.renderEngine.getFrameExecutionTime();
        int frameRate = Math.round(1000 / executionTime);
        g.setColor(Color.WHITE);
        g.drawString("Frame Rate: " + frameRate + " FPS", 10, 20);
        g.drawString("Frame Time: " + executionTime + "ms", 10, 35);
        g.drawString("Render Engine: " + renderEngine.getClass().getName(), 10, 50);
        // display coordinates
        g.setColor(Color.WHITE);
        g.drawString("X Min: " + this.xMin, 10, 65);
        g.drawString("X Max: " + this.xMax, 10, 80);
        g.drawString("Y Min: " + this.yMin, 10, 95);
        g.drawString("Y Max: " + this.yMax, 10, 110);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}