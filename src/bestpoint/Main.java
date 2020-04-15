package bestpoint;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

	final static double W = 100;
	final static double H = 100;
	final static double vMax = 5;
	final static double delS = 0.5;
	final static double tMax = 100;
	int N;
	Point S;
	Point F;
	LinkedList<Sensor> listS = new LinkedList<Sensor>();
	/*
	 * static Random rd = new Random(); static Point S = new Point(5.0, 0.0); static
	 * Point F = new Point(49.5, 100.0);
	 */

	static double calLength(Point a, Sensor b) {
		return Math.sqrt((Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2)));
	}

	public void initialFromFile(String file) throws Exception {
		Scanner sc = new Scanner(new BufferedReader(new FileReader(file)));
		String[] line = sc.nextLine().trim().split(" ");
		this.N = Integer.parseInt(line[0]);
		for (int i = 0; i < this.N; i++) {
			line = sc.nextLine().trim().split(" ");
			Double x = Double.parseDouble(line[0]);
			Double y = Double.parseDouble(line[1]);
			Double r = Double.parseDouble(line[2]);
			Sensor sensor = new Sensor(x, y, r);
			this.listS.add(sensor);
		}

		line = sc.nextLine().trim().split(" ");
		this.S = new Point(Double.parseDouble(line[0]), Double.parseDouble(line[1]));

		line = sc.nextLine().trim().split(" ");
		this.F = new Point(Double.parseDouble(line[0]), Double.parseDouble(line[1]));
		sc.close();

	}

	public void writeFile(String file, Point S, Point F, int idx) {
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(S.getX()+" "+ S.getY()+"\n");
			bw.write(idx+" 0.0\n");
			bw.write(idx+" 100.0\n");
			bw.write(F.getX()+" "+ F.getY()+"\n");
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Point findMax(List<Point>[] p, int col, Point S, Point F) {
		int rowStart = (int) (S.getX() / delS);
		int rowFinish = (int) (F.getX() / delS);
		int rowMax = (int) (W / delS + 1);
		Point rs = new Point();
		rs.setI(-1);

		for (int i = Math.min(col, rowStart) + 1; i < Math.max(col, rowStart); i++) {
			if (p[0].get(i).getI() > rs.getI()) {
				rs = p[0].get(i);
			}
		}
		for (int j = 0; j < (int) (H / delS + 1); j++) {
			if (rs.getI() < p[col].get(j).getI()) {
				rs = p[col].get(j);
			}
		}
		for (int k = Math.min(col, rowFinish); k < Math.max(col, rowFinish); k++) {
			if (rs.getI() < p[rowMax - 1].get(k).getI()) {
				rs = p[rowMax - 1].get(k);
			}
		}
		return rs;
	}

	public static double calEx(List<Point>[] p, int idx, Point S, Point F) {
		double rs = 0;
		int i = (int) ((S.getX()) / delS);// col start
		int k = (int) ((F.getX()) / delS);// col finish
		Point p1 = findMax(p, idx, S, F);
		int xmax = (int) (W / delS + 1);
		double stp = W + Math.abs((i - idx) * delS) + Math.abs((k - idx) * delS);// do dai duong di ket hop 2 duong ngan
																					// nhat
		double tm = stp / vMax;
		double timeStop = tMax - tm;

		for (int m = Math.min(i, idx); m < Math.max(i, idx); m++) {
			rs += (p[0].get(m).getI() + p[0].get(m + 1).getI()) / 2;
		}
		for (int m = 0; m < xmax - 1; m++) {
			rs += (p[idx].get(m).getI() + p[idx].get(m + 1).getI()) / 2;
		}
		for (int m = Math.min(i, k); m < Math.min(i, k); m++) {
			rs += (p[xmax - 1].get(m).getI() + p[xmax - 1].get(m + 1).getI()) / 2;
		}

		return rs * delS / vMax + p1.getI() * timeStop;
	}

	public static void main(String[] args) {
		// doc sensor tu file
		Main net = new Main();
		try {
			net.initialFromFile("C:\\Users\\Trant\\eclipse-workspace\\MaxExposurePath\\src\\20.txt");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Point t = new Point(0.0, 0.0);
		int len = (int) (H / delS + 1);
		int row = (int) (W / delS + 1);
		//tinh cuong do cam bien tai moi diem va luu lai
		LinkedList<Point>[] listP = new LinkedList[len];
		for (int i = 0; i < len; i++) {
			listP[i] = new LinkedList<Point>();
		}
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < row; j++) {
				int it = 0;
				for (int k = 0; k < net.listS.size(); k++) {
					if (calLength(t, net.listS.get(k)) < net.listS.get(k).getR()) {
						it++;
					}
				}
				listP[i].add(new Point(t.getX(), t.getY(), it));
				t.setX(t.getX() + delS);
			}
			t.setY(t.getY() + delS);
			t.setX(0.0);
		}
		// System.out.println("it max la " + itmax);

		double[] maxE = new double[listP.length];

		for (int i = 0; i < len; i++) {
			maxE[i] = calEx(listP, i, net.S, net.F);
		}

		System.out.println("diem bat dau: " + net.S.toString() + "\ndiem ket thuc: " + net.F.toString());
		/*
		 * System.out.println("thoi gian dung lai: " + timeStop);
		 * System.out.println("thoi gian di chuyen: " + dt);
		 */
		Double maxEP = Double.MIN_VALUE;
		int max = -1;
		for (int i = 0; i < maxE.length; i++) {
			if (maxEP < maxE[i]) {
				maxEP = maxE[i];
				max = i;
			}
		}
		//net.writeFile("output200.txt", net.S, net.F, max);
		System.out.println("gia tri maximal exposure path la: " + maxEP);
		System.out.println(max);
	}

}
