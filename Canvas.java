import java.awt.Color;
    import java.awt.Dimension;
    import java.awt.Graphics;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import java.awt.event.MouseAdapter;
    import java.awt.event.MouseEvent;
    import javax.swing.JPanel;

    public class Canvas extends JPanel implements ActionListener {
        private RenderEngine renderEngine;
        private int[][] pixelBuffer;
        
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

        public void setRenderEngine(RenderEngine renderEngine) {
            this.renderEngine = renderEngine;
        }

        // TODO: abstract these values into more friendly variables
        public void render(double xMin, double xMax, double yMin, double yMax) {
            pixelBuffer = this.renderEngine.computeMandelbrot(
                xMin, xMax, yMin, yMax
            );
            // rerender the canvas after every frame
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // TODO: find a more performant way to do this
            if (pixelBuffer != null) {
                for (int x = 0; x < pixelBuffer.length; x++) {
                    for (int y = 0; y < pixelBuffer[x].length; y++) {
                        // TODO: find a proper color scheme
                        int colorValue = pixelBuffer[x][y] % 256;
                        g.setColor(new Color(colorValue, colorValue, colorValue));
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