package utility;

import map.Point;
import map.Segment;

public class Utility {
	public static int crossProduct2D (Segment ab, Segment cd) {
		return (ab.getXMouvement() * cd.getYMouvement() - cd.getXMouvement() * ab.getYMouvement());
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
