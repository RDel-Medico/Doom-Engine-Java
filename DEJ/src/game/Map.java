package game;

import dataType.Point;
import dataType.Sector;
import dataType.Segment;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JPanel;
import main.Main;
import player.Camera;
import utility.Utility;

public class Map extends JPanel {
	enum WallPart {
		TOP, MIDDLE, BOTTOM
	}

	private ArrayList<Sector> map;
	private ArrayList<Sector> convertedMap;

	private ArrayList<Segment> bspMap;
	private ArrayList<Integer> bspSegmentVisible;

	public Map(ArrayList<Sector> map) {
		this.map = map;
		this.convertedMap = this.convertSectors(map);
		this.bspMap = new ArrayList<>();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setBackground(Color.BLACK);

		if (Main.DRAW_3D)
			this.draw3DMap(g);

		if (Main.DRAW_2D)
			this.draw2DMap(g);
	}

	public void draw3DMap(Graphics g) {
		this.drawSky(g);
		this.drawFloor(g);
		this.drawAllWall3D(g);
		this.drawPistol(g);
	}

	public void draw2DMap(Graphics g) {
		this.drawAllSector2D(g);
		this.drawBspSegment2D(g);
		this.drawNormalBspSeg2D(g);
		this.drawCam2D(g);
		this.drawFOVLines(g);
	}

	public void drawSky(Graphics g) {
		if (Main.skyTexture == null)
			return;

		for (int x = 0; x < Main.SCREEN_WIDTH; x += Main.skyTexture.getWidth())
			for (int y = 0; y < Main.SCREEN_HEIGHT / 2; y += Main.skyTexture.getHeight())
				g.drawImage(Main.skyTexture, x, y, this);
	}

	public void drawFloor(Graphics g) {
		if (Main.floorTexture == null)
			return;

		for (int x = 0; x < Main.SCREEN_WIDTH; x += Main.floorTexture.getWidth())
			for (int y = Main.SCREEN_HEIGHT / 2; y < Main.SCREEN_HEIGHT; y += Main.levelFloorTexture.getHeight())
				g.drawImage(Main.levelFloorTexture, x, y, this);
	}

	public void drawAllWall3D(Graphics g) {
		ArrayList<Segment> toDraw = new ArrayList<>();
		for (int i : this.bspSegmentVisible)
			toDraw.add(this.bspMap.get(i).getRootSegment());

		toDraw.sort((s1, s2) -> {
			double distanceA = Utility.distance(Main.player.getPos(), s1.getMiddlePoint());
			double distanceB = Utility.distance(Main.player.getPos(), s2.getMiddlePoint());
			return Double.compare(distanceB, distanceA);
		});

		Set<Sector> drawnSectors = new HashSet<>(); // Initialize the set

		for (Segment curr : toDraw) {
			drawWall3D(g, curr, drawnSectors);
		}
	}

	private void drawCeilingAndFloor(Graphics g, Sector sector, double cameraHeight, double playerYaw, double playerFOV,
			Point playerPos, double halfFOV, Segment leftFOVBoundary, Segment rightFOVBoundary) {
		ArrayList<Point> points = new ArrayList<>();

		for (Segment segment : sector.getSegments())
			Utility.addFOVPoint(points, segment, halfFOV, leftFOVBoundary, rightFOVBoundary, playerPos, playerYaw);

		// Project points to screen space
		ArrayList<Point> floorPoints = new ArrayList<>();
		ArrayList<Point> ceilingPoints = new ArrayList<>();

		for (int i = 0; i < points.size(); i++) {
			Point p = points.get(i);
			double distance = Utility.distance(playerPos, p);
			double angle = Utility.normalizeAngle(
					Math.toDegrees(Math.atan2(p.getY() - playerPos.getY(), p.getX() - playerPos.getX())) - playerYaw);
			int height = (int) (Main.SCREEN_HEIGHT / distance) * Main.HEIGHT_SCALE;

			if (distance < 1)
				continue;

			floorPoints.add(new Point(((angle + halfFOV) / playerFOV * Main.SCREEN_WIDTH),
					Main.SCREEN_HEIGHT / 2 + height / 2 - (int) (Main.SCREEN_HEIGHT / distance
							* (sector.getFloorHeight() - cameraHeight) / Main.HEIGHT_SCALE)));
			ceilingPoints.add(new Point(((angle + halfFOV) / playerFOV * Main.SCREEN_WIDTH),
					Main.SCREEN_HEIGHT / 2 + height / 2 - (int) (Main.SCREEN_HEIGHT / distance
							* (sector.getCeilHeight() - cameraHeight) / Main.HEIGHT_SCALE)));
		}

		// Order the floor and ceiling points based on the angle from the center of the
		// polygon
		Point polygonCenterF = Utility.calculatePolygonCenter(floorPoints);
		floorPoints.sort((p1, p2) -> Double.compare(Utility.angleFromCenter(polygonCenterF, p1),
				Utility.angleFromCenter(polygonCenterF, p2)));
		Point polygonCenterC = Utility.calculatePolygonCenter(ceilingPoints);
		ceilingPoints.sort((p1, p2) -> Double.compare(Utility.angleFromCenter(polygonCenterC, p1),
				Utility.angleFromCenter(polygonCenterC, p2)));

		// Draw floor
		drawFloor(g, sector, Utility.getXArray(floorPoints), Utility.getYArray(floorPoints));

		// Draw ceiling
		drawCeiling(g, sector, Utility.getXArray(ceilingPoints), Utility.getYArray(ceilingPoints));
	}

	@SuppressWarnings("unused")
	private void drawFloor(Graphics g, Sector sector, ArrayList<Integer> xPoints, ArrayList<Integer> yPointsFloor) {
		if (Main.DRAW_TEXTURE && (Main.DRAW_3D_FLOOR || sector.getFloorHeight() > 0)
				&& sector.getFloorTexture() != null) {
			int textureWidth = sector.getFloorTexture().getWidth();
			int textureHeight = sector.getFloorTexture().getHeight();
			Rectangle2D textureRect = new Rectangle2D.Double(0, 0, textureWidth, textureHeight);
			TexturePaint floorTexturePaint = new TexturePaint(sector.getFloorTexture(), textureRect);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setPaint(floorTexturePaint);
			g2d.fillPolygon(Utility.convertIntegers(xPoints), Utility.convertIntegers(yPointsFloor), xPoints.size());
		}

		if (!Main.DRAW_TEXTURE && (Main.DRAW_3D_FLOOR || sector.getFloorHeight() > 0) && sector.getFloorColor() != null
				&& sector.getFloorHeight() > 0) {
			g.setColor(sector.getFloorColor());
			g.fillPolygon(Utility.convertIntegers(xPoints), Utility.convertIntegers(yPointsFloor), xPoints.size());
		}
	}

	@SuppressWarnings("unused")
	private void drawCeiling(Graphics g, Sector sector, ArrayList<Integer> xPoints, ArrayList<Integer> yPointsCeiling) {
		if (Main.DRAW_TEXTURE && (Main.DRAW_3D_CEIL || sector.getCeilHeight() < Main.CEIL_HEIGHT)
				&& sector.getCeilingTexture() != null) {
			TexturePaint ceilingTexturePaint = new TexturePaint(sector.getCeilingTexture(), new Rectangle2D.Double(0, 0,
					sector.getCeilingTexture().getWidth(), sector.getCeilingTexture().getHeight()));
			Graphics2D g2d = (Graphics2D) g;
			g2d.setPaint(ceilingTexturePaint);
			g2d.fillPolygon(Utility.convertIntegers(xPoints), Utility.convertIntegers(yPointsCeiling), xPoints.size());

		}
		if (!Main.DRAW_TEXTURE && (Main.DRAW_3D_CEIL || sector.getCeilHeight() < Main.CEIL_HEIGHT)
				&& sector.getCeilColor() != null) {
			g.setColor(sector.getCeilColor());
			g.fillPolygon(Utility.convertIntegers(xPoints), Utility.convertIntegers(yPointsCeiling), xPoints.size());
		}
	}

	private void drawWall3D(Graphics g, Segment s, Set<Sector> drawnSectors) {
		Point playerPos = Main.player.getPos();
		double playerYaw = Main.player.getYaw();
		double playerFOV = Main.FOV;
		double cameraHeight = Main.player.getHeight();

		double halfFOV = playerFOV / 2.0;

		Point leftFOVEndPoint = Utility.calculateFOVEndPoint(playerPos, playerYaw, -halfFOV);
		Point rightFOVEndPoint = Utility.calculateFOVEndPoint(playerPos, playerYaw, halfFOV);
		Segment leftFOVBoundary = new Segment(playerPos, leftFOVEndPoint);
		Segment rightFOVBoundary = new Segment(playerPos, rightFOVEndPoint);

		// Calculate the distance from the camera to the segment endpoints
		Point a = s.getA();
		Point b = s.getB();

		double angleA = Utility.normalizeAngle(
				Math.toDegrees(Math.atan2(a.getY() - playerPos.getY(), a.getX() - playerPos.getX())) - playerYaw);
		double angleB = Utility.normalizeAngle(
				Math.toDegrees(Math.atan2(b.getY() - playerPos.getY(), b.getX() - playerPos.getX())) - playerYaw);

		// Check if point a is within the FOV
		if (angleA < -halfFOV || angleA > halfFOV) {
			if (Utility.boundedIntersection(s, leftFOVBoundary)) {
				a = Utility.intersectionPoint(s, leftFOVBoundary);
				if (angleB < -halfFOV || angleB > halfFOV)
					if (Utility.boundedIntersection(s, rightFOVBoundary))
						b = Utility.intersectionPoint(s, rightFOVBoundary);
			}
		}

		// Check if point b is within the FOV
		if (angleB < -halfFOV || angleB > halfFOV) {
			if (Utility.boundedIntersection(s, rightFOVBoundary)) {
				b = Utility.intersectionPoint(s, rightFOVBoundary);
				if (angleA < -halfFOV || angleA > halfFOV)
					if (Utility.boundedIntersection(s, leftFOVBoundary))
						a = Utility.intersectionPoint(s, leftFOVBoundary);
			}
		}

		drawWallFromExtremities(g, s, playerPos, playerYaw, playerFOV, cameraHeight, halfFOV, a, b, angleA, angleB);

		// Draw the floor and ceiling
		if (!drawnSectors.contains(s.getSector())) {
			drawCeilingAndFloor(g, s.getSector(), cameraHeight, playerYaw, playerFOV, playerPos, halfFOV,
					leftFOVBoundary, rightFOVBoundary);
			drawnSectors.add(s.getSector());
		}
	}

	private void drawWallFromExtremities(Graphics g, Segment s, Point playerPos, double playerYaw, double playerFOV,
			double cameraHeight, double halfFOV, Point a, Point b, double angleA, double angleB) {
		double distanceA = Utility.distance(playerPos, a);
		double distanceB = Utility.distance(playerPos, b);

		// Project the 2D points to 3D space
		angleA = Math.toDegrees(Math.atan2(a.getY() - playerPos.getY(), a.getX() - playerPos.getX())) - playerYaw;
		angleB = Math.toDegrees(Math.atan2(b.getY() - playerPos.getY(), b.getX() - playerPos.getX())) - playerYaw;

		angleA = Utility.normalizeAngle(angleA);
		angleB = Utility.normalizeAngle(angleB);

		// Convert the 2D coordinates to 3D screen coordinates
		int screenXA = (int) ((angleA + halfFOV) / playerFOV * Main.SCREEN_WIDTH);
		int screenXB = (int) ((angleB + halfFOV) / playerFOV * Main.SCREEN_WIDTH);

		// Scale the height of the wall based on the distance
		int heightA = (int) (Main.SCREEN_HEIGHT / distanceA) * Main.HEIGHT_SCALE;
		int heightB = (int) (Main.SCREEN_HEIGHT / distanceB) * Main.HEIGHT_SCALE;

		// Calculate the screen coordinates for the floor and ceiling heights
		int floorHeightA = (int) (Main.SCREEN_HEIGHT / distanceA * s.getFloorHeight() / Main.HEIGHT_SCALE);
		int floorHeightB = (int) (Main.SCREEN_HEIGHT / distanceB * s.getFloorHeight() / Main.HEIGHT_SCALE);
		int ceilHeightA = (int) (Main.SCREEN_HEIGHT / distanceA * s.getCeilingHeight() / Main.HEIGHT_SCALE);
		int ceilHeightB = (int) (Main.SCREEN_HEIGHT / distanceB * s.getCeilingHeight() / Main.HEIGHT_SCALE);
		int ceilEndHeightA = (int) (Main.SCREEN_HEIGHT / distanceA * s.getCeilingEnd() / Main.HEIGHT_SCALE);
		int ceilEndHeightB = (int) (Main.SCREEN_HEIGHT / distanceB * s.getCeilingEnd() / Main.HEIGHT_SCALE);

		// Determine the screen coordinates for the projected points
		// Bottom A and B are the bottom of the wall
		int bottomA = Main.SCREEN_HEIGHT / 2 + heightA / 2
				+ (int) (cameraHeight * Main.SCREEN_HEIGHT / distanceA / Main.HEIGHT_SCALE);
		int bottomB = Main.SCREEN_HEIGHT / 2 + heightB / 2
				+ (int) (cameraHeight * Main.SCREEN_HEIGHT / distanceB / Main.HEIGHT_SCALE);

		// Floor A and B are the top of the bottom wall
		int floorA = bottomA - floorHeightA;
		int floorB = bottomB - floorHeightB;

		// Ceil A and B are the bottom of the top wall
		int ceilA = bottomA - ceilHeightA;
		int ceilB = bottomB - ceilHeightB;

		// CeilEnd A and B are the top of the top wall
		int ceilEndA = bottomA - ceilEndHeightA;
		int ceilEndB = bottomB - ceilEndHeightB;

		// Draw the full wall
		drawFullWall(g, s, screenXA, screenXB, ceilA, ceilB, floorA, floorB, ceilEndA, ceilEndB, (int) distanceA,
				(int) distanceB, bottomA, bottomB);

		// Draw the outline of the wall
		drawOutline(g, WallPart.MIDDLE, s, screenXA, screenXB, ceilA, ceilB, floorA, floorB, ceilEndA, ceilEndB,
				bottomA, bottomB);
		drawOutline(g, WallPart.TOP, s, screenXA, screenXB, ceilA, ceilB, floorA, floorB, ceilEndA, ceilEndB, bottomA,
				bottomB);
	}

	private void drawOutline(Graphics g, WallPart wallPart, Segment s, int screenXA, int screenXB, int ceilA, int ceilB,
			int floorA, int floorB, int ceilEndA, int ceilEndB, int bottomA, int bottomB) {
		g.setColor(Color.BLACK);
		((Graphics2D) g).setStroke(new BasicStroke(3));
		switch (wallPart) {
		case TOP -> {
			if (s.getTopTexture() != null) {

				// Draw the top and bottom of the wall
				g.drawLine(screenXA, ceilEndA, screenXB, ceilEndB);
				g.drawLine(screenXA, ceilA, screenXB, ceilB);

				// Draw the left and right of the wall
				g.drawLine(screenXA, ceilEndA, screenXA, ceilA);
				g.drawLine(screenXB, ceilEndB, screenXB, ceilB);

			}
		}
		case MIDDLE -> {
			if (s.getMiddle() != null) {

				// Draw the top and bottom of the wall
				g.drawLine(screenXA, ceilA, screenXB, ceilB);
				g.drawLine(screenXA, floorA, screenXB, floorB);

				// Draw the left and right of the wall
				g.drawLine(screenXA, ceilA, screenXA, floorA);
				g.drawLine(screenXB, ceilB, screenXB, floorB);
			}
		}
		default -> {
			if (s.getBottom() != null) {

				// Draw the top and bottom of the wall
				g.drawLine(screenXA, floorA, screenXB, floorB);
				g.drawLine(screenXA, bottomA, screenXB, bottomB);

				// Draw the left and right of the wall
				g.drawLine(screenXA, floorA, screenXA, bottomA);
				g.drawLine(screenXB, floorB, screenXB, bottomB);
			}
		}
		}
		((Graphics2D) g).setStroke(new BasicStroke(1));
	}

	private void drawFullWall(Graphics g, Segment s, int screenXA, int screenXB, int ceilA, int ceilB, int floorA,
			int floorB, int ceilEndA, int ceilEndB, int distanceA, int distanceB, int bottomA, int bottomB) {
		drawWallPart(g, WallPart.TOP, s, screenXA, screenXB, ceilA, ceilB, floorA, floorB, ceilEndA, ceilEndB,
				(int) distanceA, (int) distanceB, bottomA, bottomB);
		drawWallPart(g, WallPart.MIDDLE, s, screenXA, screenXB, ceilA, ceilB, floorA, floorB, ceilEndA, ceilEndB,
				(int) distanceA, (int) distanceB, bottomA, bottomB);
		drawWallPart(g, WallPart.BOTTOM, s, screenXA, screenXB, ceilA, ceilB, floorA, floorB, ceilEndA, ceilEndB,
				(int) distanceA, (int) distanceB, bottomA, bottomB);
	}

	@SuppressWarnings("unused")
	private void drawWallPart(Graphics g, WallPart wallPart, Segment s, int screenXA, int screenXB, int ceilA,
			int ceilB, int floorA, int floorB, int ceilEndA, int ceilEndB, int distanceA, int distanceB, int bottomA,
			int bottomB) {
		Graphics2D g2d = (Graphics2D) g;
		switch (wallPart) {
		case TOP -> {
			if (Main.DRAW_TEXTURE && s.getTopTexture() != null) {
				int textureWidth = s.getTopTexture().getWidth();
				int textureHeight = s.getTopTexture().getHeight();
				Rectangle2D textureRect = new Rectangle2D.Double(0, 0, textureWidth, textureHeight);
				g2d.setPaint(new TexturePaint(s.getTopTexture(), textureRect));
				g2d.fillPolygon(new int[] { screenXA, screenXB, screenXB, screenXA },
						new int[] { ceilEndA, ceilEndB, ceilB, ceilA }, 4);
			}
			if (!Main.DRAW_TEXTURE && s.getTop() != null) {
				g2d.setColor(s.getTop());
				g2d.fillPolygon(new int[] { screenXA, screenXB, screenXB, screenXA },
						new int[] { ceilEndA, ceilEndB, ceilB, ceilA }, 4);
			}
		}
		case MIDDLE -> {
			// Draw the middle part of the wall
			if (Main.DRAW_TEXTURE && s.getTexture() != null) {
				int textureWidth = s.getTexture().getWidth();
				int textureHeight = s.getTexture().getHeight();
				Rectangle2D textureRect = new Rectangle2D.Double(0, 0, textureWidth, textureHeight);
				g2d.setPaint(new TexturePaint(s.getTexture(), textureRect));
				g2d.fillPolygon(new int[] { screenXA, screenXB, screenXB, screenXA },
						new int[] { ceilA, ceilB, floorB, floorA }, 4);
			}

			if (!Main.DRAW_TEXTURE && s.getMiddle() != null) {
				g2d.setColor(s.getMiddle());
				g2d.fillPolygon(new int[] { screenXA, screenXB, screenXB, screenXA },
						new int[] { ceilA, ceilB, floorB, floorA }, 4);
			}
		}
		default -> {
			if (distanceA > 1 && distanceB > 1) {
				if (Main.DRAW_TEXTURE && s.getBottomTexture() != null) {
					g2d.setPaint(new TexturePaint(s.getBottomTexture(),
							new Rectangle2D.Double(screenXA, floorA, screenXB - screenXA, bottomA - floorA)));
					g2d.fillPolygon(new int[] { screenXA, screenXB, screenXB, screenXA },
							new int[] { floorA, floorB, bottomB, bottomA }, 4);
				}
				if (!Main.DRAW_TEXTURE && s.getBottom() != null) {
					g2d.setColor(s.getBottom());
					g2d.fillPolygon(new int[] { screenXA, screenXB, screenXB, screenXA },
							new int[] { floorA, floorB, bottomB, bottomA }, 4);
				}
			}
		}
		}
	}

	private void drawPistol(Graphics g) {
		if (Main.pistolTexture == null)
			return;

		int pistolWidth = Main.pistolTexture.getWidth();
		int pistolHeight = Main.pistolTexture.getHeight();
		int x = (int) Main.weaponX - pistolWidth / 2;
		int y = (int) Main.weaponY - pistolHeight / 2;
		g.drawImage(Main.pistolTexture, x, y, null);
	}

	public void drawFOVLines(Graphics g) {
		Point playerPos = Main.player.getPos();
		double playerYaw = Main.player.getYaw();

		Point leftFOVEndPoint = convertPoint(Utility.calculateFOVEndPoint(playerPos, playerYaw, -Main.FOV / 2.0));
		Point rightFOVEndPoint = convertPoint(Utility.calculateFOVEndPoint(playerPos, playerYaw, Main.FOV / 2.0));

		playerPos = convertPoint(playerPos);

		g.setColor(Color.RED);
		g.drawLine((int) playerPos.getX(), (int) playerPos.getY(), (int) leftFOVEndPoint.getX(),
				(int) leftFOVEndPoint.getY());
		g.drawLine((int) playerPos.getX(), (int) playerPos.getY(), (int) rightFOVEndPoint.getX(),
				(int) rightFOVEndPoint.getY());
	}

	public void drawBspSegment2D(Graphics g) {
		for (int i : this.bspSegmentVisible) {
			Segment curr = this.convertSeg(this.bspMap.get(i).getRootSegment());
			g.setColor(curr.getMiddle());
			this.drawWall2D(g, curr);

			g.setColor(Color.ORANGE);
			g.fillOval((int) curr.getA().getX() - 3, (int) curr.getA().getY() - 3, 6, 6);
			g.fillOval((int) curr.getB().getX() - 3, (int) curr.getB().getY() - 3, 6, 6);

			Point middle = curr.getMiddlePoint();
			g.drawString(Integer.toString(i), (int) middle.getX(), (int) middle.getY());
		}
	}

	public void drawNormalBspSeg2D(Graphics g) {
		g.setColor(Color.RED);
		for (int i : this.bspSegmentVisible)
			this.drawWall2D(g,
					this.convertSeg(this.bspMap.get(i)).normal(this.convertSeg(this.bspMap.get(i)).getMiddlePoint()));
	}

	public void updateBspSegment(ArrayList<Segment> s, ArrayList<Integer> id) {
		this.bspMap.clear();

		for (int i = 0; i < s.size(); i++)
			this.bspMap.add(s.get(i));

		this.bspSegmentVisible = id;
	}

	private ArrayList<Sector> convertSectors(ArrayList<Sector> sectors) {
		ArrayList<Sector> res = new ArrayList<>();

		for (int i = 0; i < sectors.size(); i++)
			res.add(convertSector(sectors.get(i)));

		return res;
	}

	private Sector convertSector(Sector sector) {
		ArrayList<Segment> res = new ArrayList<>();

		for (Segment s : sector.getSegments())
			res.add(convertSeg(s));

		return new Sector(res, sector.getFloorHeight(), sector.getCeilHeight(), sector.getCeilEnd(),
				sector.getFloorColor(), sector.getCeilColor());
	}

	private Segment convertSeg(Segment segment) {
		Segment res = new Segment(convertPoint(segment.getA()), convertPoint(segment.getB()), segment.getMiddle(),
				segment.getTop(), segment.getBottom());
		res.setSector(segment.getSector());
		return res;
	}

	private Point convertPoint(Point a) {
		return new Point(
				(a.getX() - this.getMinX()) * (Main.DISPLAY_BOTTOM_RIGHT.getX() - Main.DISPLAY_TOP_LEFT.getX())
						/ (this.getMaxX() - this.getMinX()) + Main.DISPLAY_TOP_LEFT.getX(),
				(a.getY() - this.getMinY()) * (Main.DISPLAY_BOTTOM_RIGHT.getY() - Main.DISPLAY_TOP_LEFT.getY())
						/ (this.getMaxY() - this.getMinY()) + Main.DISPLAY_TOP_LEFT.getY());
	}

	public void drawCam2D(Graphics g) {
		g.setColor(Color.GREEN);
		Point camPos = convertPoint(Camera.getSelf().getPos());
		g.fillOval((int) camPos.getX() - 5, (int) camPos.getY() - 5, 10, 10);

		// Calculate the endpoint of the direction line
		double yaw = Camera.getSelf().getYaw();
		int length = 20; // Length of the direction line
		int endX = (int) (camPos.getX() + length * Math.cos(Math.toRadians(yaw)));
		int endY = (int) (camPos.getY() + length * Math.sin(Math.toRadians(yaw)));

		// Draw the direction line
		g.setColor(Color.RED);
		g.drawLine((int) camPos.getX(), (int) camPos.getY(), endX, endY);
	}

	public void drawNormal(Graphics g) {
		g.setColor(Color.RED);
		for (Sector s : this.convertedMap)
			for (Segment seg : s.getSegments())
				this.drawWall2D(g, seg.normal(seg.getMiddlePoint()));
	}

	private void drawAllSector2D(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		for (Sector s : this.convertedMap)
			this.drawAllSegment2D(g, s);
	}

	private void drawAllSegment2D(Graphics g, Sector s) {
		for (Segment seg : s.getSegments())
			drawWall2D(g, seg);
	}

	private void drawWall2D(Graphics g, Segment s) {
		g.drawLine((int) s.getA().getX(), (int) s.getA().getY(), (int) s.getB().getX(), (int) s.getB().getY());
	}

	public double getMinX() {
		double min = Integer.MAX_VALUE;

		for (Sector s : this.map)
			min = Math.min(min, s.getMinX());

		return min;
	}

	public double getMaxX() {
		double max = Integer.MIN_VALUE;

		for (Sector s : this.map)
			max = Math.max(max, s.getMaxX());

		return max;
	}

	public double getMinY() {
		double min = Integer.MAX_VALUE;

		for (Sector s : this.map)
			min = Math.min(min, s.getMinY());

		return min;
	}

	public double getMaxY() {
		double max = Integer.MIN_VALUE;

		for (Sector s : this.map)
			max = Math.max(max, s.getMaxY());

		return max;
	}

	public ArrayList<Sector> getMap() {
		return map;
	}

	public void setMap(ArrayList<Sector> map) {
		this.map = map;
	}

	public ArrayList<Sector> getConvertedMap() {
		return convertedMap;
	}

	public void setConvertedMap(ArrayList<Sector> convertedMap) {
		this.convertedMap = convertedMap;
	}

	public ArrayList<Segment> getBspMap() {
		return bspMap;
	}

	public void setBspMap(ArrayList<Segment> bspMap) {
		this.bspMap = bspMap;
	}
}
