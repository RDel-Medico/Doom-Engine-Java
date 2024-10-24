package player;

import dataType.Point;
import dataType.Sector;
import main.Main;
import utility.Utility;

public class Camera {

	private static final double MOVE_SPEED = 0.5;
	private static final double TURN_SPEED = 3.0;
	private static final double FOV = 90.0;

	public Point pos;
	private double height;
	public double yaw;
	
	private static final Camera self;
	
	static {
		self = new Camera();
	}
	
	private Camera() {
		pos = new Point(30, 10);
		yaw = 0;
		height = 0;
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

	public void moveLeft() {
		double angle = Math.toRadians(yaw + 90);
		double deltaX = MOVE_SPEED * Math.cos(angle);
		double deltaY = MOVE_SPEED * Math.sin(angle);
		pos.setX(pos.getX() + deltaX);
		pos.setY(pos.getY() + deltaY);
	}

	public void moveRight() {
		double angle = Math.toRadians(yaw - 90);
		double deltaX = MOVE_SPEED * Math.cos(angle);
		double deltaY = MOVE_SPEED * Math.sin(angle);
		pos.setX(pos.getX() + deltaX);
		pos.setY(pos.getY() + deltaY);
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

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

	public void updateHeight(boolean moving) {
		Sector curr = Utility.findSectorContainingPoint(pos);
    if (curr != null) {
        int baseHeight = curr.getFloorHeight();
        if (moving) {
            height = baseHeight + Main.BOBBING_AMPLITUDE * Math.sin(Main.bobbingTime);
        } else {
            height = baseHeight;
        }
    }
	}
}
