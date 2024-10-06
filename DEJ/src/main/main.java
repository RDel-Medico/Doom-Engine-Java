package main;

import javax.swing.JFrame;

import map.MapRenderer2D;
import map.Point;
import map.Sector;
import map.Segment;

public class main {
	final static int SCREEN_WIDTH = 1500;
	final static int SCREEN_HEIGHT = 900;

	public static void main(String[] args) {
		JFrame frame = new JFrame("First test");
		frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Point a = new Point(0, 0);
		Point b = new Point(100, 0);
		Point c = new Point(100, 100);
		Point d = new Point(0, 100);
		
		Segment[] seg = new Segment[] {new Segment(a, b),
		new Segment(b, c),
		new Segment(c, d),
		new Segment(d, a)};
		
		Sector level = new Sector(seg);
		
		Point a2 = new Point(10, 50);
		Point b2 = new Point(80, 30);
		Point c2 = new Point(40, 70);
		Point d2 = new Point(30, 50);
		
		Segment[] seg2 = new Segment[] {new Segment(a2, b2),
		new Segment(b2, c2),
		new Segment(c2, d2),
		new Segment(d2, a2)};
		
		Sector level2 = new Sector(seg2);
		
		MapRenderer2D map = new MapRenderer2D(new Sector[] {level, level2}, SCREEN_WIDTH, SCREEN_HEIGHT);
		
		frame.add(map);
		
		frame.setVisible(true);
	}

}
