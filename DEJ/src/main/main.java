package main;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

import bsp.BSP;
import bsp.BSPTraversal;
import map.MapRenderer2D;
import map.Point;
import map.Sector;
import map.Segment;
import player.Camera;

public class main implements KeyListener {
	final static int SCREEN_WIDTH = 1500;
	final static int SCREEN_HEIGHT = 900;
	
	public static Camera player;
	public static MapRenderer2D map;
	public static BSPTraversal bspT;
	public static BSP bsp;
	
	public static int lastX;

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
		
		Point i = new Point(30, 10);
		Point j = new Point(40, 20);
		Point k = new Point(40, 10);
		
		Segment[] triangle = new Segment[] {new Segment(i, j), new Segment(j, k), new Segment(k, i), new Segment(e, h)};
		
		Sector[] level = new Sector[] {new Sector(triangle), new Sector(obstacle), new Sector(seg)};
		
		player = Camera.getSelf();
		
		bsp = new BSP(level);
		bspT = new BSPTraversal(bsp.getRoot(), bsp.getSegments());
		
		
		
		map = new MapRenderer2D(level);
		bspT.update();
		map.setSegment(bsp.getSegments(), bspT.getId());
		
		frame.add(map);
		
		frame.addKeyListener(new main());
		
		frame.setVisible(true);
	}
	
	public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_UP:
                player.moveForward();
                break;
            case KeyEvent.VK_DOWN:
                player.moveBackward();
                break;
            case KeyEvent.VK_LEFT:
                player.turnLeft();
                break;
            case KeyEvent.VK_RIGHT:
                player.turnRight();
                break;
            case KeyEvent.VK_Q:
                player.vision.rotate(-45);
                break;
            case KeyEvent.VK_D:
                player.vision.rotate(45);
                break;
        }

        bspT.update();
        map.setSegment(bsp.getSegments(), bspT.getId());
        map.repaint();
    }

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
