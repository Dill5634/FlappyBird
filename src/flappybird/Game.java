package flappybird;
import MGui.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 *
 * @author Emil
 */
public class Game extends MFrame implements Runnable, MouseListener{
    private final int SCREEN_WIDTH = 288, SCREEN_HEIGHT = 510,HEIGHT_AVAILABLE, HGAP = 150;
    private int pavimentX = 0, cont = 0;
    private BufferedImage wall, paviment, tubeUp, tubeDown;
    private Bird bird;
    private ArrayList<Tube> al,_al;
    private ArrayList<BufferedImage> birds;
    private Random r;
    private boolean[] keys;
    private boolean overcomed = false, flying = false;
    private final String TITLE;
    private final KeyListener listener;
    public Game(String title, int w){
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setResizable(false);
        TITLE = title;
        setTitle(title);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(MFrame.EXIT_ON_CLOSE);
        try{
            //wall = ImageIO.read(getClass().getResource("../res/Wall"+w+".png"));
            wall = ImageIO.read(Game.class.getResource("Wall"+w+".png"));
            //paviment = ImageIO.read(getClass().getResource("../res/Paviment.png"));
            paviment = ImageIO.read(Game.class.getResource("Paviment.png"));
            //tubeUp = ImageIO.read(getClass().getResource("../res/TuboSu.png"));
            tubeUp = ImageIO.read(Game.class.getResource("TuboSu.png"));
            //tubeDown = ImageIO.read(getClass().getResource("../res/TuboGiu.png")).getSubimage(1, 0, tubeUp.getWidth(), tubeUp.getHeight()); //l' immagine è un pixel più larga
            tubeDown = ImageIO.read(Game.class.getResource("TuboGiu.png")).getSubimage(1, 0, tubeUp.getWidth(), tubeUp.getHeight());
            birds = new ArrayList(3);
            for(int i=1; i<4; i++)
                //birds.add(ImageIO.read(getClass().getResource("../res/Bird"+i+".png")));
                birds.add(ImageIO.read(Game.class.getResource("Bird"+i+".png")));
            bird = new Bird(birds, SCREEN_WIDTH/4, SCREEN_HEIGHT/3);
            //bird = new Bird(ImageIO.read(getClass().getResource("../res/Bird1.png")), SCREEN_WIDTH/4, SCREEN_HEIGHT/3);
            //BufferedImage image = ImageIO.read(getClass().getResource("../res/FlappyBirdLogo.png"));
            BufferedImage image = ImageIO.read(Game.class.getResource("FlappyBirdLogo.png"));
            setIconImage(image);
        } catch(IOException ex){
            dispose();
            System.exit(0);
        }
        keys=new boolean[256];
        for(boolean i:keys)
            i=false;
        listener = new MyKeyListener();
        addKeyListener(listener);
        //addMouseListener(new MyMouseListener());
        HEIGHT_AVAILABLE = SCREEN_HEIGHT - paviment.getHeight();
        r = new Random();
        al = new ArrayList();
        _al = new ArrayList();
        for(int i=0, j=400; i<3; i++, j+=150){
            int startY = r.nextInt(HEIGHT_AVAILABLE-HGAP-50);
            if(startY==0)
                startY = 50;
            al.add(new Tube(j,startY-tubeUp.getHeight(),tubeUp.getWidth(), tubeUp.getHeight())); //tubo sopra
            _al.add(new Tube(j,startY+HGAP,tubeDown.getWidth(), tubeDown.getHeight())); //tubo sotto
        }
        setVisible(true);
        Thread t = new Thread(this);
        t.start();
    }
    @Override
    public void mpaint ( Graphics2D g2 ){
        g2.drawImage(wall, 0, 0, null);
        if(al.get(0).getX()+tubeUp.getWidth()<0){
            al.get(0).stop();
            _al.get(0).stop();
            al.remove(0);
            _al.remove(0);
            overcomed = false;
            int startY = r.nextInt(HEIGHT_AVAILABLE-HGAP-50);
            if(startY == 0)
                startY = 50;
            al.add(new Tube((int)al.get(1).getX()+150,startY-tubeUp.getHeight(),tubeUp.getWidth(), tubeUp.getHeight()));
            _al.add(new Tube((int)al.get(1).getX()+150,startY+HGAP,tubeDown.getWidth(), tubeDown.getHeight()));
        }
        for(int i=0; i<al.size(); i++){
            g2.drawImage(tubeDown, (int)al.get(i).getX(), (int)al.get(i).getY(), null);
            g2.drawImage(tubeUp, (int)_al.get(i).getX(), (int)_al.get(i).getY(), null);
            /*g2.setColor(Color.red);
            g2.drawRect((int)al.get(0).getX(), (int)al.get(0).getY(), tubeUp.getWidth(), tubeUp.getHeight());
            g2.drawRect((int)_al.get(0).getX(), (int)_al.get(0).getY(), tubeDown.getWidth(), tubeDown.getHeight());
            */
        }
        g2.drawImage(bird.getImg(),(int)bird.getX(),(int)bird.getY(), this);
        if((pavimentX+1+SCREEN_WIDTH)<paviment.getWidth()) 
            pavimentX++;
        else pavimentX = 0;
        g2.drawImage(paviment.getSubimage(pavimentX,0 ,SCREEN_WIDTH , paviment.getHeight()), 0, SCREEN_HEIGHT-paviment.getHeight(), this);
        g2.setFont(new Font("Serif", Font.BOLD, 30));
        g2.setColor(Color.WHITE);
        g2.drawString(cont+"", (int)(SCREEN_WIDTH/2.2), SCREEN_HEIGHT/6);
        //g2.drawString(bird.getY()+"", SCREEN_WIDTH/4, SCREEN_HEIGHT/6);
        
    }
    
    public void score(){
        Tube tube = al.get(0);
        if(tube.getX()+tube.getWidth()<bird.getX() && !overcomed){
            overcomed = true;
            cont++;
        }
    }
    
    public void updateStatus(){
        if(bird.getY()>-10){
            if(flying || keys[KeyEvent.VK_SPACE]){
                //System.out.println("Entrato");
                bird.setMove(true);
                flying = false;
            }
            else bird.setMove(false);
        }
        if(intersectTubes() || bird.getY()+bird.getHeight()>=(SCREEN_HEIGHT-paviment.getHeight())){
            bird.setFlying(false);
            //JOptionPane.showMessageDialog(null, "Sei morto :(\nPunteggio : "+cont, "MORTO", JOptionPane.INFORMATION_MESSAGE);
            if(JOptionPane.showConfirmDialog(this,"Sei morto. Vuoi riprovare?","MORTO",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
              reset();
            else{
              dispose();
              System.exit(0);
            }
            //dispose();
            //new Menu(TITLE);
            //System.exit(0); 
            reset();
        }
    }
    
    public void reset(){
        bird = new Bird(birds, SCREEN_WIDTH/4, SCREEN_HEIGHT/3);
        al = new ArrayList();
        _al = new ArrayList();
        for(int i=0, j=400; i<3; i++, j+=150){
            int startY = r.nextInt(HEIGHT_AVAILABLE-HGAP-50);
            if(startY==0)
                startY = 50;
            al.add(new Tube(j,startY-tubeUp.getHeight(),tubeUp.getWidth(), tubeUp.getHeight())); //tubo sopra
            _al.add(new Tube(j,startY+HGAP,tubeDown.getWidth(), tubeDown.getHeight())); //tubo sotto
        }
        cont = pavimentX = 0;
    }
    
    public boolean intersectTubes(){
        for(int i=0; i<al.size(); i++){
            if(bird.intersects(al.get(i)) || bird.intersects(_al.get(i)))
                return true;
            }
        return false;
    }

    @Override
    public void run() {
        while(true){
            try{
                Thread.sleep(10);
            } catch(InterruptedException ex){}
            updateStatus();
            score();
            mrepaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        flying = true;
        //System.out.println("click");
    }
    
    private class MyKeyListener implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {
                    keys[e.getKeyCode()]=true;
		}

		@Override
		public void keyPressed(KeyEvent e) {
                    keys[e.getKeyCode()]=true;
		}

		@Override
		public void keyReleased(KeyEvent e) {
                    keys[e.getKeyCode()]=false;
		}
	}
    /*private class MyMouseListener extends MouseAdapter{

        @Override
        public void mouseClicked(MouseEvent e) {
            flying = true;
            System.out.println("click");
        }
    }*/
    
}
