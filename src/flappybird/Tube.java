package flappybird;

import java.awt.geom.Rectangle2D;

/**
 *
 * @author Emil
 */
public class Tube extends Rectangle2D.Double implements Runnable{
    private final double VEL_X = 1;
    private Thread t;
    private boolean running = true;
    public Tube(int startX, int startY, int width, int height){
        super(startX, startY, width, height);
        t = new Thread(this);
        t.start();
    }
    
    public void stop(){
        running = false;
    }
    
    @Override
    public void run() {
        while(running){
            x-=VEL_X;
            try{
                Thread.sleep(10);
            } catch(InterruptedException ex){
                
            }
        }
    }   
}
