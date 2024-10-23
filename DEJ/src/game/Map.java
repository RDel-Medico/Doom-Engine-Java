package game;

import dataType.Point;
import dataType.Sector;
import dataType.Segment;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JPanel;
import main.Main;
import player.Camera;
import utility.Utility;

public class Map extends JPanel{
	private ArrayList<Sector> map;
	private ArrayList<Sector> convertedMap;
	
	private ArrayList<Segment> bspMap; // Converted segments
	private ArrayList<Integer> bspSegmentVisible;

	private final int HEIGHT_SCALE = 8;
	
	
	public Map (ArrayList<Sector> map) {
		this.map = map;
		this.convertedMap = this.convertSectors(map);
		this.bspMap = new ArrayList<>();
	}
	
	@Override
    protected void paintComponent (Graphics g) {
		super.paintComponent(g);
        this.setBackground(Color.BLACK);

		
        
		this.draw3DMap(g);
        this.draw2DMap(g);
	}

	public void draw3DMap(Graphics g) {
		ArrayList<Segment> toDraw = new ArrayList<>();
		for (int i : this.bspSegmentVisible)
			toDraw.add(this.bspMap.get(i).getRootSegment());

		toDraw.sort((s1, s2) -> {
			double distanceA = Utility.distance(Main.player.pos(), s1.getMiddlePoint());
			double distanceB = Utility.distance(Main.player.pos(), s2.getMiddlePoint());
			return Double.compare(distanceB, distanceA);
		});

		Set<Sector> drawnSectors = new HashSet<>(); // Initialize the set

		for (Segment curr : toDraw) {
			this.drawWall3D(g, curr, drawnSectors);
		}

		
	}

	private void drawCeilingAndFloor(Graphics g, Sector sector) {
		ArrayList<Point> points = new ArrayList<>();
		Point playerPos = Main.player.pos();
		double playerYaw = Main.player.getYaw();
		double playerFOV = Main.player.getFOV();
		double halfFOV = playerFOV / 2.0;

		Point leftFOVEndPoint = Utility.calculateFOVEndPoint(playerPos, playerYaw, -halfFOV);
		Point rightFOVEndPoint = Utility.calculateFOVEndPoint(playerPos, playerYaw, halfFOV);
		Segment leftFOVBoundary = new Segment(playerPos, leftFOVEndPoint, null, null, null);
		Segment rightFOVBoundary = new Segment(playerPos, rightFOVEndPoint, null, null, null);

		for (Segment segment : sector.getSegments()) {
			Point a;
			Point b;

			if (sector.isIsReversed()) {
				a = segment.getB();
				b = segment.getA();
			} else {
				a = segment.getA();
				b = segment.getB();
			}

			double angleA = Math.toDegrees(Math.atan2(a.getY() - playerPos.getY(), a.getX() - playerPos.getX())) - playerYaw;
			double angleB = Math.toDegrees(Math.atan2(b.getY() - playerPos.getY(), b.getX() - playerPos.getX())) - playerYaw;

			angleA = Utility.normalizeAngle(angleA);
			angleB = Utility.normalizeAngle(angleB);

			boolean aInFOV = angleA >= -halfFOV && angleA <= halfFOV;
			boolean bInFOV = angleB >= -halfFOV && angleB <= halfFOV;

			if (aInFOV) {
				points.add(a);
			} else if (Utility.boundedIntersection(segment, leftFOVBoundary)) {
				points.add(Utility.intersectionPoint(segment, leftFOVBoundary));
			} else if (Utility.boundedIntersection(segment, rightFOVBoundary)) {
				points.add(Utility.intersectionPoint(segment, rightFOVBoundary));
			}

			if (bInFOV) {
				points.add(b);
			} else if (Utility.boundedIntersection(segment, leftFOVBoundary)) {
				points.add(Utility.intersectionPoint(segment, leftFOVBoundary));
			} else if (Utility.boundedIntersection(segment, rightFOVBoundary)) {
				points.add(Utility.intersectionPoint(segment, rightFOVBoundary));
			}
		}

		// Project points to screen space
		int[] xPoints = new int[points.size()];
		int[] yPointsFloor = new int[points.size()];
		int[] yPointsCeiling = new int[points.size()];

		for (int i = 0; i < points.size(); i++) {
			Point p = points.get(i);
			double angle = Math.toDegrees(Math.atan2(p.getY() - playerPos.getY(), p.getX() - playerPos.getX())) - playerYaw;
			angle = Utility.normalizeAngle(angle);

			double distance = Utility.distance(playerPos, p);
			int screenX = (int) ((angle + halfFOV) / playerFOV * Main.SCREEN_WIDTH);
			int height = (int) (Main.SCREEN_HEIGHT / distance) * HEIGHT_SCALE;

			xPoints[i] = screenX;
			yPointsFloor[i] = Main.SCREEN_HEIGHT / 2 + height / 2 - (int) (Main.SCREEN_HEIGHT / distance * sector.getFloorHeight() / HEIGHT_SCALE);
			yPointsCeiling[i] = Main.SCREEN_HEIGHT / 2 + height / 2 - (int) (Main.SCREEN_HEIGHT / distance * sector.getCeilHeight() / HEIGHT_SCALE);
		}

		if (Utility.isPointInSector(playerPos, sector)) {
			// Add to the existing list the top left and top right points of the screen to ceiling and bottom left and bottom right points of the screen to floor
			xPoints = Arrays.copyOf(xPoints, 2 + xPoints.length);
			yPointsFloor = Arrays.copyOf(yPointsFloor, yPointsFloor.length + 2);
			yPointsCeiling = Arrays.copyOf(yPointsCeiling, yPointsCeiling.length + 2);

			xPoints[xPoints.length - 2] = 0;
			yPointsFloor[yPointsFloor.length - 2] = Main.SCREEN_HEIGHT;
			yPointsCeiling[yPointsCeiling.length - 2] = 0;

			xPoints[xPoints.length - 1] = Main.SCREEN_WIDTH;
			yPointsFloor[yPointsFloor.length - 1] = Main.SCREEN_HEIGHT;
			yPointsCeiling[yPointsCeiling.length - 1] = 0;
		}

		// Draw floor
		if (sector.getFloorColor() != null) {
			g.setColor(sector.getFloorColor());
			g.fillPolygon(xPoints, yPointsFloor, xPoints.length);
		}

		// Draw ceiling
		if (sector.getCeilColor() != null) {
			g.setColor(sector.getCeilColor());
			g.fillPolygon(xPoints, yPointsCeiling, xPoints.length);
		}
	}

	private void drawWall3D(Graphics g, Segment s, Set<Sector> drawnSectors) {
		Point playerPos = Main.player.pos();
		double playerYaw = Main.player.getYaw();
		double playerFOV = Main.player.getFOV();

		// Calculate the distance from the camera to the segment endpoints
		Point a = s.getA();
		Point b = s.getB();

		double angleA = Math.toDegrees(Math.atan2(a.getY() - playerPos.getY(), a.getX() - playerPos.getX())) - playerYaw;
		double angleB = Math.toDegrees(Math.atan2(b.getY() - playerPos.getY(), b.getX() - playerPos.getX())) - playerYaw;

		angleA = Utility.normalizeAngle(angleA);
		angleB = Utility.normalizeAngle(angleB);

		double halfFOV = playerFOV / 2.0;

		// Calculate FOV boundaries
		Point leftFOVEndPoint = Utility.calculateFOVEndPoint(playerPos, playerYaw, -halfFOV);
		Point rightFOVEndPoint = Utility.calculateFOVEndPoint(playerPos, playerYaw, halfFOV);
		Segment leftFOVBoundary = new Segment(playerPos, leftFOVEndPoint, Color.RED, Color.RED, Color.RED);
		Segment rightFOVBoundary = new Segment(playerPos, rightFOVEndPoint, Color.RED, Color.RED, Color.RED);

		// Check if point a is within the FOV
		if (angleA < -halfFOV || angleA > halfFOV) {
			if (Utility.boundedIntersection(s, leftFOVBoundary)) {
				a = Utility.intersectionPoint(s, leftFOVBoundary);
				if (angleB < -halfFOV || angleB > halfFOV) {
					if (Utility.boundedIntersection(s, rightFOVBoundary)) {
						b = Utility.intersectionPoint(s, rightFOVBoundary);
					}
				}
			} else if (Utility.boundedIntersection(s, rightFOVBoundary)) {
				a = Utility.intersectionPoint(s, rightFOVBoundary);
				if (angleB < -halfFOV || angleB > halfFOV) {
					if (Utility.boundedIntersection(s, leftFOVBoundary)) {
						b = Utility.intersectionPoint(s, leftFOVBoundary);
					}
				}
			}
		}

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
		int heightA = (int) (Main.SCREEN_HEIGHT / distanceA) * HEIGHT_SCALE;
		int heightB = (int) (Main.SCREEN_HEIGHT / distanceB) * HEIGHT_SCALE;

		// Calculate the screen coordinates for the floor and ceiling heights
		int floorHeightA = (int) (Main.SCREEN_HEIGHT / distanceA * s.getFloorHeight() / HEIGHT_SCALE);
		int floorHeightB = (int) (Main.SCREEN_HEIGHT / distanceB * s.getFloorHeight() / HEIGHT_SCALE);
		int ceilHeightA = (int) (Main.SCREEN_HEIGHT / distanceA * s.getCeilingHeight() / HEIGHT_SCALE);
		int ceilHeightB = (int) (Main.SCREEN_HEIGHT / distanceB * s.getCeilingHeight() / HEIGHT_SCALE);

		// Determine the screen coordinates for the projected points
		int bottomA = Main.SCREEN_HEIGHT / 2 + heightA / 2;
		int bottomB = Main.SCREEN_HEIGHT / 2 + heightB / 2;
		int floorA = bottomA - floorHeightA;
		int floorB = bottomB - floorHeightB;
		int ceilA = bottomA - ceilHeightA;
		int ceilB = bottomB - ceilHeightB;

		// Draw the floor and ceiling
		if (!drawnSectors.contains(s.getSector())) {
			drawCeilingAndFloor(g, s.getSector());
			drawnSectors.add(s.getSector());
		}

		// Draw the bottom part of the wall
		if (s.getBottom() != null) {
			g.setColor(s.getBottom());
			int[] bottomXPoints = {screenXA, screenXB, screenXB, screenXA};
			int[] bottomYPoints = {bottomA, bottomB, floorB, floorA};
			g.fillPolygon(bottomXPoints, bottomYPoints, 4);
		}

		// Draw the middle part of the wall
		if (s.getMiddle() != null) {
			g.setColor(s.getMiddle());
			int[] middleXPoints = {screenXA, screenXB, screenXB, screenXA};
			int[] middleYPoints = {floorA, floorB, ceilB, ceilA};
			g.fillPolygon(middleXPoints, middleYPoints, 4);
			
		}

		// Draw the top part of the wall
		if (s.getTop() != null) {
			g.setColor(s.getTop());
			int[] topXPoints = {screenXA, screenXB, screenXB, screenXA};
			int[] topYPoints = {ceilA, ceilB, ceilB - heightB, ceilA - heightA};
			g.fillPolygon(topXPoints, topYPoints, 4);
		}

		if (s.getMiddle() != null) {
			g.setColor(Color.WHITE);
			((Graphics2D) g).setStroke(new BasicStroke(3));
			// Draw the top and bottom of the wall
			g.drawLine(screenXA, ceilA, screenXB, ceilB);
			g.drawLine(screenXA, bottomA, screenXB, bottomB);
			// Draw the left and right of the wall
			g.drawLine(screenXA, bottomA, screenXA, ceilA);
			g.drawLine(screenXB, bottomB, screenXB, ceilB);
			((Graphics2D) g).setStroke(new BasicStroke(1));
		}
	}

	public void draw2DMap(Graphics g) {
		drawAllSector2D(g);
		drawBspSegment2D(g);
		drawNormalBspSeg2D(g);
		drawCam2D(g);
		drawFOVLines(g);
	}

	public void drawFOVLines(Graphics g) {
		Point playerPos = Main.player.pos();
		double playerYaw = Main.player.getYaw();
		double playerFOV = Main.player.getFOV();

		Point leftFOVEndPoint = convertPoint(Utility.calculateFOVEndPoint(playerPos, playerYaw, - playerFOV / 2.0));
		Point rightFOVEndPoint = convertPoint(Utility.calculateFOVEndPoint(playerPos, playerYaw, playerFOV / 2.0));

		playerPos = convertPoint(playerPos);

		g.setColor(Color.RED);
		g.drawLine((int) playerPos.getX(), (int) playerPos.getY(), (int) leftFOVEndPoint.getX(), (int) leftFOVEndPoint.getY());
		g.drawLine((int) playerPos.getX(), (int) playerPos.getY(), (int) rightFOVEndPoint.getX(), (int) rightFOVEndPoint.getY());
	}

	public void drawBspSegment2D(Graphics g) {
		for (int i : this.bspSegmentVisible) {
			Segment curr = this.convertSeg(this.bspMap.get(i).getRootSegment());
			g.setColor(curr.getMiddle());
			this.drawWall2D(g, curr);
			
			g.setColor(Color.ORANGE);
			g.fillOval((int) curr.getA().getX()-3, (int) curr.getA().getY()-3, 6, 6);
			g.fillOval((int) curr.getB().getX()-3, (int) curr.getB().getY()-3, 6, 6);
			
			Point middle = curr.getMiddlePoint();
			g.drawString(Integer.toString(i), (int) middle.getX(), (int) middle.getY());
		}
	}
	
	public void drawNormalBspSeg2D(Graphics g) {
		g.setColor(Color.RED);
		for (int i : this.bspSegmentVisible)
			this.drawWall2D(g, this.convertSeg(this.bspMap.get(i)).normal(this.convertSeg(this.bspMap.get(i)).getMiddlePoint()));
	}
	
	public void updateBspSegment(ArrayList<Segment> s, ArrayList<Integer> id) {
		this.bspMap.clear();
		
		for (int i = 0; i < s.size(); i++)
			this.bspMap.add(s.get(i));
		
		this.bspSegmentVisible = id;
	}
	
	private ArrayList<Sector> convertSectors (ArrayList<Sector> sectors) {
		ArrayList<Sector> res = new ArrayList<>();
		
		for (int i = 0; i < sectors.size(); i++)
			res.add(convertSector(sectors.get(i)));
		
		return res;
	}
	
	private Sector convertSector(Sector sector) {
		ArrayList<Segment> res = new ArrayList<>();
		
		for (int i = 0; i < sector.getNbSegment(); i++)
			res.add(convertSeg(sector.getSegment(i)));
		
		return new Sector(res, sector.getFloorHeight(), sector.getCeilHeight(), sector.getFloorColor(), sector.getCeilColor(), sector.isIsReversed());
	}
	
	private Segment convertSeg(Segment segment) {
		Segment res = new Segment(convertPoint(segment.getA()), convertPoint(segment.getB()), segment.getMiddle(), segment.getTop(), segment.getBottom());
		res.setSector(segment.getSector());
		return res;
	}
	
	private Point convertPoint(Point a) {
		return new Point((a.getX() - this.getMinX()) * (Main.DISPLAY_BOTTOM_RIGHT.getX() - Main.DISPLAY_TOP_LEFT.getX()) / (this.getMaxX() - this.getMinX()) + Main.DISPLAY_TOP_LEFT.getX(),
				(a.getY() - this.getMinY()) * (Main.DISPLAY_BOTTOM_RIGHT.getY() - Main.DISPLAY_TOP_LEFT.getY()) / (this.getMaxY() - this.getMinY()) + Main.DISPLAY_TOP_LEFT.getY());
	}
	
	public void drawCam2D(Graphics g) {
		g.setColor(Color.GREEN);
		Point camPos = convertPoint(Camera.getSelf().pos());
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
