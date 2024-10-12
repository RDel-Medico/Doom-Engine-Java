package player;

import dataType.Point;
import dataType.Segment;

public class Camera {
	public Point pos;
	public Segment vision;
	
	private static Camera self;
	
	static {
		self = new Camera();
	}
	
	private Camera() {
		pos = new Point(10, 10);
		vision = new Segment(pos, new Point(pos.getX() + 1, pos.getY() + 1));
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
}
