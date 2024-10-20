package player;

import dataType.Point;
import dataType.Segment;

public class Camera {

	public Point pos;
	public Segment vision;
	private static final double FOV = 90.0;
	public double yaw;
	
	private static final Camera self;
	
	static {
		self = new Camera();
	}
	
	private Camera() {
		pos = new Point(10, 10);
		vision = new Segment(pos, new Point(pos.getX() + 10, pos.getY()));
		yaw = 0;
	}

	public Segment getRotatedVision() {
		double angle = Math.toRadians(yaw);
		int x = (int) (vision.getXMouvement() * Math.cos(angle) - vision.getYMouvement() * Math.sin(angle));
		int y = (int) (vision.getXMouvement() * Math.sin(angle) + vision.getYMouvement() * Math.cos(angle));
		return new Segment(pos, new Point(pos.getX() + x, pos.getY() + y));
	}

	public void rotate(double angle) {
		yaw += angle;
	}

	public double getYaw() {
		return yaw;
	}
	
	public static Camera getSelf() {
		return self;
	}
	
	public Point pos() {
		return pos;
	}

	public void moveForward() {
		pos.moveUp();
		vision.up();
	}

	public void moveBackward() {
		pos.moveDown();
		vision.down();
	}

	public void turnLeft() {
		pos.moveLeft();
		vision.left();
	}

	public void turnRight() {
		pos.moveRight();
		vision.right();
	}

	public double getFOV() {
		return FOV;
	}
}
