package flappybird;
import MGui.*;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
/**
 *
 * @author Emil
 */
public class Menu extends MFrame implements MouseListener{
    private final int SCREEN_WIDTH = 288, SCREEN_HEIGHT = 510, f;
    private BufferedImage wall, btnPlay, name;
    private final String TITLE;
    public Menu(String title){
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setResizable(false);
        TITLE = title;
        setTitle(title);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(MFrame.EXIT_ON_CLOSE);
        Random r = new Random();
        f = r.nextInt(2)+1;
        try{
            //wall = ImageIO.read(getClass().getResource("../res/Wall"+f+".png"));
            wall = ImageIO.read(Game.class.getResource("Wall"+f+".png"));
            //btnPlay = ImageIO.read(getClass().getResource("../res/BtnPlay.png"));
            btnPlay = ImageIO.read(Game.class.getResource("BtnPlay.png"));
            //name = ImageIO.read(getClass().getResource("../res/Scritta.png"));
            name = ImageIO.read(Game.class.getResource("Scritta.png"));
            //BufferedImage image = ImageIO.read(getClass().getResource("../res/FlappyBirdLogo.png"));
            BufferedImage image = ImageIO.read(Game.class.getResource("FlappyBirdLogo.png"));
            setIconImage(image);
        } catch(IOException ex){
            dispose();
            System.exit(0);
        }
        setVisible(true);
        mrepaint();
    }
    @Override
    public void mpaint ( Graphics2D g2 ){
        try{
            g2.drawImage(wall, 0, 0, null);
            g2.drawImage(btnPlay, SCREEN_WIDTH/2 - btnPlay.getWidth()/2, SCREEN_HEIGHT/2, null);
            g2.drawImage(name, SCREEN_WIDTH/3 - btnPlay.getWidth()/2, SCREEN_HEIGHT/8, null);
            GraphSet.setColor(g2, 255, 255, 255);
            Font f = new Font(g2.getFont().getName(),Font.BOLD, 20);
            g2.setFont(f);
            g2.drawString("Premi spazio per volare", SCREEN_WIDTH/8, SCREEN_HEIGHT/3);
        } catch(NullPointerException ex){}
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Rectangle2D.Double r = new Rectangle2D.Double(SCREEN_WIDTH/2 - btnPlay.getWidth()/2, SCREEN_HEIGHT/2, btnPlay.getWidth(), btnPlay.getHeight());
        if(r.contains(e.getX(), e.getY())){
            dispose();
            new Game(TITLE,f);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }
}
