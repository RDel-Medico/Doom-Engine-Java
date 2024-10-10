package map;

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
	
	public Point minus(Point p) {
		return new Point(x - p.getX(), y - p.getY());
	}

	public void setX(int x) {
		this.x = x;
	}

	public void up() {
		this.y--;
	}

	public void down() {
		this.y++;
	}

	public void left() {
		this.x--;
	}

	public void right() {
		this.x++;
	}
}
