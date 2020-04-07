package bestpoint;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

public class Main {

	final static double W = 200;
	final static double H = 100;
	final static int N = 50;
	final static int rMax = 15;
	final static double vMax = 2; // 2m/s
	final static double delS = 0.5;
	final static double tMax = 120;// 120s
	static Random rd = new Random();
	static double calLength(Point a, Sensor b) {
		return Math.sqrt((Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2)));
	}
	
	public void generateSensor(int numSensor, int  rMax) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("sensor.txt"));
        for(int i=0;i<numSensor;i++){
            double x = rd.nextDouble()*W;
            double y = rd.nextDouble()*H;
            double r = rd.nextInt(rMax) +1;
            bw.write(x+";"+y+";"+r);
            bw.newLine();
        }
        bw.close();
    }
	public static void main(String[] args) {
		// khoi tao random sensor

		LinkedList<Sensor> listS = new LinkedList<Sensor>();
		
		/*
		 * try (BufferedReader br = new BufferedReader( new FileReader(new
		 * File("C:\\Users\\Trant\\eclipse-workspace\\MaxExposurePath\\src\\sensor.txt")
		 * ))) { String line; while ((line = br.readLine()) != null) { String[]
		 * coordinatesString = line.split(";"); Sensor s = new Sensor();
		 * s.setX(Double.parseDouble(coordinatesString[0]));
		 * s.setY(Double.parseDouble(coordinatesString[1]));
		 * s.setR(Integer.parseInt(coordinatesString[2])); listS.add(s); } } catch
		 * (Exception e) { e.printStackTrace(); }
		 */

		for (int i = 0; i < N; i++) {
			Sensor s = new Sensor();
			s.setX(rd.nextDouble() * W);
			s.setY(rd.nextDouble() * H);
			s.setR(rd.nextInt(rMax) + 1);
			listS.add(s);
		}
		Point S = new Point(0.0, rd.nextInt(200) * delS);
		Point F = new Point(200.0, rd.nextInt(200) * delS);
		/*
		 * for(int i=0; i<listS.size();i++) {
		 * System.out.println(listS.get(i).toString()); }
		 */
		LinkedList<Point> listP = new LinkedList<Point>();
		int itmax = 0;
		double yMax = Math.max(F.getI(), S.getY());
		double yMin = Math.min(F.getY(), S.getY());
		Point t = new Point(0.0, yMin);
		while (true) {
			int it = 0;
			for (int i = 0; i < listS.size(); i++) {
				if (calLength(t, listS.get(i)) < listS.get(i).getR()) {
					it++;
				}
			}
			t.setI(it);
			listP.add(t);

			if (itmax < t.getI()) {
				itmax = t.getI();
			}

			if (t.getX() == W && t.getY() != yMax) {
				t.setX(0.0);
				t.setY(t.getY() + 0.5);
			}
			t.setX(t.getX() + 0.5);
			if (t.getX() == W && t.getY() == yMax) {
				break;
			}
		}
		System.out.println(S.toString() + "\n" + F.toString());
		System.out.println("diem co I max la " + itmax);

	}

}
