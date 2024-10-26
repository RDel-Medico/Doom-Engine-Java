package main;

import bsp.BSP;
import bsp.BSPTraversal;
import dataType.Point;
import dataType.Sector;
import dataType.Segment;
import game.Map;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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

public class Main implements KeyListener, MouseMotionListener, MouseListener {
	public static final boolean DRAW_TEXTURE = true;
	public static final boolean DRAW_2D = true;
	public static final boolean DRAW_3D = false;
	public static final boolean DRAW_3D_FLOOR = false;
	public static final boolean DRAW_3D_CEIL = false;

	public final static int SCREEN_WIDTH = 1500;
	public final static int SCREEN_HEIGHT = 900;

	public final static int HEIGHT_SCALE = 8;

	public static final double COLLISION_TRESHOLD = 5.0;
	public static final double MOVE_SPEED = 0.5;
	public static final double TURN_SPEED = 3.0;
	public static final double FOV = 90.0;

	public final static Color CEIL_COLOR = Color.RED;
	public final static Color FLOOR_COLOR = new Color(139, 69, 19);

	public final static Point DISPLAY_TOP_LEFT = new Point(100, 100);
	public final static Point DISPLAY_BOTTOM_RIGHT = new Point(1400, 800);

	private static final int CENTER_X = SCREEN_WIDTH / 2;
	private static final int CENTER_Y = SCREEN_HEIGHT / 2;

	public static double weaponX = SCREEN_WIDTH / 2;
	public static double weaponY = SCREEN_HEIGHT - 150;
	public static double weaponTime = 0;
	public static final double WEAPON_AMPLITUDE = 30;
	public static final double WEAPON_FREQUENCY = 0.2;

	public static final double BOBBING_AMPLITUDE = 5.0;
	public static final double BOBBING_FREQUENCY = 0.4;

	public static final int FLOOR_HEIGHT = 0;
	public static final int CEIL_HEIGHT = 80;
	public static final int CEIL_END = 80;

	public static Camera player;
	public static Map map;
	public static BSPTraversal bspT;
	public static BSP bsp;
	private static Robot robot;

	public static BufferedImage middleWallTexture = null;
	public static BufferedImage topWallTexture = null;
	public static BufferedImage bottomWallTexture = null;
	public static BufferedImage ceilingTexture = null;
	public static BufferedImage floorTexture = null;
	public static BufferedImage skyTexture = null;
	public static BufferedImage levelFloorTexture = null;
	public static BufferedImage pistolTexture = null;
	public static BufferedImage pistolFiringTexture = null;

	public static Color bottomWallColor = Color.DARK_GRAY;
	public static Color middleWallColor = Color.GRAY;
	public static Color topWallColor = Color.LIGHT_GRAY;

	public static double bobbingTime = 0;
	public static double baseHeight;

	private static Set<Integer> pressedKeys = new HashSet<>();

	static {
		try {
			robot = new Robot();
		} catch (AWTException | SecurityException e) {
			System.out.println("Error creating robot");
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Doom Engine");
		frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setupGame();

		frame.add(map);
		frame.addKeyListener(new Main());
		frame.addMouseMotionListener(new Main());
		frame.addMouseListener(new Main());
		frame.setVisible(true);

		hideCursor(frame);

		Timer timer = new Timer(16, event -> update());
		timer.start();
	}

	public static void setupGame() {
		loadTexture();
		createLevel();
		createPlayer();
		updateMap();
	}

	private static void createPlayer() {
		player = Camera.getSelf();
	}

	private static void createLevel() {
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
		Point z = new Point(40, 110);
		Point aa = new Point(50, 110);
		Point ab = new Point(90, 110);

		ArrayList<Segment> leftStart = new ArrayList<>();
		leftStart.add(new Segment(e, a));
		leftStart.add(new Segment(f, e));
		leftStart.add(new Segment(b, f, null, null, null, null, null, null));
		leftStart.add(new Segment(a, b));

		ArrayList<Segment> middleStart = new ArrayList<>();
		middleStart.add(new Segment(f, b, null, null, null, null, null, null));
		middleStart.add(new Segment(g, f, null, Main.middleWallColor, null, null, null, Main.middleWallTexture));
		middleStart.add(new Segment(c, g, null, null, null, null, null, null));
		middleStart.add(new Segment(b, c));

		ArrayList<Segment> rightStart = new ArrayList<>();
		rightStart.add(new Segment(g, c, null, null, null, null, null, null));
		rightStart.add(new Segment(h, g));
		rightStart.add(new Segment(d, h));
		rightStart.add(new Segment(c, d));

		ArrayList<Segment> hallway = new ArrayList<>();
		hallway.add(new Segment(j, k, null, Main.middleWallColor, null, null, null, Main.middleWallTexture));
		hallway.add(new Segment(g, k, Main.middleWallColor, null, null, null, Main.middleWallTexture, null));
		hallway.add(new Segment(g, f, null, Main.middleWallColor, null, null, null, Main.middleWallTexture));
		hallway.add(new Segment(j, f, Main.middleWallColor, null, null, null, Main.middleWallTexture, null));

		ArrayList<Segment> leftRoom = new ArrayList<>();
		leftRoom.add(new Segment(y, i));
		leftRoom.add(new Segment(z, y));
		leftRoom.add(new Segment(j, z, null, null, null, null, null, null));
		leftRoom.add(new Segment(i, j));

		ArrayList<Segment> middleRoom = new ArrayList<>();
		middleRoom.add(new Segment(z, j, null, null, null, null, null, null));
		middleRoom.add(new Segment(aa, z));
		middleRoom.add(new Segment(k, aa, null, null, null, null, null, null));
		middleRoom.add(new Segment(j, k, null, null, null, null, null, null));

		ArrayList<Segment> rightRoom = new ArrayList<>();
		rightRoom.add(new Segment(aa, k, null, null, null, null, null, null));
		rightRoom.add(new Segment(ab, aa));
		rightRoom.add(new Segment(l, ab));
		rightRoom.add(new Segment(k, l));

		ArrayList<Segment> leftTriangle = new ArrayList<>();
		leftTriangle.add(new Segment(s, t, null, Main.topWallColor, Main.bottomWallColor, Main.bottomWallTexture, null,
				Main.topWallTexture));
		leftTriangle.add(new Segment(t, m, null, Main.topWallColor, Main.bottomWallColor, Main.bottomWallTexture, null,
				Main.topWallTexture));
		leftTriangle.add(new Segment(m, s, null, Main.topWallColor, Main.bottomWallColor, Main.bottomWallTexture, null,
				Main.topWallTexture));

		ArrayList<Segment> rightTriangle = new ArrayList<>();
		rightTriangle.add(new Segment(w, x));
		rightTriangle.add(new Segment(x, p));
		rightTriangle.add(new Segment(p, w));

		ArrayList<Segment> firstStep = new ArrayList<>();
		firstStep.add(new Segment(q, r, null, null, Main.bottomWallColor, Main.bottomWallTexture, null, null));
		firstStep.add(new Segment(r, o, null, null, Main.bottomWallColor, Main.bottomWallTexture, null, null));
		firstStep.add(new Segment(o, n, null, null, Main.bottomWallColor, Main.bottomWallTexture, null, null));
		firstStep.add(new Segment(n, q, null, null, Main.bottomWallColor, Main.bottomWallTexture, null, null));

		ArrayList<Segment> secondStep = new ArrayList<>();
		secondStep.add(new Segment(u, v, null, null, Main.bottomWallColor, Main.bottomWallTexture, null, null));
		secondStep.add(new Segment(v, r, null, null, Main.bottomWallColor, Main.bottomWallTexture, null, null));
		secondStep.add(new Segment(r, q, null, null, Main.bottomWallColor, Main.bottomWallTexture, null, null));
		secondStep.add(new Segment(q, u, null, null, Main.bottomWallColor, Main.bottomWallTexture, null, null));

		ArrayList<Segment> thirdStep = new ArrayList<>();
		thirdStep.add(new Segment(z, aa, null, null, Main.bottomWallColor, Main.bottomWallTexture, null, null));
		thirdStep.add(new Segment(aa, v, null, null, Main.bottomWallColor, Main.bottomWallTexture, null, null));
		thirdStep.add(new Segment(v, u, null, null, Main.bottomWallColor, Main.bottomWallTexture, null, null));
		thirdStep.add(new Segment(u, z, null, null, Main.bottomWallColor, Main.bottomWallTexture, null, null));

		level.add(new Sector(leftTriangle, 20, 70, 80, floorTexture, floorTexture));
		level.add(new Sector(firstStep, 10, 80, 80, Main.floorTexture, null));
		level.add(new Sector(secondStep, 20, 80, 80, Main.floorTexture, null));
		level.add(new Sector(thirdStep, 30, 80, 80, Main.floorTexture, null));

		level.add(new Sector(leftStart));
		level.add(new Sector(middleStart));
		level.add(new Sector(rightStart));
		level.add(new Sector(leftRoom));
		level.add(new Sector(middleRoom));
		level.add(new Sector(rightRoom));
		level.add(new Sector(rightTriangle));
		level.add(new Sector(hallway, 0, 70, 80, null, topWallTexture, null, Main.CEIL_COLOR));

		map = new Map(level);
		bsp = new BSP(level);
		bspT = new BSPTraversal(bsp.getRoot());
	}

	private static void loadTexture() {
		if (DRAW_TEXTURE) {
			try {
				middleWallTexture = ImageIO.read(new File("./ressources/wall.png"));
				topWallTexture = ImageIO.read(new File("./ressources/top.png"));
				bottomWallTexture = ImageIO.read(new File("./ressources/bottom.png"));
				ceilingTexture = ImageIO.read(new File("./ressources/ceiling.png"));
				floorTexture = ImageIO.read(new File("./ressources/floor.png"));
				skyTexture = ImageIO.read(new File("./ressources/background.png"));
				levelFloorTexture = ImageIO.read(new File("./ressources/floorLevel.png"));
				pistolTexture = ImageIO.read(new File("./ressources/pistol.png"));
				pistolFiringTexture = ImageIO.read(new File("./ressources/pistolFiring.png"));
			} catch (IOException e) {
				System.out.println("Error loading texture");
			}
		}
	}

	private static void update() {
		updatePlayer();
		updateMap();
	}

	private static void updateMap() {
		bspT.update();
		map.updateBspSegment(bsp.getSegments(), bspT.getId());
		map.repaint();
	}

	private static void updatePlayer() {
		boolean moving = false;

		if (pressedKeys.contains(KeyEvent.VK_UP) || pressedKeys.contains(KeyEvent.VK_Z)) {
			player.move(Camera.Direction.FORWARD, bsp.getSegments());
			moving = true;
		}

		if (pressedKeys.contains(KeyEvent.VK_DOWN) || pressedKeys.contains(KeyEvent.VK_S)) {
			player.move(Camera.Direction.BACKWARD, bsp.getSegments());
			moving = true;
		}

		if (pressedKeys.contains(KeyEvent.VK_LEFT) || pressedKeys.contains(KeyEvent.VK_Q)) {
			player.move(Camera.Direction.LEFT, bsp.getSegments());
			moving = true;
		}

		if (pressedKeys.contains(KeyEvent.VK_RIGHT) || pressedKeys.contains(KeyEvent.VK_D)) {
			player.move(Camera.Direction.RIGHT, bsp.getSegments());
			moving = true;
		}

		if (!moving) {
			weaponTime = 0;
			weaponX = SCREEN_WIDTH / 2;
			weaponY = SCREEN_HEIGHT - 150;
		} else {
			weaponTime += WEAPON_FREQUENCY;
			weaponX = SCREEN_WIDTH / 2 + WEAPON_AMPLITUDE * Math.sin(weaponTime);
			weaponY = SCREEN_HEIGHT - 150 + WEAPON_AMPLITUDE * Math.abs(Math.sin(weaponTime));
			bobbingTime += BOBBING_FREQUENCY;
		}

		player.updateHeight(moving);
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

	public static void hideCursor(JFrame frame) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		BufferedImage cursorImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Cursor invisibleCursor = toolkit.createCustomCursor(cursorImage, new java.awt.Point(0, 0), "InvisibleCursor");
		frame.setCursor(invisibleCursor);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int deltaX = e.getX() - Main.CENTER_X;
		int deltaY = e.getY() - Main.CENTER_Y;

		if (deltaX != 0 || deltaY != 0) {
			player.rotate(deltaX * 0.1); // Adjust sensitivity as needed
			robot.mouseMove(Main.CENTER_X, Main.CENTER_Y);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// Do nothing
	}

	@SuppressWarnings("unused")
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1 && Main.DRAW_TEXTURE) {
			triggerFiringAnimation();
		}
	}

	public static void triggerFiringAnimation() {
		pistolTexture = Main.pistolFiringTexture; // Switch to firing image
		map.repaint(); // Repaint to show the new image

		// Set a timer to revert back to the original image after 200 milliseconds
		Timer timer = new Timer(300, event -> {
			try {
				pistolTexture = ImageIO.read(new File("./ressources/pistol.png"));
			} catch (IOException e) {
				System.out.println("Error loading texture");
			}
			map.repaint(); // Repaint to show the original image
		});
		timer.setRepeats(false); // Only execute once
		timer.start();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Do nothing
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// Do nothing
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// Do nothing
	}
}
