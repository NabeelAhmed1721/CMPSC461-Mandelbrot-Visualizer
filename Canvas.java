import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import javax.swing.JPanel;

public class Canvas extends JPanel implements ActionListener {
    private int[][] pixelBuffer;
    private RenderEngine renderEngine;
    private float xMin, xMax, yMin, yMax;
    private Point lastDragPoint;
    private boolean isDragging = false;

    public Canvas(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        
        // Mouse click events to help with dragging state
        addMouseListener(new MouseAdapter() {
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
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDragging && lastDragPoint != null) {
                    Point currentPoint = e.getPoint();
                    
                    // drag distance in screen coordinates
                    float dx = currentPoint.x - lastDragPoint.x;
                    float dy = currentPoint.y - lastDragPoint.y;
                    
                    // convert to screen distance
                    float xRange = xMax - xMin;
                    float yRange = yMax - yMin;
                    float xOffset = -(dx / getWidth()) * xRange;
                    float yOffset = -(dy / getHeight()) * yRange;
                    
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

            float rangeX = this.xMax - this.xMin;
            float rangeY = this.yMax - this.yMin;

            float zoomFactor = rotation < 0 ? 0.8f : 1.25f;

            // New range based on zoom factor
            float newRangeX = rangeX * zoomFactor;
            float newRangeY = rangeY * zoomFactor;

            // Calculate new bounds with the mouse position as the zoom center
            this.xMin = this.xMin + mouseXRatio * (rangeX - newRangeX);
            this.xMax = this.xMin + newRangeX;
            this.yMin = this.yMin + mouseYRatio * (rangeY - newRangeY);
            this.yMax = this.yMin + newRangeY;

            // Call render with updated bounds
            render();
        });
    }

    public void setRenderEngine(RenderEngine renderEngine) {
        this.renderEngine = renderEngine;
    }

    public void setPlane(float xMin, float xMax, float yMin, float yMax) {
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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}