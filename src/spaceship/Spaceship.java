
package spaceship;

import java.io.*;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;

public class Spaceship extends JFrame implements Runnable {
    static final int WINDOW_WIDTH = 420;
    static final int WINDOW_HEIGHT = 445;
    final int XBORDER = 20;
    final int YBORDER = 20;
    final int YTITLE = 25;
    boolean animateFirstTime = true;
    int xsize = -1;
    int ysize = -1;
    Image image;
    Graphics2D g;

    sound zsound = null;
    sound bgSound = null;
    Image outerSpaceImage;
    Image coolpic;

//variables for rocket.
    Image rocketImage;
    int rocketXPos;
    int rocketYPos;
    int rocketXSpeed;
    int rocketYSpeed;
    int numStars = 10;
    int starXPos[];
    int starYPos[];
    boolean starActive[] = new boolean[numStars];
    boolean starXLeft;
    boolean starXRight;
    boolean rocketOK1;
    boolean rocketOK2;
    int whichStarHit;
    int rocketScale = 1;
    
    Missile missiles[] = new Missile[Missile.numMissiles];
    
    
    boolean donePlaying;

    static Spaceship frame;
    public static void main(String[] args) {
        frame = new Spaceship();
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public Spaceship() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.BUTTON1 == e.getButton()) {
                    //left button

// location of the cursor.
                    int xpos = e.getX();
                    int ypos = e.getY();

                }
                if (e.BUTTON3 == e.getButton()) {
                    //right button
                    reset();
                }
                repaint();
            }
        });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        repaint();
      }
    });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseMoved(MouseEvent e) {

        repaint();
      }
    });

        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.VK_UP == e.getKeyCode()) {
                    if(rocketOK1)
                    {
                        rocketYSpeed-=7;
                        rocketOK1=false;
                    }
                    else
                    rocketYSpeed--;
                } else if (e.VK_DOWN == e.getKeyCode()) {
                    
                    if(rocketOK2)
                    {
                        rocketYSpeed+=7;
                        rocketOK2=false;
                    }
                    else
                        rocketYSpeed++;
                } else if (e.VK_LEFT == e.getKeyCode()) {
                    if(rocketXSpeed>=-15)
                    {
                        rocketXSpeed--;
                        for (int index=0;index<numStars;index++)
                        {
                        if(rocketXSpeed==-1)
                        rocketScale=-1;
                        }
                    }
                }
                    else if (e.VK_RIGHT == e.getKeyCode()) {
                    if(rocketXSpeed<=15)
                    {
                        rocketXSpeed++;
                        for (int index=0;index<numStars;index++)
                        {
                            if(rocketXSpeed==1)
                            rocketScale=1;
                        }
                    }
                }
                else if (e.VK_SPACE == e.getKeyCode()) {
                    for (int index=0;index<missiles.length;index++)
                    {
                        missiles[index].active = true;
                        missiles[index].xpos = rocketXPos;
                        missiles[index].ypos = rocketYPos;
                    
//Move on to the next missile.                    
                        missiles[index].current++;
                        if (missiles[index].current >= missiles.length)
                        missiles[index].current = 0;
                    }
        
                }
                
                else if (e.VK_INSERT == e.getKeyCode()) {
                    zsound = new sound("ouch.wav");                    
                }
                repaint();
            }
        });
        init();
        start();
    }
    Thread relaxer;
////////////////////////////////////////////////////////////////////////////
    public void init() {
        requestFocus();
    }
////////////////////////////////////////////////////////////////////////////
    public void destroy() {
    }



////////////////////////////////////////////////////////////////////////////
    public void paint(Graphics gOld) {
        if (image == null || xsize != getSize().width || ysize != getSize().height) {
            xsize = getSize().width;
            ysize = getSize().height;
            image = createImage(xsize, ysize);
            g = (Graphics2D) image.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }
//fill background
        g.setColor(Color.cyan);
        g.fillRect(0, 0, xsize, ysize);

        int x[] = {getX(0), getX(getWidth2()), getX(getWidth2()), getX(0), getX(0)};
        int y[] = {getY(0), getY(0), getY(getHeight2()), getY(getHeight2()), getY(0)};
//fill border
        g.setColor(Color.black);
        g.fillPolygon(x, y, 4);
// draw border
        g.setColor(Color.red);
        g.drawPolyline(x, y, 5);

        if (animateFirstTime) {
            gOld.drawImage(image, 0, 0, null);
            return;
        }

        g.drawImage(outerSpaceImage,getX(0),getY(0),
                getWidth2(),getHeight2(),this);

         for (int index=0;index<numStars;index++)
        {
            drawCircle(getX(starXPos[index]), getYNormal(starYPos[index]), 0, 1, 1);
            drawRocket(rocketImage,getX(rocketXPos),getYNormal(rocketYPos),0.0,1.0*rocketScale,1.0*rocketScale );
        }
         for (int index=0;index<missiles.length;index++)
        {
         
         if (missiles[index].active)
            {
                drawMissile(coolpic,getX(missiles[index].xpos), getYNormal(missiles[index].ypos), 0, 1, 1);
            }
        }
         
         drawRocket(coolpic,getX(rocketXPos),getYNormal(rocketYPos),0.0,3.0*rocketScale,9.0*rocketScale );
         
         

        gOld.drawImage(image, 0, 0, null);
    }
    ////////////////////////////////////////////////////////////////////////////
    public void drawMissile(Image image,int xpos,int ypos,double rot,double xscale,
            double yscale) {
        int width = coolpic.getWidth(this);
        int height = coolpic.getHeight(this);
        g.translate(xpos,ypos);
        g.rotate(rot  * Math.PI/180.0);
        g.scale( xscale , yscale );

        g.drawImage(image,-width/2,-height/2,
        width,height,this);

        g.scale( 1.0/xscale,1.0/yscale );
        g.rotate(-rot  * Math.PI/180.0);
        g.translate(-xpos,-ypos);
    }
//    public void drawMissile(int xpos,int ypos,double rot,double xscale,double yscale)
//    {
//        g.translate(xpos,ypos);
//        g.rotate(rot  * Math.PI/180.0);
//        g.scale( xscale , yscale );
//
//        g.setColor(Color.white);
//        g.fillRect(-6,-2,12,4);
//
//        g.scale( 1.0/xscale,1.0/yscale );
//        g.rotate(-rot  * Math.PI/180.0);
//        g.translate(-xpos,-ypos);
//    }
////////////////////////////////////////////////////////////////////////////
    public void drawCircle(int xpos,int ypos,double rot,double xscale,double yscale)
    {
        g.translate(xpos,ypos);
        g.rotate(rot  * Math.PI/180.0);
        g.scale( xscale , yscale );

        g.setColor(Color.yellow);
        g.fillOval(-10,-10,20,20);

        g.scale( 1.0/xscale,1.0/yscale );
        g.rotate(-rot  * Math.PI/180.0);
        g.translate(-xpos,-ypos);
    }
    ////////////////////////////////////////////////////////////////////////////
    public void drawYee(Image image,int xpos,int ypos,double rot,double xscale,
            double yscale) {
        int width = coolpic.getWidth(this);
        int height = coolpic.getHeight(this);
        g.translate(xpos,ypos);
        g.rotate(rot  * Math.PI/180.0);
        g.scale( xscale , yscale );

        g.drawImage(image,-width/2,-height/2,
        width,height,this);

        g.scale( 1.0/xscale,1.0/yscale );
        g.rotate(-rot  * Math.PI/180.0);
        g.translate(-xpos,-ypos);
    }
////////////////////////////////////////////////////////////////////////////
    public void drawRocket(Image image,int xpos,int ypos,double rot,double xscale,
            double yscale) {
        int width = rocketImage.getWidth(this);
        int height = rocketImage.getHeight(this);
        g.translate(xpos,ypos);
        g.rotate(rot  * Math.PI/180.0);
        g.scale( xscale , yscale );

        g.drawImage(image,-width/2,-height/2,
        width,height,this);

        g.scale( 1.0/xscale,1.0/yscale );
        g.rotate(-rot  * Math.PI/180.0);
        g.translate(-xpos,-ypos);
    }
////////////////////////////////////////////////////////////////////////////
// needed for     implement runnable
    public void run() {
        while (true) {
            animate();
            repaint();
            double seconds = 0.04;    //time that 1 frame takes.
            int miliseconds = (int) (1000.0 * seconds);
            try {
                Thread.sleep(miliseconds);
            } catch (InterruptedException e) {
            }
        }
    }
/////////////////////////////////////////////////////////////////////////
    public void reset() {

//init the location of the rocket to the center.
        rocketXPos = getWidth2()/2;
        rocketYPos = getHeight2()/2;

        rocketXSpeed = 0;
        rocketYSpeed = 0;
        
        whichStarHit = -1;
        starXPos = new int[numStars];
        starYPos = new int[numStars];
        
        Missile.current = 0;
      //  missiles[] = new Missile[Missile.numMissiles];
        for (int index=0;index<missiles.length;index++)
        {
            missiles[index]=new Missile();
        }
        
        for (int index=0;index<numStars;index++)
        {
            starXPos[index] = (int)(Math.random()*getWidth2());
            starYPos[index] = (int)(Math.random()*getHeight2());
        }
    }
/////////////////////////////////////////////////////////////////////////
    public void animate() {
        if (animateFirstTime) {
            animateFirstTime = false;
            if (xsize != getSize().width || ysize != getSize().height) {
                xsize = getSize().width;
                ysize = getSize().height;
            }
            readFile();
            outerSpaceImage = Toolkit.getDefaultToolkit().getImage("./outerSpace.jpg");
            rocketImage = Toolkit.getDefaultToolkit().getImage("./rocket.GIF");
            coolpic = Toolkit.getDefaultToolkit().getImage("C:\\Users\\373000964\\Downloads\\coolpic.jpg");
            reset();
            bgSound = new sound("./starwars.wav");
        }

        if (bgSound.donePlaying)
        {
            bgSound = new sound("./starwars.wav");
        }
        
        for (int index=0;index<numStars;index++)
        {
            starXPos[index]-=rocketXSpeed;
        }
        rocketYPos-=rocketYSpeed;
        if(rocketYPos>=getHeight2())
        {
            rocketYSpeed=0;
            rocketOK2=true;
        }
        if(rocketYPos<=0)
        {
            rocketYSpeed=0;
            rocketOK1=true;
        }
        for (int index=0;index<numStars;index++)
        {
            if(starXPos[index]>getWidth2())
            {
                starXPos[index] = 0;
            }
            if(starXPos[index]<0)
            {
                starXPos[index] = getWidth2();
                starYPos[index] = (int)(Math.random()*getHeight2());
            }
        }
        
        
        for (int index=0;index<missiles.length;index++)
        {
                if (missiles[index].active)
            {
                missiles[index].xpos+=7;
                if (missiles[index].xpos > getWidth2())
                {
                    missiles[index].active = false;
                }
            }
        }
        
        
        boolean hit = false;
        for (int index=0;index<numStars;index++)
        {
            
            if(rocketXPos>=starXPos[index]-10 && rocketXPos <= starXPos[index]+10 
                    && rocketYPos >= starYPos[index]-10 && rocketYPos <= starYPos[index]+10)
            {
                hit = true;
                if(index!=whichStarHit)
                {
                    zsound = new sound("./ouch.wav"); 
                    whichStarHit=index;
                }
                whichStarHit=index;
            }
        }
        if(!hit)
            whichStarHit=-1;
        
    }

////////////////////////////////////////////////////////////////////////////
    public void start() {
        if (relaxer == null) {
            relaxer = new Thread(this);
            relaxer.start();
        }
    }
////////////////////////////////////////////////////////////////////////////
    public void stop() {
        if (relaxer.isAlive()) {
            relaxer.stop();
        }
        relaxer = null;
    }
/////////////////////////////////////////////////////////////////////////
    public int getX(int x) {
        return (x + XBORDER);
    }

    public int getY(int y) {
        return (y + YBORDER + YTITLE);
    }

    public int getYNormal(int y) {
        return (-y + YBORDER + YTITLE + getHeight2());
    }
    
    
    public int getWidth2() {
        return (xsize - getX(0) - XBORDER);
    }

    public int getHeight2() {
        return (ysize - getY(0) - YBORDER);
    }
    
    public void readFile() {
        try {
            String inputfile = "info.txt";
            BufferedReader in = new BufferedReader(new FileReader(inputfile));
            String line = in.readLine();
            while (line != null) {
                String newLine = line.toLowerCase();
                if (newLine.startsWith("numstars"))
                {
                    String numStarsString = newLine.substring(9);
                    numStars = Integer.parseInt(numStarsString.trim());
                }
                line = in.readLine();
            }
            in.close();
        } catch (IOException ioe) {
        }
    }
}

class sound implements Runnable {
    Thread myThread;
    File soundFile;
    public boolean donePlaying = false;
    sound(String _name)
    {
        soundFile = new File(_name);
        myThread = new Thread(this);
        myThread.start();
    }
    public void run()
    {
        try {
        AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);
        AudioFormat format = ais.getFormat();
    //    System.out.println("Format: " + format);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        SourceDataLine source = (SourceDataLine) AudioSystem.getLine(info);
        source.open(format);
        source.start();
        int read = 0;
        byte[] audioData = new byte[16384];
        while (read > -1){
            read = ais.read(audioData,0,audioData.length);
            if (read >= 0) {
                source.write(audioData,0,read);
            }
        }
        donePlaying = true;

        source.drain();
        source.close();
        }
        catch (Exception exc) {
            System.out.println("error: " + exc.getMessage());
            exc.printStackTrace();
        }
    }

}

class Missile
{
    public static int current;
    public static final int numMissiles = 10;
    public int xpos;
    public int ypos;
    public boolean active;
    public boolean right;
    
    Missile()
    {
        active = false;
    }
    
}