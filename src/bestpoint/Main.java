package bestpoint;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Main {

    private final static double W = 200;
    private final static double H = 100;
    final static int N = 50;
    final static int rMax = 15;
    private final static double vMax = 2; // 2m/s
    private final static double delS = 0.5;
    private final static double tMax = 120;// 120s
    private static Random rd = new Random();
    private static Point startPoint = new Point(0.0, rd.nextInt(200) * delS);
    private static Point finishPoint = new Point(200.0, rd.nextInt(200) * delS);
    static double yMax = Math.max(finishPoint.getY(), startPoint.getY());
    static double yMin = Math.min(finishPoint.getY(), startPoint.getY());
    private static double calLength(Point a, Sensor b) {
        return Math.sqrt((Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2)));
    }

    private List<Sensor> readSensor() throws IOException {
        List<Sensor> sensorList = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File("sensor.txt")));
            String line;
            while ((line = br.readLine()) != null) {
                String[] coordinatesString = line.split(";");
                sensorList.add(new Sensor(Double.parseDouble(coordinatesString[0]),
                        Double.parseDouble(coordinatesString[1]),
                        Double.parseDouble(coordinatesString[2]))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        br.close();
        return sensorList;
    }

    public void generateSensor(int numSensor, int rMax) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("sensor.txt"));
        for (int i = 0; i < numSensor; i++) {
            double x = rd.nextDouble() * W;
            double y = rd.nextDouble() * H;
            double r = rd.nextInt(rMax) + 1;
            bw.write(x + ";" + y + ";" + r);
            bw.newLine();
        }
        bw.close();
    }
    public static Point findMaxExposure(List<Point>[] p,int col){
        int rowStart = (int) (startPoint.getX()/delS);
        int rowFinish = (int) (finishPoint.getX()/delS);
        int rowMax = (int) (H/delS);
        Point rs;
        if(Math.min(col,rowStart) == col) {
            rs = new Point(col * delS, 0.0);
        }
        else{
            rs = startPoint;
        }
        for(int i= Math.min(col,rowStart)+1;i<Math.max(col,rowStart);i++){
            if(p[0].get(i).getI()> rs.getI()){
                rs = p[0].get(i);
            }
        }
        for(int j=0;j<(int)(H/delS);j++){
            if(rs.getI()<p[j].get(Math.max(col,rowStart)).getI()){
                rs = p[j].get(Math.max(col,rowStart));
            }
        }
        int start = Math.max(col,rowStart);
        for(int k= Math.min(start,rowFinish)+1;k<Math.max(start,rowFinish);k++){
            if(rs.getI()<p[rowMax].get(k).getI()){
                rs = p[rowMax].get(k);
            }
        }
        return rs;
    }
    public static double calEx(List<Point>[] p, int idx, double t) {
        double rs = 0;
        int i = (int) ((startPoint.getX()) / delS);// col start
        int k = (int) ((finishPoint.getX()) / delS);// col finish
        Point p1 = findMaxExposure(p, idx);
        int xmax = (int) (W / delS + 1);
        if (i < idx) {
            for (int j = i; j < idx; j++) {
                if (p[0].get(j).equals(p1)) {
                    break;
                }
                rs += (p[0].get(j).getI() + p[0].get(j + 1).getI()) / 2;
            }
            for (int m = 0; m < xmax; m++) {
                if (p[idx].get(m).equals(p1)) {
                    break;
                }
                rs += (p[idx].get(m).getI() + p[idx].get(m + 1).getI()) / 2;
            }
            if (k < idx) {
                for (int n = idx; n > k; n--) {
                    if (p[xmax - 1].get(n).equals(p1)) {
                        break;
                    }
                    rs += (p[xmax - 1].get(n).getI() + p[xmax - 1].get(n - 1).getI()) / 2;
                }
            } else {
                for (int n = idx; n < k; n++) {
                    if (p[xmax - 1].get(n).equals(p1)) {
                        break;
                    }
                    rs += (p[xmax - 1].get(n).getI() + p[xmax - 1].get(n + 1).getI()) / 2;
                }
            }
        } else if (i > idx) {
            for (int j = i; j > idx; j--) {
                if (p[0].get(j).equals(p1)) {
                    break;
                }
                rs += (p[0].get(j).getI() + p[0].get(j - 1).getI()) / 2;
            }
            for (int m = 0; m < xmax; m++) {
                if (p[idx].get(m).equals(p1)) {
                    break;
                }
                rs += (p[idx].get(m).getI() + p[idx].get(m + 1).getI()) / 2;
            }
            if (k < idx) {
                for (int n = idx; n > k; n--) {
                    if (p[xmax - 1].get(n).equals(p1)) {
                        break;
                    }
                    rs += (p[xmax - 1].get(n).getI() + p[xmax - 1].get(n - 1).getI()) / 2;
                }
            } else {
                for (int n = idx; n < k; n++) {
                    if (p[xmax - 1].get(n).equals(p1)) {
                        break;
                    }
                    rs += (p[xmax - 1].get(n).getI() + p[xmax - 1].get(n + 1).getI()) / 2;
                }
            }
        } else {
            for (int l = 0; l < xmax; l++) {
                if (p[i].get(l).equals(p1)) {
                    break;
                }
                rs += (p[i].get(l).getI() + p[i].get(l + 1).getI()) / 2;
            }

        }

        return rs * t + p1.getI() * (tMax - t);
    }
    public static void main(String[] args) throws IOException {
        Main main = new Main();
        List<Sensor> listS =main.readSensor();

        Point t = new Point(0.0, 0.0);
        int len = (int) (H / delS + 1);
        int row = (int) (W / delS + 1);
        List<Point>[] listP = new List[len];
        for (int i = 0; i < len; i++) {
            listP[i] = new ArrayList<>();
        }
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < row; j++) {
                int it = 0;
                for (int k = 0; k < listS.size(); k++) {
                    if (calLength(t, listS.get(k)) < listS.get(k).getR()) {
                        it++;
                    }
                }
                listP[i].add(new Point(t.getX(), t.getY(), it));
                t.setX(t.getX() + delS);
            }
            t.setY(t.getY() + delS);
            t.setX(0.0);
        }

        double stp = W + yMax - yMin;// do dai duong di ngan nhat

        double tm = stp / vMax - delS / vMax;
        double timeStop = tMax - tm;
        double dt = delS / vMax;

        double[] maxE = new double[listP.length];

        System.out.println("diem bat dau: " + finishPoint.toString() + "\ndiem ket thuc: " + finishPoint.toString());
        System.out.println("thoi gian dung lai: " + timeStop);
        System.out.println("thoi gian di chuyen: " + dt);
        Double maxEP = Double.MIN_VALUE;
        int idx = -1;
        for (int i = 0; i < len; i++) {
			/*if (maxE[i] > maxEP) {
				maxEP = maxE[i];
				idx = i;
			}*/
            System.out.println(findMaxExposure(listP, i).toString());
        }
        System.out.println("gia tri maximal exposure path la: " + maxEP + " duong di qua canh co tung do la: "
                + (idx * delS + yMin));
    }
}
