package dataType;

public class Point {
	private int x;
	private int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public void setX(int x) {
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
