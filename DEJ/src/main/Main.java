package main;


import bsp.BSP;
import bsp.BSPTraversal;
import dataType.Point;
import dataType.Sector;
import dataType.Segment;
import game.Map;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.Timer;
import player.Camera;

public class Main implements KeyListener {
	public final static int SCREEN_WIDTH = 1500;
	public final static int SCREEN_HEIGHT = 900;
	
	public final static Point DISPLAY_TOP_LEFT = new Point(100, 100);
	public final static Point DISPLAY_BOTTOM_RIGHT = new Point(1400, 800);
	
	public static Camera player;
	public static Map map;
	public static BSPTraversal bspT;
	public static BSP bsp;

        @SuppressWarnings("FieldMayBeFinal")
	private static Set<Integer> pressedKeys = new HashSet<>();

	public static void main(String[] args) {
		JFrame frame = new JFrame("Doom Engine");
		
		frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setup();
		
		frame.add(map);
		
		frame.addKeyListener(new Main());
		
		frame.setVisible(true);
	}
	
	public static void setup() {
		Point a = new Point(0, 0);
		Point b = new Point(0, 50);
		Point c = new Point(50, 50);
		Point d = new Point(50, 0);
		
		ArrayList<Segment> seg = new ArrayList<>();
		seg.add(new Segment(b, a));
		seg.add(new Segment(c, b));
		seg.add(new Segment(d, c));
		seg.add(new Segment(a, d));
		
		Point e = new Point(10, 20);
		Point f = new Point(30, 20);
		Point g = new Point(30, 40);
		Point h = new Point(10, 40);
		
		ArrayList<Segment> obstacle = new ArrayList<>();
		obstacle.add(new Segment(f, e));
		obstacle.add(new Segment(g, f));
		obstacle.add(new Segment(h, g));
		obstacle.add(new Segment(e, h));
		
		Point i = new Point(30, 10);
		Point j = new Point(40, 20);
		Point k = new Point(40, 10);
		
		ArrayList<Segment> triangle = new ArrayList<>();
		triangle.add(new Segment(i, j));
		triangle.add(new Segment(j, k));
		triangle.add(new Segment(k, i));
		
		ArrayList<Sector> level = new ArrayList<>();
		level.add(new Sector(triangle, Color.RED, -5, 5));
		level.add(new Sector(obstacle, Color.BLUE, -5, 5));
		level.add(new Sector(seg, Color.GREEN, -5, 5));
		
		map = new Map(level);
		bsp = new BSP(level);
		bspT = new BSPTraversal(bsp.getRoot());
		
		player = Camera.getSelf();
		
		bspT.update();
		map.updateBspSegment(bsp.getSegments(), bspT.getId());

		Timer timer = new Timer(16, event -> update());
		timer.start();
	}

	private static void update() {
		if (pressedKeys.contains(KeyEvent.VK_UP)) {
			player.moveForward();
		}
		if (pressedKeys.contains(KeyEvent.VK_DOWN)) {
			player.moveBackward();
		}
		if (pressedKeys.contains(KeyEvent.VK_LEFT) || pressedKeys.contains(KeyEvent.VK_Q)) {
			player.turnLeft();
		}
		if (pressedKeys.contains(KeyEvent.VK_RIGHT) || pressedKeys.contains(KeyEvent.VK_D)) {
			player.turnRight();
		}

		bspT.update();
		map.updateBspSegment(bsp.getSegments(), bspT.getId());
		map.repaint();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		pressedKeys.add(e.getKeyCode());
    }

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		pressedKeys.remove(e.getKeyCode());
	}
}
