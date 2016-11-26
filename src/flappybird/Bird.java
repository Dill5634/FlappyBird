package flappybird;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author Emil
 */
public class Bird extends Rectangle2D.Double implements Runnable{
    
    private final int MAX_SPEED = 30;
    private double gravity = 1.5, jump = 20;
    private BufferedImage img;
    private ArrayList<BufferedImage> birds;
    private int cont = 0;
    private boolean move, flying = true;
    
    public Bird(ArrayList<BufferedImage> birds, int x, int y){
        super(x, y, birds.get(0).getWidth(), birds.get(0).getHeight());
        this.birds = birds;
        Thread t = new Thread(this);
        t.start();
    }

    public BufferedImage getImg(){
        return img;
    }
    
    public void setMove(boolean move){
        this.move = move;
    }

    public void setFlying(boolean flying) {
        this.flying = flying;
    }
    
    private void move(){
        if(move){
            jump = 20;
            y-=jump;
            //System.out.println("Y: "+y);
        }
        else {
            y+=jump;
            jump+= gravity;
            if(jump > MAX_SPEED)
                jump = MAX_SPEED;
        }
    }
    
    @Override
    public void run() {
        while(flying){
            move();
            move = false;
            img = birds.get(cont);
            cont = (cont!=2)? cont+=1 : 0;
            try{
                Thread.sleep(100); 
            } catch(InterruptedException ex){
                
            }
        }
        
    }
}
