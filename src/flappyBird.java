import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.*;
//Image,Graphics,TImer are under java.awt.*  package

public class flappyBird extends JPanel implements ActionListener,KeyListener
{
    int boardwidth=1000;
    int boardheight=550;

    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image buttomPipeImg;

    class Bird      //class is declared as local
    {
        int X=boardwidth/8;
        int Y=boardheight/2;
        int birdWidth=50;
        int birdHeight=40;
        Image img;

        Bird(Image img)
        {
            this.img=img;
        }
    }
    class Pipes  //class is declared as local
    {
        int pipex=boardwidth;
        int pipey=0;
        int pipeheigth=350;
        int pipewidth=70;
        Image img;
        boolean pass=false;

        Pipes(Image img)
        {
            this.img=img;
        }
    }

    //game play logic building
    Bird bird;           //we cann access the fields of Bird Class
    Timer gameLoop;      //for running Frames
    Timer PipesTimer;
    int moveBirdY=0;
    //int movePipeX=-5;
    ArrayList<Pipes> PipeList;   //as we have many pipes wee need to store the in list of Pipes class 
   // Random ran=new Random();
    boolean isOver=false;
    double score=0;

    flappyBird()    //constructor
    {
        setPreferredSize(new Dimension(boardwidth,boardheight));
        setFocusable(true);//this this the one that take key events 
        addKeyListener(this);        //we check the 3 functions 

        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdBackgroundImage.jpeg")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        buttomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        //storing the birdImg in bird OBJECT
        bird=new Bird(birdImg);

        //creating the ARRAYLIST of pipes as many pipes are required of Pipes class
        PipeList=new ArrayList<>();

        //game timer for frames
        gameLoop=new Timer(1000/60,this); 
        gameLoop.start();  //Starts the Timer, causing it to start sending action events to its listeners

        //make the pipes move to the left
        PipesTimer=new Timer(2000, (ActionEvent e) -> {
            placepipes();     //the placepipes method is called
        } // when ever this part is invoked the placepipes function will be called
        );
        PipesTimer.start();
    }

    public void placepipes()
    {
        Pipes topPipe=new Pipes(topPipeImg);
        int r=(int)(topPipe.pipey - topPipe.pipeheigth/4 - Math.random() * topPipe.pipeheigth/2);
        topPipe.pipey=r;
        PipeList.add(topPipe);

        Pipes btmPipe=new Pipes(buttomPipeImg);
        btmPipe.pipey=topPipe.pipey + topPipe.pipeheigth + 150;
        PipeList.add(btmPipe);
    }
    //we have initialise all the images in the 4 variables of image type
    //now we will draw the images in the background
    //Uses the drawImage method from the Graphics object to actually draw the backgroundImg on the panel.

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);   //calling the paintComponent
        draw(g);                   //passing g to draw method
    }
    public void draw(Graphics g)
    {
        //draw background
        g.drawImage(backgroundImg,0,0,boardwidth,boardheight,null);

        //draw bird
        g.drawImage(bird.img,bird.X,bird.Y,bird.birdWidth,bird.birdHeight,null);

        //draw pipes
        for(int i=0;i<PipeList.size();i++)
        {
            Pipes p=PipeList.get(i);
            g.drawImage(p.img,p.pipex,p.pipey,p.pipewidth,p.pipeheigth,null);
        }
        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (isOver) {
            g.drawString("Game Over.Your Score:" + String.valueOf((int) score), 10, 35);
        }
        else {
            g.drawString("Score:"+String.valueOf((int) score), 10, 35);
        }
    }
    
    public void move()
    {
        //bird move
        moveBirdY=moveBirdY+1;
        bird.Y=bird.Y+moveBirdY;
        bird.Y=Math.max(bird.Y,0);

        //pipe move
        for(int i=0;i<PipeList.size();i++)
        {
            Pipes p=PipeList.get(i);
            p.pipex=p.pipex-4;

            if(!p.pass && bird.X > (p.pipex + p.pipewidth))
            {
                p.pass=true;
                score+=0.5;
            }
            if(bird.Y >boardheight)
            {
                isOver=true;
            }
            else if(collision(bird, p))
            {
                isOver=true;
            }
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) 
    {        
        move();
        repaint();
        if(isOver)
        {
            gameLoop.stop();
            PipesTimer.stop();
        }
    }
    boolean collision(Bird a, Pipes b) {
                 return (a.X < b.pipex + b.pipewidth &&   //a's top left corner doesn't reach b's top right corner
                        a.X + a.birdWidth > b.pipex &&   //a's top right corner passes b's top left corner
                        a.Y < b.pipey + b.pipeheigth &&  //a's top left corner doesn't reach b's bottom left corner
                        a.Y + a.birdHeight > b.pipey);    //a's bottom left corner passes b's top left corner
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_SPACE)//when we press on spacebar this function is  triggered
        {
            moveBirdY=-9;
            if (isOver) 
            {
                //restart game by resetting conditions
                 bird.Y = boardheight/2;
                 moveBirdY=0;
                PipeList.clear();
                isOver = false;
                score = 0;
                gameLoop.start();
                PipesTimer.start();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}
