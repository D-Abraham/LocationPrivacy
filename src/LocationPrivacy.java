import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class LocationPrivacy extends JPanel {

    private Point.Double[] users;
    private   Point.Double[] cr;

    public LocationPrivacy(){
        users = new Point.Double[1000];
        cr = new Point.Double[20];
    }
    public void readDB(String usersXYfileName, String crFileName){

        try{
            BufferedReader buffereReader = new BufferedReader(new FileReader(usersXYfileName));
            String line = null;
            int counter=0;
            line = buffereReader.readLine();//skipping 1st line
            while ((line = buffereReader.readLine()) != null){
                String[] lineSplit = line.split(" ");
                users[counter]=new Point.Double();
                users[counter].setLocation(Double.parseDouble(lineSplit[0]),Double.parseDouble(lineSplit[1]));
                counter++;
            }
            buffereReader.close();
            //reading Input-CR.txt
            int indix=0;
            buffereReader = new BufferedReader(new FileReader(crFileName));
            line = buffereReader.readLine();// skipping 1st line
            for(int row=0; row<10; row++){
                line = buffereReader.readLine();
                String[] lineSplit = line.split("[ a-zA-Z--]+");
                cr[indix]= new Point.Double();
                cr[indix].setLocation(Double.parseDouble(lineSplit[0]),Double.parseDouble(lineSplit[1]));
                cr[indix+1]= new Point.Double();
                cr[indix+1].setLocation(Double.parseDouble(lineSplit[2]),Double.parseDouble(lineSplit[3]));
                indix+=2;
            }
            double temp=cr[18].getY();
            cr[18].setLocation(cr[18].getX(),cr[19].getY());
            cr[19].setLocation(cr[19].getX(),temp);
            buffereReader.close(); //close file
        }
        catch (FileNotFoundException e){
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getDistance(Point.Double a, Point.Double b){
        return Math.sqrt((Math.pow((a.getX()-b.getX()),2))+(Math.pow((a.getY()-b.getY()),2)));
    }

    public Point.Double getMidPoint(Point.Double a, Point.Double b){

        return new Point.Double(((a.getX()+b.getX())/2),((a.getY()+b.getY())/2));
    }

    public ArrayList<Point.Double> getUsersInRC(Point.Double min, Point.Double max){
        ArrayList<Point.Double> usersInRc = new ArrayList<>();
        for(int i=0; i<1000; i++){
            if(((users[i].getX()<=max.getX())&&(users[i].getX()>=min.getX()))&&
                    ((users[i].getY()<=max.getY())&&(users[i].getY()>=min.getY()))){
                usersInRc.add(users[i]);
            }
        }
        return usersInRc;
    }

    public Point.Double getSource(ArrayList<Point.Double> usersCr,Point.Double midPoint){

        double shortestDis = Integer.MAX_VALUE;
        int index=0;
        for(int i=0; i<usersCr.size();i++){
            if(getDistance(midPoint,usersCr.get(i))<shortestDis){
                shortestDis = getDistance(midPoint,usersCr.get(i));
                index=i;
            }
        }
        return usersCr.get(index);
    }

    public void paintComponent(Graphics g){
        for(int i=0; i<users.length;i++){
            g.drawRect((int)users[i].getX(),(int)users[i].getY(),2,2);
        }
        for(int i=0; i<20; i+=2){
            g.setColor(Color.green);
            g.drawLine((int)cr[i].getX(),(int)cr[i].getY(),(int)cr[i+1].getX(),(int)cr[i+1].getY());
            g.drawLine((int)cr[i].getX(),(int)cr[i+1].getY(),(int)cr[i+1].getX(),(int)cr[i].getY());
            g.drawLine((int)cr[i].getX(),(int)cr[i].getY(),(int)cr[i+1].getX(),(int)cr[i].getY());
            g.drawLine((int)cr[i].getX(),(int)cr[i+1].getY(),(int)cr[i+1].getX(),(int)cr[i+1].getY());
            g.drawLine((int)cr[i].getX(),(int)cr[i].getY(),(int)cr[i].getX(),(int)cr[i+1].getY());
            g.drawLine((int)cr[i+1].getX(),(int)cr[i].getY(),(int)cr[i+1].getX(),(int)cr[i+1].getY());
            g.setColor(Color.red);
            g.drawString("CR "+i/2,(int)cr[i].getX(),(int)cr[i].getY());
            Point.Double midP = getMidPoint(cr[i],cr[i+1]);
            g.fillRect((int)midP.getX(),(int)midP.getY(),2,2);
        }

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1100, 1100);
    }
    public static void main(String[] args) {
        LocationPrivacy lp =  new LocationPrivacy();
        lp.readDB("Users.txt", "Input-CR.txt");
        ArrayList<Point.Double>crUsers;
        Point.Double midP;
        int count = 0;
        for(int cr=0; cr<20; cr+=2){
            System.out.println("CR "+count);
            crUsers = lp.getUsersInRC(lp.cr[cr],lp.cr[cr+1]);
            System.out.println("total users inside CR: "+crUsers.size()+"\nUsers coordinates: ");
            for(int i=0; i<crUsers.size();i++){
                System.out.println(crUsers.get(i).getX()+" - "+crUsers.get(i).getY());
            }
            count++;
            midP=lp.getMidPoint(lp.cr[cr],lp.cr[cr+1]);

            System.out.println("CR MidPoint(center): "+midP.getX()+" - "+midP.getY());
            Point.Double source = lp.getSource(crUsers,midP);
            System.out.println("Query source "+source.getX()+" - "+source.getY());
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame();
                frame.add(lp);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

}
