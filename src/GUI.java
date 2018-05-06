import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GUI extends JPanel {
    LocationPrivacy lp;
    ArrayList<Point.Double> pSources;
    public GUI(){
        lp = new LocationPrivacy();
        lp.readDB("Users.txt", "Input-CR.txt");
        pSources = new ArrayList<Point.Double>(); // holds all(10) source points
        ArrayList<Point.Double> crUsers;
        int count = 0;
        for(int cr=0; cr<20; cr+=2){
            System.out.print("CR "+count);
            crUsers = lp.getUsersInRC(lp.getCr()[cr],lp.getCr()[cr+1]);
            System.out.print(" total users inside: "+crUsers.size());
            count++;
            Point.Double source = lp.getSource(crUsers);
            System.out.println(" Query source "+source.getX()+" - "+source.getY());
            pSources.add(source);
        }
        int[] indexs=lp.getSourcesIndexs(lp.getUsers(),pSources);
        System.out.println("Query source ID(index):");
        for(int i=0; i<indexs.length;i++){
            System.out.println(i+"- "+indexs[i]);
        }
        System.out.println("Note: ID(index)+2 == line # in Users.txt, because the first line is used by a text.");
    }

    public void paintComponent(Graphics g){
        for(int i=0; i<lp.getUsers().length;i++){
            g.drawRect((int)lp.getUsers()[i].getX(),(int)lp.getUsers()[i].getY(),2,2);
        }
        for(int i=0; i<20; i+=2){
            g.setColor(Color.green);
            g.drawLine((int)lp.getCr()[i].getX(),(int)lp.getCr()[i].getY(),(int)lp.getCr()[i+1].getX(),(int)lp.getCr()[i].getY());
            g.drawLine((int)lp.getCr()[i].getX(),(int)lp.getCr()[i+1].getY(),(int)lp.getCr()[i+1].getX(),(int)lp.getCr()[i+1].getY());
            g.drawLine((int)lp.getCr()[i].getX(),(int)lp.getCr()[i].getY(),(int)lp.getCr()[i].getX(),(int)lp.getCr()[i+1].getY());
            g.drawLine((int)lp.getCr()[i+1].getX(),(int)lp.getCr()[i].getY(),(int)lp.getCr()[i+1].getX(),(int)lp.getCr()[i+1].getY());
            g.drawString("CR "+i/2,(int)lp.getCr()[i].getX(),(int)lp.getCr()[i].getY());

        }
        for (int i=0; i<pSources.size();i++){
            g.setColor(Color.red);
            g.drawRect((int)pSources.get(i).getX(),(int)pSources.get(i).getY(),2,2);
            g.drawString("-->Query source",(int)pSources.get(i).getX()+10,(int)pSources.get(i).getY()+5);

        }

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1100, 1100);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame();
                frame.add(new GUI());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
