package utility;

import dataType.Point;
import dataType.Segment;
import main.Main;

public class Utility {

	public static Point project(Point a) {

		int dx = a.getX() - Main.player.pos().getX();
		int dy = a.getY() - Main.player.pos().getY();

		double angle = Math.toRadians(Main.player.getYaw());
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);

		int x = (int) (dx * cos - dy * sin);
		int y = (int) (dx * sin + dy * cos);

		int screenX = Main.SCREEN_WIDTH / 2 + x * Main.SCREEN_WIDTH / (2*y);
		int screenY = Main.SCREEN_HEIGHT / 2 - Main.SCREEN_HEIGHT / (2*y);

		return new Point(screenX, screenY);
	}

	public static boolean isWithinFOV(Segment s, Point c, double fov, double yaw) {
		Point a = s.getA();
        Point b = s.getB();

        double angleA = Math.toDegrees(Math.atan2(a.getY() - c.getY(), a.getX() - c.getX())) - yaw;
        double angleB = Math.toDegrees(Math.atan2(b.getY() - c.getY(), b.getX() - c.getX())) - yaw;

        angleA = normalizeAngle(angleA);
		angleB = normalizeAngle(angleB);

        double halfFOV = fov / 2.0;

        return (angleA >= -halfFOV && angleA <= halfFOV) || (angleB >= -halfFOV && angleB <= halfFOV);
	}

	private static double normalizeAngle(double angle) {
		angle = angle % 360;
		if (angle > 180) {
			angle -= 360;
		} else if (angle < -180) {
			angle += 360;
		}
		return angle;
	}

	public static Point calculateFOVEndPoint (Point a, double yaw, double fov) {
		double angle = Math.toRadians(yaw + fov / 2.0);
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);

		int x = (int) (a.getX() + 1000 * cos);
		int y = (int) (a.getY() + 1000 * sin);

		return new Point(x, y);
	}

	public static boolean isCollinear(Segment s, Point c) {
		return 0 == crossProduct2D(s, new Segment(s.getA(), c));
	}

	public static boolean isInFront(Segment s, Point c, double yaw) {
		return crossProduct2D(s, new Segment(s.getA(), c)) < 0;
	}

	public static int crossProduct2D (Segment ab, Segment cd) {
		int x1 = ab.getB().getX() - ab.getA().getX();
        int y1 = ab.getB().getY() - ab.getA().getY();
        int x2 = cd.getB().getX() - cd.getA().getX();
        int y2 = cd.getB().getY() - cd.getA().getY();
        return x1 * y2 - y1 * x2;
	}
	
	public static boolean isInFront(Segment s, Point c) {
		return crossProduct2D(s, new Segment(s.getA(), c)) < 0;
	}
	
	public static boolean isInFront(Segment s1, Segment s2) {
		return crossProduct2D(s1, s2) < 0;
	}
	
	public static boolean isParallel (Segment s1, Segment s2) {
		return 0 == crossProduct2D(s1, s2);
	}
	
	public static boolean isCollinear (Segment s1, Segment s2) {
		int num = crossProduct2D(new Segment(s1.getA(), s2.getA()), s1);
		int den = crossProduct2D(s1, s2);
		
		return num == 0 && den == 0;
	}
	
	public static boolean intersection(Segment s1, Segment s2) {
		double num = crossProduct2D(new Segment(s1.getA(), s2.getA()), s1);
		double den = crossProduct2D(s1, s2);
		if (0.0 < num / den && num / den < 1.0) {
			return true;
		}
		return 0.0 < num / den && num / den < 1.0;
	}
	
	public static Point intersectionPoint(Segment s1, Segment s2) {
		double num = crossProduct2D(new Segment(s1.getA(), s2.getA()), s1);
		double den = crossProduct2D(s1, s2);
		
		double t = num / den;
		int collisionX = s2.getA().getX() + (int)(t * (double)s2.getXMouvement());
		int collisionY = s2.getA().getY() + (int)(t * (double)s2.getYMouvement());
		
		return new Point(collisionX, collisionY);
	}
	
	public static boolean collisionOnFront(Segment s1, Segment s2) {
		double num = crossProduct2D(new Segment(s1.getA(), s2.getA()), s1);
		double den = crossProduct2D(s1, s2);
		
		return num < 0 || (Math.round(num) == 0 && den > 0);
	}
	
	public static boolean collisionOnBack(Segment s1, Segment s2) {
		double num = crossProduct2D(new Segment(s1.getA(), s2.getA()), s1);
		double den = crossProduct2D(s1, s2);
		
		return num > 0 || (Math.round(num) == 0 && den < 0);
	}
}
