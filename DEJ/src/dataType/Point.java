package dataType;

public class Point {
	private double x;
	private double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public void setX(double x) {
		this.x = x;
	}

	public void moveUp() {
		this.y--;
	}

	public void moveDown() {
		this.y++;
	}

	public void moveLeft() {
		this.x--;
	}

	public void moveRight() {
		this.x++;
	}
}
