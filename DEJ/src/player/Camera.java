package player;

import dataType.Point;

public class Camera {

	private static final double MOVE_SPEED = 0.3;
	private static final double TURN_SPEED = 3.0;

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
        double deltaX = MOVE_SPEED * Math.cos(angle);
		double deltaY = MOVE_SPEED * Math.sin(angle);
        pos.setX(pos.getX() + deltaX);
        pos.setY(pos.getY() + deltaY);
    }

    public void moveBackward() {
        double angle = Math.toRadians(yaw);
		double deltaX = MOVE_SPEED * Math.cos(angle);
		double deltaY = MOVE_SPEED * Math.sin(angle);
        pos.setX(pos.getX() - deltaX);
        pos.setY(pos.getY() - deltaY);
    }

	public void turnLeft() {
		yaw -= TURN_SPEED;
	}

	public void turnRight() {
		yaw += TURN_SPEED;
	}

	public double getFOV() {
		return FOV;
	}
}
