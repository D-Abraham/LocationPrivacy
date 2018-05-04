import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LocationPrivacy {
    static Double[][] xy = new Double[2][1000];
    static Double[][] crXY = new Double[10][4];

    static void readDB(String usersXYfileName, String crFileName){

        try{
            //reading Users.txt
            BufferedReader buffereReader = new BufferedReader(new FileReader(usersXYfileName));
            String line = null;
            int counter=0;
            line = buffereReader.readLine();//skipping 1st line
            while ((line = buffereReader.readLine()) != null){
                //System.out.println(line);
                String[] lineSplit = line.split(" ");
                xy[0][counter]=Double.parseDouble(lineSplit[0]);
                xy[1][counter] = Double.parseDouble(lineSplit[1]);
                counter++;
            }
            buffereReader.close();
            //reading Input-CR.txt
            buffereReader = new BufferedReader(new FileReader(crFileName));
            line = buffereReader.readLine();// skipping 1st line
            for(int row=0; row<10; row++){
                line = buffereReader.readLine();
                String[] lineSplit = line.split("[ a-zA-Z--]+");
                crXY[row][0] = Double.parseDouble(lineSplit[0]);
                crXY[row][1] = Double.parseDouble(lineSplit[1]);
                crXY[row][2] = Double.parseDouble(lineSplit[2]);
                crXY[row][3] = Double.parseDouble(lineSplit[3]);
            }

            buffereReader.close(); //close file
        }
        catch (FileNotFoundException e){
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static double getDistance(double x1, double y1, double x2, double y2){
        return Math.sqrt((Math.pow((x1-x2),2))+(Math.pow((y1-y2),2)));
    }

    public static void main(String[] args) {
        readDB("Users.txt", "Input-CR.txt");
        for (int pos = 0; pos < 1000; pos++) {
            System.out.println(xy[0][pos] + " - " + xy[1][pos]);
        }

        for (int row = 0; row < 10; row++) {
            for (int column = 0; column < 4; column++) {
                System.out.print(crXY[row][column] + "  ");
                if (column == 3) {
                    System.out.println();
                }
            }
        }

        System.out.println(getDistance(-6,-4,1,7));

    }
}
