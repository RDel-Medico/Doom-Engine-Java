package player;

import dataType.Point;
import dataType.Sector;
import dataType.Segment;
import java.util.ArrayList;
import main.Main;
import utility.Utility;

public class Camera {

	private static final double MOVE_SPEED = 0.5;
	private static final double TURN_SPEED = 3.0;
	private static final double FOV = 90.0;

	private static final double COLLISION_TRESHOLD = 5.0;

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

	private boolean isColliding(Point newPos, ArrayList<Segment> segments) {
        for (Segment segment : segments) {
			if (!segment.isCollide()) {
				continue;
			}
            if (Utility.boundedIntersection(segment, new Segment(Main.player.pos(), newPos, null, null, null, false))) {
                return true;
            }
        }
        return false;
    }

    public void moveForward(ArrayList<Segment> segments) {
        double angle = Math.toRadians(yaw);
        double deltaX = MOVE_SPEED * Math.cos(angle);
        double deltaY = MOVE_SPEED * Math.sin(angle);
        Point newPos = new Point(pos.getX() + deltaX, pos.getY() + deltaY);
		Point newPosFar = new Point(pos.getX() + deltaX * COLLISION_TRESHOLD, pos.getY() + deltaY * COLLISION_TRESHOLD);
        if (!isColliding(newPosFar, segments)) {
            pos.setX(newPos.getX());
            pos.setY(newPos.getY());
        }
    }

    public void moveBackward(ArrayList<Segment> segments) {
        double angle = Math.toRadians(yaw);
        double deltaX = MOVE_SPEED * Math.cos(angle);
        double deltaY = MOVE_SPEED * Math.sin(angle);
        Point newPos = new Point(pos.getX() - deltaX, pos.getY() - deltaY);
		Point newPosFar = new Point(pos.getX() - deltaX * COLLISION_TRESHOLD, pos.getY() - deltaY * COLLISION_TRESHOLD);
        if (!isColliding(newPosFar, segments)) {
            pos.setX(newPos.getX());
            pos.setY(newPos.getY());
        }
    }

    public void moveLeft(ArrayList<Segment> segments) {
        double angle = Math.toRadians(yaw + 90);
        double deltaX = MOVE_SPEED * Math.cos(angle);
        double deltaY = MOVE_SPEED * Math.sin(angle);
        Point newPos = new Point(pos.getX() + deltaX, pos.getY() + deltaY);
		Point newPosFar = new Point(pos.getX() + deltaX * COLLISION_TRESHOLD, pos.getY() + deltaY * COLLISION_TRESHOLD);
        if (!isColliding(newPosFar, segments)) {
            pos.setX(newPos.getX());
            pos.setY(newPos.getY());
        }
    }

    public void moveRight(ArrayList<Segment> segments) {
        double angle = Math.toRadians(yaw - 90);
        double deltaX = MOVE_SPEED * Math.cos(angle);
        double deltaY = MOVE_SPEED * Math.sin(angle);
        Point newPos = new Point(pos.getX() + deltaX, pos.getY() + deltaY);
		Point newPosFar = new Point(pos.getX() + deltaX * COLLISION_TRESHOLD, pos.getY() + deltaY * COLLISION_TRESHOLD);
        if (!isColliding(newPosFar, segments)) {
            pos.setX(newPos.getX());
            pos.setY(newPos.getY());
        }
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
