package main;

import java.util.ArrayList;

import javax.swing.JFrame;

import bsp.BSP;
import bsp.BSPTraversal;
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
		Point b = new Point(0, 50);
		Point c = new Point(50, 50);
		Point d = new Point(50, 0);
		
		Segment[] seg = new Segment[] {new Segment(b, a), new Segment(c, b), new Segment(d, c), new Segment(a, d)};
		
		Point e = new Point(10, 20);
		Point f = new Point(30, 20);
		Point g = new Point(30, 40);
		Point h = new Point(10, 40);
		
		Segment[] obstacle = new Segment[] {new Segment(f, e), new Segment(g, f), new Segment(h, g), new Segment(e, h)};
		
		Sector[] level = new Sector[] {new Sector(obstacle), new Sector(seg)};
		
		BSP bsp = new BSP(level);
		BSPTraversal bspT = new BSPTraversal(bsp.getRoot(), bsp.getSegments());
		
		MapRenderer2D map = new MapRenderer2D(level);
		bspT.update();
		map.setSegment(bsp.getSegments(), bspT.getId());
		
		frame.add(map);
		
		frame.setVisible(true);
	}

}
