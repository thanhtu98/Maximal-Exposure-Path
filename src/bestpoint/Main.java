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

	final static double W = 200;
	final static double H = 100;
	final static int N = 50;
	final static int rMax = 15;
	final static double vMax = 2; // 2m/s
	final static double delS = 0.5;
	final static double tMax = 200;// 200s
	static Random rd = new Random();
	static Point S = new Point(0.0, rd.nextInt(200) * delS);
	static Point F = new Point(200.0, rd.nextInt(200) * delS);
	static double yMax = Math.max(F.getY(), S.getY());
	static double yMin = Math.min(F.getY(), S.getY());

	static double calLength(Point a, Sensor b) {
		return Math.sqrt((Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2)));
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

	public static double calEx(List<Point>[] p, int row, int col, double t) {
		double rs = 0;
		int i = (int) ((S.getY() - yMin) / delS);// row start
		int k = (int) ((F.getY() - yMin) / delS);// row finish
		if (i < row) {
			for (int j = i; j < row; j++) {
				rs += (p[j].get(0).getI() + p[j + 1].get(0).getI()) / 2;
			}
			for (int m = i; m < k; m++) {
				rs += (p[m].get(0).getI() + p[m + 1].get(0).getI()) / 2;
			}
		} else if (i > row) {
			for (int j = i; j > row; j--) {
				rs += (p[j].get(0).getI() + p[j - 1].get(0).getI()) / 2;
			}
			for (int m = i; m > k; m--) {
				rs += (p[m].get(0).getI() + p[m - 1].get(0).getI()) / 2;
			}
		} else {
			for (int l = 0; l < 399; l++) {
				if (l == col) {
					l += 2;
				}
				rs += (p[i].get(l).getI() + p[i].get(l + 1).getI()) / 2;
			}

		}

		return rs * t;
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

		/*
		 * for (int i = 0; i < listS.size(); i++) {
		 * System.out.println(listS.get(i).toString()); }
		 */

		int itmax = 0;

		Point t = new Point(0.0, yMin);
		int len = (int) ((yMax - yMin) / delS + 1);
		int row = (int) (W / delS + 1);
		LinkedList<Point>[] listP = new LinkedList[len];
		for (int i = 0; i < len; i++) {
			listP[i] = new LinkedList<Point>();
		}
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < row; j++) {
				int it = 0;
				for (int k = 0; k < listS.size(); k++) {
					if (calLength(t, listS.get(k)) < listS.get(k).getR()) {
						it++;
					}
				}
				t.setI(it);
				listP[i].add(new Point(t.getX(), t.getY(), it));
				if (itmax < t.getI()) {
					itmax = t.getI();
				}
				t.setX(t.getX() + delS);
			}
			t.setY(t.getY() + delS);
			t.setX(0.0);
		}
		// System.out.println("it max la " + itmax);

		/*
		 * do { int it = 0; for (int i = 0; i < listS.size(); i++) { if (calLength(t,
		 * listS.get(i)) < listS.get(i).getR()) { it++; } } // t.setI(it);
		 * listP[c].add(new Point(t.getX(), t.getY(), it)); if (t.getX() == W) { c++; }
		 * if (itmax < t.getI()) { itmax = t.getI(); }
		 * 
		 * if (t.getX() != W && t.getY() != yMax) { t.setX(t.getX() + 0.5); } else if
		 * (t.getX() == W && t.getY() != yMax) { t.setX(0.0); t.setY(t.getY() + 0.5); }
		 * 
		 * } while (t.getX() != W && t.getY() != yMax);
		 */

		double stp = W + yMax - yMin;

		double dt = stp / vMax - delS / vMax;
		double timeStop = tMax - dt;

		double[] maxE = new double[listP.length];
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < row; j++) {
				if (listP[i].get(j).getI() == itmax) {
					maxE[i] = 1.0 * listP[i].get(j).getI() * timeStop + calEx(listP, i, j, dt);
					break;
				}

			}
		}

		System.out.println("diem bat dau: " + S.toString() + "\ndiem ket thuc: " + F.toString());
		// System.out.println("I max la " + itmax);
		System.out.println("thoi gian dung lai: " + timeStop);
		System.out.println("thoi gian di chuyen: " + dt);
		Double maxEP = Double.MIN_VALUE;
		int idx = -1;
		for (int i = 0; i < maxE.length; i++) {
			if (maxE[i] > maxEP) {
				maxEP = maxE[i];
				idx = i;
			}
		}
		System.out.println("gia tri maximal exposure path la: " + maxEP + " duong di qua canh co tung do la: "
				+ (idx * delS + yMin));

	}

}
