package View;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This class is used to set up the background panel for the GameView.
 */
public class BackgroundPanel extends JPanel {

    private Image image;

    /**
     * Constructor for the BackgroundPanel class.
     * @param path the image to add
     */
    public BackgroundPanel(String path) {
        try{
            BufferedImage image = ImageIO.read(getClass().getResource(path));
            this.image = image;
            this.setLayout(null);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Used to paint the image.
     * @param g Graphics
     */
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(image,0,0,this);
    }

    /**
     * Used to get the preferred size.
     * @return Dimension the size
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(image.getWidth(this), image.getHeight(this));
    }
}
