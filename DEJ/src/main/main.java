package main;

import javax.swing.JFrame;

import map.MapRenderer2D;
import map.Point;
import map.Sector;
import map.Segment;
import utility.Utility;

public class main {
	final static int SCREEN_WIDTH = 1500;
	final static int SCREEN_HEIGHT = 900;

	public static void main(String[] args) {
		JFrame frame = new JFrame("First test");
		frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Point a = new Point(0, 0);
		Point b = new Point(50, 50);
		
		Point c = new Point(50, 0);
		Point d = new Point(0, 50);
		
		Segment[] seg = new Segment[] {new Segment(a, b), new Segment(c, d)};
		
		System.out.println(Utility.intersectionPoint(seg[0], seg[1]).getX() + ", " + Utility.intersectionPoint(seg[0], seg[1]).getY());
		
		frame.setVisible(true);
	}

}
