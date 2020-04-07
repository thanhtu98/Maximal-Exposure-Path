package bestpoint;

public class Sensor {
	double x;
	double y;
	int r;
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public int getR() {
		return r;
	}
	public void setR(int r) {
		this.r = r;
	}
	@Override
	public String toString() {
		return "Sensor [x=" + x + ", y=" + y + ", r=" + r + "]";
	}
	
}
