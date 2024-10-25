package player;

import dataType.Point;
import dataType.Sector;
import dataType.Segment;
import java.util.ArrayList;
import main.Main;
import utility.Utility;

public class Camera {
    public enum Direction {FORWARD, BACKWARD, LEFT, RIGHT};

    public static Camera getSelf() {
        return self;
    }

	private Point pos;
	private double height;
	private double yaw;
	
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

    public void move(Direction direction, ArrayList<Segment> segments) {
        double angle;
        switch (direction) {
            case FORWARD -> angle = yaw;
            case BACKWARD -> angle = yaw;
            case LEFT -> angle = yaw - 90;
            default -> angle = yaw + 90;
        }
        angle = Math.toRadians(angle);

        double deltaX = Main.MOVE_SPEED * Math.cos(angle);
        double deltaY = Main.MOVE_SPEED * Math.sin(angle);
        Point newPos;
        Point newPosFar;
        switch (direction) {
            case FORWARD -> {
                newPos = new Point(pos.getX() + deltaX, pos.getY() + deltaY);
                newPosFar = new Point(pos.getX() + deltaX * Main.COLLISION_TRESHOLD, pos.getY() + deltaY * Main.COLLISION_TRESHOLD);
                }
            case BACKWARD -> {
                newPos = new Point(pos.getX() - deltaX, pos.getY() - deltaY);
                newPosFar = new Point(pos.getX() - deltaX * Main.COLLISION_TRESHOLD, pos.getY() - deltaY * Main.COLLISION_TRESHOLD);
                }
            case LEFT -> {
                newPos = new Point(pos.getX() + deltaX, pos.getY() + deltaY);
                newPosFar = new Point(pos.getX() + deltaX * Main.COLLISION_TRESHOLD, pos.getY() + deltaY * Main.COLLISION_TRESHOLD);
                }
            default -> {
                newPos = new Point(pos.getX() + deltaX, pos.getY() + deltaY);
                newPosFar = new Point(pos.getX() + deltaX * Main.COLLISION_TRESHOLD, pos.getY() + deltaY * Main.COLLISION_TRESHOLD);
                }
        }
        
        if (!Utility.isColliding(newPosFar, segments)) {
            pos.setX(newPos.getX());
            pos.setY(newPos.getY());
        }
    }

	public void turnLeft() {
		yaw -= Main.TURN_SPEED;
	}

	public void turnRight() {
		yaw += Main.TURN_SPEED;
	}

	public void updateHeight(boolean moving) {
		Sector curr = Utility.findSectorContainingPoint(pos);
        if (curr == null)
            return;
            
        int baseHeight = curr.getFloorHeight();
        if (moving) {
            height = baseHeight + Main.BOBBING_AMPLITUDE * Math.sin(Main.bobbingTime);
        } else {
            height = baseHeight;
        }
	}

    public Point getPos() {
        return pos;
    }

    public void setPos(Point pos) {
        this.pos = pos;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getYaw() {
        return yaw;
    }

    public void setYaw(double yaw) {
        this.yaw = yaw;
    }
}
