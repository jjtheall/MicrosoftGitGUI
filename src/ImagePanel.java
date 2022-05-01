import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImagePanel extends JPanel {

    BufferedImage bobcat;
    BufferedImage microsoft;

    public ImagePanel(){
        this.setPreferredSize(new Dimension(1000,100));
        try {
            bobcat = ImageIO.read(new File("docs/Bobcat.jpeg"));
            microsoft = ImageIO.read(new File("docs/micro.jpeg"));
        } catch (IOException e) {
            System.out.println("unable to find image files");
            e.printStackTrace();
        }
        JLabel title = new JLabel("GitHub Repo Generator");
        title.setFont(new Font("Serif",Font.BOLD,30));
        this.add(title);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D brush = (Graphics2D)g;
        brush.drawImage(bobcat,200,0,100,100,null);
        brush.drawImage(microsoft,700,0,150,100,null);
    }
}
