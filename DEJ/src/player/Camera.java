package player;

import map.Point;
import map.Segment;

public class Camera {
	public Point pos;
	
	public Segment vision;
	
	static Camera self;
	
	static {
		self = new Camera();
	}
	
	public Camera() {
		pos = new Point(10, 10);
		
		vision = new Segment(pos, new Point(pos.getX() + 1, pos.getY() + 1));
	}
	
	public static Camera getSelf() {
		return self;
	}
	
	public Point pos() {
		return self.pos;
	}

	public void moveForward() {
		self.pos.up();
		self.vision.up();
	}

	public void moveBackward() {
		self.pos.down();
		self.vision.down();
	}

	public void turnLeft() {
		self.pos.left();
		self.vision.left();
	}

	public void turnRight() {
		self.pos.right();
		self.vision.right();
	}
}
