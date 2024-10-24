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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.Timer;
import player.Camera;

public class Main implements KeyListener {
	public final static int SCREEN_WIDTH = 1500;
	public final static int SCREEN_HEIGHT = 900;

	public final static Color CEIL_COLOR = Color.RED;
	public final static Color FLOOR_COLOR = new Color(139, 69, 19);
	
	public final static Point DISPLAY_TOP_LEFT = new Point(100, 100);
	public final static Point DISPLAY_BOTTOM_RIGHT = new Point(1400, 800);
	
	public static Camera player;
	public static Map map;
	public static BSPTraversal bspT;
	public static BSP bsp;

	public static BufferedImage middleWallTexture = null;
	public static BufferedImage topWallTexture = null;
	public static BufferedImage bottomWallTexture = null;
	public static BufferedImage ceilingTexture = null;
	public static BufferedImage floorTexture = null;

	public static boolean texture = true;

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
		if (texture) {
			try {
				middleWallTexture = ImageIO.read(new File ("./ressources/wall.png"));
				topWallTexture = ImageIO.read(new File ("./ressources/top.png"));
				bottomWallTexture = ImageIO.read(new File ("./ressources/bottom.png"));
				ceilingTexture = ImageIO.read(new File ("./ressources/ceiling.png"));
				floorTexture = ImageIO.read(new File ("./ressources/floor.png"));
			} catch (IOException e) {
				System.out.println("Error loading texture");
			}
		}
		
		ArrayList<Sector> level = new ArrayList<>();

		Point a = new Point(20, 0);
		Point b = new Point(40, 0);
		Point c = new Point(50, 0);
		Point d = new Point(70, 0);
		Point e = new Point(20, 30);
		Point f = new Point(40, 30);
		Point g = new Point(50, 30);
		Point h = new Point(70, 30);
		Point i = new Point(0, 50);
		Point j = new Point(40, 50);
		Point k = new Point(50, 50);
		Point l = new Point(90, 50);
		Point m = new Point(30, 80);
		Point n = new Point(40, 70);
		Point o = new Point(50, 70);
		Point p = new Point(60, 80);
		Point q = new Point(40, 80);
		Point r = new Point(50, 80);
		Point s = new Point(10, 100);
		Point t = new Point(30, 100);
		Point u = new Point(40, 90);
		Point v = new Point(50, 90);
		Point w = new Point(60, 100);
		Point x = new Point(80, 100);
		Point y = new Point(0, 110);
		Point z = new Point(40, 100);
		Point aa = new Point(50, 100);
		Point ab = new Point(90, 110);

		ArrayList<Segment> leftStart = new ArrayList<>();
		leftStart.add(new Segment(e, a, Color.GREEN, Color.ORANGE, Color.WHITE));
		leftStart.add(new Segment(f, e, Color.GREEN, Color.ORANGE, Color.WHITE));
		leftStart.add(new Segment(b, f, null, null, null));
		leftStart.add(new Segment(a, b, Color.GREEN, Color.ORANGE, Color.WHITE));

		ArrayList<Segment> middleStart = new ArrayList<>();
		middleStart.add(new Segment(f, b, null, null, null));
		middleStart.add(new Segment(g, f, null, null, null));
		middleStart.add(new Segment(c, g, null, null, null));
		middleStart.add(new Segment(b, c, Color.GREEN, Color.ORANGE, Color.WHITE));

		ArrayList<Segment> rightStart = new ArrayList<>();
		rightStart.add(new Segment(g, c, null, null, null));
		rightStart.add(new Segment(h, g, Color.GREEN, Color.ORANGE, Color.WHITE));
		rightStart.add(new Segment(d, h, Color.GREEN, Color.ORANGE, Color.WHITE));
		rightStart.add(new Segment(c, d, Color.GREEN, Color.ORANGE, Color.WHITE));

		ArrayList<Segment> hallway = new ArrayList<>();
		hallway.add(new Segment(j, k, null, Color.ORANGE, null));
		hallway.add(new Segment(g, k, Color.GREEN, null, Color.WHITE));
		hallway.add(new Segment(g, f, null, Color.ORANGE, null));
		hallway.add(new Segment(j, f, Color.GREEN, null, Color.WHITE));
		Sector hall = new Sector(hallway, 0, 70, 80, FLOOR_COLOR, CEIL_COLOR, false);
		hall.setCeilingTexture(middleWallTexture);
		level.add(hall);

		ArrayList<Segment> leftRoom = new ArrayList<>();
		leftRoom.add(new Segment(y, i, Color.GREEN, Color.CYAN, Color.WHITE));
		leftRoom.add(new Segment(z, y, Color.GREEN, Color.CYAN, Color.WHITE));
		leftRoom.add(new Segment(j, z, null, null, null));
		leftRoom.add(new Segment(i, j, Color.GREEN, Color.CYAN, Color.WHITE));

		ArrayList<Segment> middleRoom = new ArrayList<>();
		middleRoom.add(new Segment(z, j, null, null, null));
		middleRoom.add(new Segment(aa, z, Color.GREEN, Color.CYAN, Color.WHITE));
		middleRoom.add(new Segment(k, aa, null, null, null));
		middleRoom.add(new Segment(j, k, null, null, null));

		ArrayList<Segment> rightRoom = new ArrayList<>();
		rightRoom.add(new Segment(aa, k, null, null, null));
		rightRoom.add(new Segment(ab, aa, Color.GREEN, Color.CYAN, Color.WHITE));
		rightRoom.add(new Segment(l, ab, Color.GREEN, Color.CYAN, Color.WHITE));
		rightRoom.add(new Segment(k, l, Color.GREEN, Color.CYAN, Color.WHITE));

		ArrayList<Segment> leftTriangle = new ArrayList<>();
		leftTriangle.add(new Segment(s, t, null, Color.CYAN, Color.WHITE));
		leftTriangle.add(new Segment(t, m, null, Color.CYAN, Color.WHITE));
		leftTriangle.add(new Segment(m, s, null, Color.CYAN, Color.WHITE));
		Sector lTriangle = new Sector(leftTriangle, 20, 70, 80, FLOOR_COLOR, CEIL_COLOR, false);
		lTriangle.setFloorTexture(floorTexture);
		lTriangle.setCeilingTexture(floorTexture);
		level.add(lTriangle);

		ArrayList<Segment> rightTriangle = new ArrayList<>();
		rightTriangle.add(new Segment(w, x, Color.MAGENTA, Color.CYAN, Color.WHITE));
		rightTriangle.add(new Segment(x, p, Color.MAGENTA, Color.CYAN, Color.WHITE));
		rightTriangle.add(new Segment(p, w, Color.MAGENTA, Color.CYAN, Color.WHITE));

		ArrayList<Segment> firstStep = new ArrayList<>();
		firstStep.add(new Segment(q, r, null, null, Color.WHITE));
		firstStep.add(new Segment(r, o, null, null, Color.WHITE));
		firstStep.add(new Segment(o, n, null, null, Color.WHITE));
		firstStep.add(new Segment(n, q, null, null, Color.WHITE));
		Sector fStep = new Sector(firstStep, 10, 80, 80, FLOOR_COLOR, CEIL_COLOR, false);
		fStep.setFloorTexture(floorTexture);
		level.add(fStep);

		ArrayList<Segment> secondStep = new ArrayList<>();
		secondStep.add(new Segment(u, v, null, null, Color.WHITE));
		secondStep.add(new Segment(v, r, null, null, Color.WHITE));
		secondStep.add(new Segment(r, q, null, null, Color.WHITE));
		secondStep.add(new Segment(q, u, null, null, Color.WHITE));
		Sector sStep = new Sector(secondStep, 20, 80, 80, FLOOR_COLOR, CEIL_COLOR, false);
		sStep.setFloorTexture(floorTexture);
		level.add(sStep);

		ArrayList<Segment> thirdStep = new ArrayList<>();
		thirdStep.add(new Segment(z, aa, null, null, Color.WHITE));
		thirdStep.add(new Segment(aa, v, null, null, Color.WHITE));
		thirdStep.add(new Segment(v, u, null, null, Color.WHITE));
		thirdStep.add(new Segment(u, z, null, null, Color.WHITE));
		Sector tStep = new Sector(thirdStep, 30, 80, 80, FLOOR_COLOR, CEIL_COLOR, false);
		tStep.setFloorTexture(floorTexture);
		level.add(tStep);

		level.add(new Sector(leftStart, 0, 80, 80, FLOOR_COLOR, CEIL_COLOR, true));
		level.add(new Sector(middleStart, 0, 80, 80, FLOOR_COLOR, CEIL_COLOR, true));
		level.add(new Sector(rightStart, 0, 80, 80, FLOOR_COLOR, CEIL_COLOR, true));
		level.add(new Sector(leftRoom, 0, 80, 80, FLOOR_COLOR, CEIL_COLOR, true));
		level.add(new Sector(middleRoom, 0, 80, 80, FLOOR_COLOR, CEIL_COLOR, true));
		level.add(new Sector(rightRoom, 0, 80, 80, FLOOR_COLOR, CEIL_COLOR, true));
		level.add(new Sector(rightTriangle, 0, 80, 80, FLOOR_COLOR, CEIL_COLOR, false));
		

		
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
		if (pressedKeys.contains(KeyEvent.VK_UP) || pressedKeys.contains(KeyEvent.VK_Z)) {
			player.moveForward();
		}
		if (pressedKeys.contains(KeyEvent.VK_DOWN) || pressedKeys.contains(KeyEvent.VK_S)) {
			player.moveBackward();
		}
		if (pressedKeys.contains(KeyEvent.VK_LEFT) || pressedKeys.contains(KeyEvent.VK_Q)) {
			player.turnLeft();
		}
		if (pressedKeys.contains(KeyEvent.VK_RIGHT) || pressedKeys.contains(KeyEvent.VK_D)) {
			player.turnRight();
		}


		player.updateHeight();
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
