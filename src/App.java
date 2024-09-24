import javax.swing.JFrame;
public class App 
{
    public static void main(String[] args) 
    {
        int height=550;
        int width=1000;

        JFrame frame=new JFrame("Flappy Bird Game");
       // frame.setVisible(true);
        frame.setSize(width,height);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
        flappyBird obj1=new flappyBird();//here we are calling the flappyBird class and creating an object
        frame.add(obj1);
        frame.pack();             
        obj1.requestFocus();
        frame.setVisible(true);
    }
}
