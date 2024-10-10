package map;

public class Segment {
	private Point a;
	private Point b;
	
	public Segment (Point a, Point b) {
		this.a = a;
		this.b = b;
	}
	
	public Segment (int mvtX, int mvtY) {
		this.a = new Point(0,0);
		this.b = new Point(mvtX, mvtY);
	}
	
	public Segment (int mvtX, int mvtY, int x, int y) {
		this.a = new Point(x,y);
		this.b = new Point(x+mvtX, y+mvtY);
	}

	public Point getA() {
		return a;
	}
	
	public void rotate(double yawDegrees) {
        // Convert degrees to radians
        double yawRadians = Math.toRadians(yawDegrees);

        // Get the current movement vector (x and y)
        int mvtX = getXMouvement();
        int mvtY = getYMouvement();

        // Apply the 2D rotation matrix
        double cosTheta = Math.cos(yawRadians);
        double sinTheta = Math.sin(yawRadians);

        // New x and y after rotation
        int newMvtX = (int) Math.round(mvtX * cosTheta - mvtY * sinTheta);
        int newMvtY = (int) Math.round(mvtX * sinTheta + mvtY * cosTheta);

        // Update point B based on the new movement vector
        this.b = new Point(this.a.getX() + newMvtX, this.a.getY() + newMvtY);
    }
	
	public Point getMiddle() {
		int middleSegX = Math.min(this.a.getX(), this.b.getX()) + Math.abs((this.a.getX() - this.b.getX())) / 2;
		int middleSegY = Math.min(this.a.getY(), this.b.getY()) + Math.abs((this.a.getY() - this.b.getY())) / 2;
		
		return new Point(middleSegX, middleSegY);
	}

	public void setA(Point a) {
		this.a = a;
	}

	public Point getB() {
		return b;
	}
	
	public Segment normal() {
		return new Segment(-this.getYMouvement() / 10, this.getXMouvement() / 10);
	}
	
	public Segment normal(int x, int y) {
		return new Segment(-this.getYMouvement() / 10, this.getXMouvement() / 10, x, y);
	}

	public void setB(Point b) {
		this.b = b;
	}
	
	public int getXMouvement() {
		return this.b.getX() - this.a.getX();
	}
	
	public int getYMouvement() {
		return this.b.getY() - this.a.getY();
	}

	public void up() {
		this.b.up();
	}

	public void down() {
		this.b.down();
	}

	public void left() {
		this.b.left();
	}

	public void right() {
		this.b.right();
	}
}
