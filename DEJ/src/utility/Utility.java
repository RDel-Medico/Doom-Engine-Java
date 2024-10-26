package utility;

import dataType.Point;
import dataType.Sector;
import dataType.Segment;
import java.util.ArrayList;
import main.Main;

public class Utility {
	private static final double TRESHOLD = 0.0001;

	public static boolean isZero(double value) {
		return value >= -TRESHOLD && value <= TRESHOLD;
	}

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
		return boundedIntersection(s, new Segment(c, calculateFOVEndPoint(c, yaw, -halfFOV)))
				|| boundedIntersection(s, new Segment(c, calculateFOVEndPoint(c, yaw, halfFOV)));
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
		return (o4 == 0 && onSegment(p2, q1, q2)) || (o3 == 0 && onSegment(p2, p1, q2))
				|| (o1 == 0 && onSegment(p1, p2, q1)) || (o2 == 0 && onSegment(p1, q2, q1));
	}

	// To find orientation of ordered triplet (p, q, r).
	// 0 --> p, q and r are collinear
	// 1 --> Clockwise
	// 2 --> Counterclockwise
	private static int orientation(Point p, Point q, Point r) {
		double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) - (q.getX() - p.getX()) * (r.getY() - q.getY());
		if (isZero(val))
			return 0; // collinear
		return (val > 0) ? 1 : 2; // clock or counterclock wise
	}

	// Given three collinear points p, q, r, the function checks if point q lies on
	// line segment 'pr'
	private static boolean onSegment(Point p, Point q, Point r) {
		return q.getX() <= Math.max(p.getX(), r.getX()) && q.getX() >= Math.min(p.getX(), r.getX())
				&& q.getY() <= Math.max(p.getY(), r.getY()) && q.getY() >= Math.min(p.getY(), r.getY());
	}

	// Returns the distance between two points
	public static double distance(Point a, Point b) {
		return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
	}

	// Returns the distance between a point and a segment
	public static double distanceToSegment(Point p, Segment s) {
		double x0 = p.getX();
		double y0 = p.getY();
		double x1 = s.getA().getX();
		double y1 = s.getA().getY();
		double x2 = s.getB().getX();
		double y2 = s.getB().getY();

		double numerator = Math.abs((y2 - y1) * x0 - (x2 - x1) * y0 + x2 * y1 - y2 * x1);
		double denominator = Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));

		return numerator / denominator;
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

	public static Point calculateFOVEndPoint(Point a, double yaw, double fov) {
		double angle = Math.toRadians(yaw + fov);
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);

		double x = a.getX() + 1000 * cos;
		double y = a.getY() + 1000 * sin;

		return new Point(x, y);
	}

	public static boolean isCollinear(Segment s, Point c) {
		return isZero(crossProduct2D(s, new Segment(s.getA(), c)));
	}

	public static boolean isInFront(Segment s, Point c, double yaw) {
		return crossProduct2D(s, new Segment(s.getA(), c)) < 0;
	}

	public static double crossProduct2D(Segment ab, Segment cd) {
		double x1 = ab.getB().getX() - ab.getA().getX();
		double y1 = ab.getB().getY() - ab.getA().getY();
		double x2 = cd.getB().getX() - cd.getA().getX();
		double y2 = cd.getB().getY() - cd.getA().getY();
		return x1 * y2 - y1 * x2;
	}

	public static boolean isInFront(Segment s, Point c) {
		return crossProduct2D(s, new Segment(s.getA(), c)) < 0;
	}

	public static boolean isInFront(Segment s1, Segment s2) {
		return crossProduct2D(s1, s2) < 0;
	}

	public static boolean isParallel(Segment s1, Segment s2) {
		return isZero(crossProduct2D(s1, s2));
	}

	public static boolean isCollinear(Segment s1, Segment s2) {
		double num = crossProduct2D(new Segment(s1.getA(), s2.getA()), s1);
		double den = crossProduct2D(s1, s2);

		return isZero(num) && isZero(den);
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
		double collisionX = s2.getA().getX() + (int) (t * (double) s2.getXMouvement());
		double collisionY = s2.getA().getY() + (int) (t * (double) s2.getYMouvement());

		return new Point(collisionX, collisionY);
	}

	public static boolean collisionOnFront(Segment s1, Segment s2) {
		double num = crossProduct2D(new Segment(s1.getA(), s2.getA()), s1);
		double den = crossProduct2D(s1, s2);

		return num < 0 || (isZero(num) && den > 0);
	}

	public static boolean collisionOnBack(Segment s1, Segment s2) {
		double num = crossProduct2D(new Segment(s1.getA(), s2.getA()), s1);
		double den = crossProduct2D(s1, s2);

		return num > 0 || (isZero(num) && den < 0);
	}

	public static Sector findSectorContainingPoint(Point p) {
		for (Sector sector : Main.map.getMap())
			if (isPointInSector(p, sector))
				return sector;

		return null;
	}

	public static Point calculatePolygonCenter(ArrayList<Point> points) {
		double sumX = 0;
		double sumY = 0;
		int numPoints = points.size();

		for (Point p : points) {
			sumX += p.getX();
			sumY += p.getY();
		}

		double centerX = sumX / numPoints;
		double centerY = sumY / numPoints;

		return new Point(centerX, centerY);
	}

	public static double angleFromCenter(Point center, Point p) {
		return Math.atan2(p.getY() - center.getY(), p.getX() - center.getX());
	}

	public static boolean isColliding(Point newPos, ArrayList<Segment> segments) {
		for (Segment segment : segments) {
			if (!segment.isCollide()) {
				continue;
			}
			if (Utility.boundedIntersection(segment, new Segment(Main.player.getPos(), newPos))) {
				return true;
			}
		}
		return false;
	}

	public static boolean isPointInSector(Point p, Sector sector) {
		int intersectionCount = 0;
		ArrayList<Segment> segments = sector.getSegments();

		for (Segment segment : segments) {
			Point a = segment.getA();
			Point b = segment.getB();

			// Check if the ray intersects the segment
			if (doesRayIntersectSegment(p, a, b)) {
				intersectionCount++;
			}
		}

		// Point is inside the sector if the intersection count is odd
		return (intersectionCount % 2) == 1;
	}

	private static boolean doesRayIntersectSegment(Point p, Point a, Point b) {
		// Ensure a is the lower point
		if (a.getY() > b.getY()) {
			Point temp = a;
			a = b;
			b = temp;
		}

		// Check if the point is outside the vertical bounds of the segment
		if (p.getY() == a.getY() || p.getY() == b.getY()) {
			p = new Point(p.getX(), p.getY() + TRESHOLD);
		}

		if (p.getY() < a.getY() || p.getY() > b.getY()) {
			return false;
		}

		// Check if the point is to the right of the segment
		if (p.getX() >= Math.max(a.getX(), b.getX())) {
			return false;
		}

		// Check if the point is to the left of the segment
		if (p.getX() < Math.min(a.getX(), b.getX())) {
			return true;
		}

		// Calculate the intersection point
		double red = (p.getY() - a.getY()) / (b.getY() - a.getY());
		double intersectionX = a.getX() + red * (b.getX() - a.getX());

		return p.getX() < intersectionX;
	}

	public static ArrayList<Integer> getXArray(ArrayList<Point> a) {
		ArrayList<Integer> res = new ArrayList<>();
		for (Point p : a)
			res.add((int) p.getX());
		return res;
	}

	public static ArrayList<Integer> getYArray(ArrayList<Point> a) {
		ArrayList<Integer> res = new ArrayList<>();
		for (Point p : a)
			res.add((int) p.getY());
		return res;
	}

	public static void addFOVPoint(ArrayList<Point> points, Segment segment, double halfFOV, Segment leftFOVBoundary,
			Segment rightFOVBoundary, Point playerPos, double playerYaw) {
		Point a = segment.getA();
		Point b = segment.getB();

		// Calculate the angle of the segment endpoints relative to the player position
		double angleA = Utility.normalizeAngle(
				Math.toDegrees(Math.atan2(a.getY() - playerPos.getY(), a.getX() - playerPos.getX())) - playerYaw);
		double angleB = Utility.normalizeAngle(
				Math.toDegrees(Math.atan2(b.getY() - playerPos.getY(), b.getX() - playerPos.getX())) - playerYaw);

		if (angleA >= -halfFOV && angleA <= halfFOV) {
			points.add(a);
		} else if (Utility.boundedIntersection(segment, leftFOVBoundary)) {
			points.add(Utility.intersectionPoint(segment, leftFOVBoundary));
		} else if (Utility.boundedIntersection(segment, rightFOVBoundary)) {
			points.add(Utility.intersectionPoint(segment, rightFOVBoundary));
		}

		if (angleB >= -halfFOV && angleB <= halfFOV) {
			points.add(b);
		} else if (Utility.boundedIntersection(segment, leftFOVBoundary)) {
			points.add(Utility.intersectionPoint(segment, leftFOVBoundary));
		} else if (Utility.boundedIntersection(segment, rightFOVBoundary)) {
			points.add(Utility.intersectionPoint(segment, rightFOVBoundary));
		}
	}

	public static int[] convertIntegers(ArrayList<Integer> integers) {
		int[] ret = new int[integers.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = integers.get(i);
		}
		return ret;
	}
}
