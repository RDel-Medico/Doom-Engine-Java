package player;

import dataType.Point;

public class Camera {

	public Point pos;
	private static final double FOV = 90.0;
	public double yaw;
	
	private static final Camera self;
	
	static {
		self = new Camera();
	}
	
	private Camera() {
		pos = new Point(10, 10);
		yaw = 0;
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
        double angle = Math.toRadians(yaw);
        int deltaX = (int) (Math.round(Math.cos(angle)));
		int deltaY = (int) (Math.round(Math.sin(angle)));
        pos.setX(pos.getX() + deltaX);
        pos.setY(pos.getY() + deltaY);
    }

    public void moveBackward() {
        double angle = Math.toRadians(yaw);
		int deltaX = (int) (Math.round(Math.cos(angle)));
		int deltaY = (int) (Math.round(Math.sin(angle)));
        pos.setX(pos.getX() - deltaX);
        pos.setY(pos.getY() - deltaY);
    }

	public void turnLeft() {
		yaw -= 10;
	}

	public void turnRight() {
		yaw += 10;
	}

	public double getFOV() {
		return FOV;
	}
}
