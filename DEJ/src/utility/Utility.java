package utility;

import dataType.Point;
import dataType.Segment;

public class Utility {

	public static boolean isWithinFOV(Segment s, Point c, double fov, double yaw) {
        Point a = s.getA();
        Point b = s.getB();

        double angleA = Math.toDegrees(Math.atan2(a.getY() - c.getY(), a.getX() - c.getX())) - yaw;
        double angleB = Math.toDegrees(Math.atan2(b.getY() - c.getY(), b.getX() - c.getX())) - yaw;

        angleA = normalizeAngle(angleA);
        angleB = normalizeAngle(angleB);

        double halfFOV = fov / 2.0;

        // Check if either endpoint is within the FOV
        if ((angleA >= -halfFOV && angleA <= halfFOV) || (angleB >= -halfFOV && angleB <= halfFOV)) {
            return true;
        }

        // Check if the segment intersects the FOV boundaries
        Point leftFOVEndPoint = calculateFOVEndPoint(c, yaw, -halfFOV);
        Point rightFOVEndPoint = calculateFOVEndPoint(c, yaw, halfFOV);

        Segment leftFOVBoundary = new Segment(c, leftFOVEndPoint);
        Segment rightFOVBoundary = new Segment(c, rightFOVEndPoint);

        return boundedIntersection(s, leftFOVBoundary) || boundedIntersection(s, rightFOVBoundary);
    }

	public static boolean boundedIntersection(Segment s1, Segment s2) {
		Point p1 = s1.getA();
        Point q1 = s1.getB();
        Point p2 = s2.getA();
        Point q2 = s2.getB();

        // Find the four orientations needed for the general and special cases
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        // General case
        if (o1 != o2 && o3 != o4) {
            return true;
        }

        // Special cases
        return (o4 == 0 && onSegment(p2, q1, q2)) || (o3 == 0 && onSegment(p2, p1, q2)) || (o1 == 0 && onSegment(p1, p2, q1)) || (o2 == 0 && onSegment(p1, q2, q1));
	}

	private static int orientation(Point p, Point q, Point r) {
        int val = (q.getY() - p.getY()) * (r.getX() - q.getX()) - (q.getX() - p.getX()) * (r.getY() - q.getY());
        if (val == 0) return 0; // collinear
        return (val > 0) ? 1 : 2; // clock or counterclock wise
    }

    private static boolean onSegment(Point p, Point q, Point r) {
        return q.getX() <= Math.max(p.getX(), r.getX()) && q.getX() >= Math.min(p.getX(), r.getX()) &&
               q.getY() <= Math.max(p.getY(), r.getY()) && q.getY() >= Math.min(p.getY(), r.getY());
    }

	public static double distance(Point a, Point b) {
		return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
	}

	public static double normalizeAngle(double angle) {
		angle = angle % 360;
		if (angle > 180) {
			angle -= 360;
		} else if (angle < -180) {
			angle += 360;
		}
		return angle;
	}

	public static Point calculateFOVEndPoint (Point a, double yaw, double fov) {
		double angle = Math.toRadians(yaw + fov);
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
