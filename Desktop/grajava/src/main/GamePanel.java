package main;

import Tile.TileManager;
import entity.Player;
import object.SuperObject;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {


    //SCREEN SETTINGS
    final int originalTileSize=16;
    final int scale = 3;
    public  final int tileSize = originalTileSize * scale; //48x48
    public final int maxScreenCol=16;
    public int maxScreenRow=12;
    public final int screenWidth=tileSize*maxScreenCol; //768 pixels
    public final int screenHeight=tileSize*maxScreenRow; //576 pixel
    // World settings
    public final int maxWorldCol=50;
    public final int maxWorldRow=50;
    public  final int worldWidth=tileSize*maxWorldCol;
    public  final int worldHeight=tileSize*maxWorldRow;
//FPS
    int FPS=60;
    TileManager tileM=new TileManager(this);
    public UI ui=new UI(this);
Thread gameThread;
KeyHandler keyH=new KeyHandler();
public CollisionChecker cChecker=new CollisionChecker(this);
public AssetSetter aSetter=new AssetSetter(this);
public Player player=new Player(this, keyH);
public SuperObject obj[]=new SuperObject[10];



    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true); //IMPROVE RENDERING PERFORMANCE
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }
    public void setupGame() {
        aSetter.setObject();

    }
    public void startGameThread()
    {
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
    public void run() {
        double drawInterval=1000000000/FPS;
        double nextDrawTime=System.nanoTime()+drawInterval;
        while (gameThread!=null) {


            update();

            repaint();

            try {
                double remainingTime=nextDrawTime-System.nanoTime();
                remainingTime/=10000000;
                if(remainingTime<0)
                {
                    remainingTime=0;
                }
                Thread.sleep((long)remainingTime);
                nextDrawTime+=drawInterval;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
    public void update()
    {

       player.update();
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //Tile
        tileM.draw(g2);
        //OBJECT
        for(int i=0;i<obj.length;i++)
        {
            if(obj[i]!=null) {
            obj[i].draw(g2,this);
            }
        }
        //Player
        player.draw(g2);
        //UI
        ui.draw(g2);
        g2.dispose();
    }

}
