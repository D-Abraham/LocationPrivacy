import java.awt.*;
import java.io.*;
import java.util.ArrayList;
/*
LocationPrivacy uses the provides users xy and CR to find the query source.
 */
public class LocationPrivacy {

    private Point.Double[] users;
    private   Point.Double[] cr;
    /**
     * Constructs a LocationPrivacy object.
     */
    public LocationPrivacy(){
        users = new Point.Double[1000];
        cr = new Point.Double[20];
    }
    /**
     * Reads users xy and CR from the specified files.
     * @param usersXYfileName file that contains users xy coordinates
     * @param crFileName file that contains CR coordinates
     */
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
                String[] lineSplit = line.split("[ a-zA-Z-]+");
                cr[indix]= new Point.Double();
                cr[indix].setLocation(Double.parseDouble(lineSplit[0]),Double.parseDouble(lineSplit[1]));
                cr[indix+1]= new Point.Double();
                cr[indix+1].setLocation(Double.parseDouble(lineSplit[2]),Double.parseDouble(lineSplit[3]));
                indix+=2;
            }
            cr[18].setLocation(cr[18].getX(),cr[18].getY()*-1);

        }
        catch (FileNotFoundException e){
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the distance between point a and point b.
     * @param a point a.
     * @param b point b.
     * @return returns distance.
     */
    public double getDistance(Point.Double a, Point.Double b){
        return Math.sqrt((Math.pow((a.getX()-b.getX()),2))+(Math.pow((a.getY()-b.getY()),2)));
    }

    /**
     * Returns an ArrayList of Points within the CR.
     * @param min CR minimum point.
     * @param max CR maximum point
     * @return Arraylist<Point.Double> of all points within the CR.
     */
    public ArrayList<Point.Double> getUsersInRC(Point.Double min, Point.Double max){
        ArrayList<Point.Double> usersInRc = new ArrayList<>();
        for(int i=0; i<1000; i++){
            if(((users[i].getX()<max.getX())&&(users[i].getX()>min.getX()))&&
                    ((users[i].getY()<max.getY())&&(users[i].getY()>min.getY()))){
                usersInRc.add(users[i]);
            }
        }
        return usersInRc;
    }

    /**
     * This method will find the source by finding a point with shortest total distance
     * between itself and other points in CR.
     * @param usersInCR ArrayList that hold all of users inside CR.
     * @return The query source point.
     */
    public Point.Double getSource(ArrayList<Point.Double> usersInCR){
        double shortestDis=Integer.MAX_VALUE;
        int userIndex=0;
        double[] Distance = new double[usersInCR.size()];
        double ave=0;
        for (int user=0; user<usersInCR.size();user++){
            ave=0;
            for (int compTo=0; compTo<usersInCR.size(); compTo++){
                if (compTo!=user){
                    ave += getDistance(usersInCR.get(user),usersInCR.get(compTo));
                }
            }
            Distance[user]=ave;
        }
        for (int i=0;i<Distance.length; i++){
            if (Distance[i] < shortestDis) {
                shortestDis = Distance[i];
                userIndex = i;
            }
        }
        return usersInCR.get(userIndex);
    }

    /**
     * Returns users.
     * @return users.
     */
    public Point.Double[] getUsers() {
        return users;
    }

    /**
     * Returns CR.
     * @return cr
     */
    public Point.Double[] getCr(){
        return cr;
    }

    /*
    This method was made only because I was not sure if it was required to return query index.
     */
    public  int[] getSourcesIndexs(Point.Double[] users,ArrayList<Point.Double> sources){
        int[] indexs = new int[10];
        for(int i=0; i<sources.size(); i++){
            for (int x=0; x<users.length; x++){
             if((sources.get(i).getX() == users[x].getX())&&(sources.get(i).getY()==users[x].getY())){
                 indexs[i]=x;
             }
            }
        }
        return indexs;
    }

}
