package game;

import dataType.Point;
import dataType.Sector;
import dataType.Segment;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import javax.swing.JPanel;
import main.Main;
import player.Camera;
import utility.Utility;

public class Map extends JPanel{
	private ArrayList<Sector> map;
	private ArrayList<Sector> convertedMap;
	
	private ArrayList<Segment> bspMap; // Converted segments
	private ArrayList<Integer> bspSegmentVisible;
	
	
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
			toDraw.add(this.bspMap.get(i));

		toDraw.sort((s1, s2) -> {
			double distanceA = Utility.distance(Main.player.pos(), s1.getMiddle());
			double distanceB = Utility.distance(Main.player.pos(), s2.getMiddle());
			return Double.compare(distanceB, distanceA);
		});

		for (Segment curr : toDraw) {
			g.setColor(curr.getSector().getColor());
			this.drawWall3D(g, curr);
		}
	}

	private void drawWall3D(Graphics g, Segment s) {
		Point playerPos = Main.player.pos();

		double distanceA = Utility.distance(playerPos, s.getA());
		double distanceB = Utility.distance(playerPos, s.getB());

		int screenXA = (int) (Main.SCREEN_WIDTH / 2 + (s.getA().getX() - playerPos.getX()) / distanceA * Main.SCREEN_WIDTH);
		int screenYA = (int) (Main.SCREEN_HEIGHT / 2 - s.getFloorHeight() / distanceA * Main.SCREEN_HEIGHT);
		int screenYCeilingA = (int) (Main.SCREEN_HEIGHT / 2 - s.getCeilingHeight() / distanceA * Main.SCREEN_HEIGHT);

		int screenXB = (int) (Main.SCREEN_WIDTH / 2 + (s.getB().getX() - playerPos.getX()) / distanceB * Main.SCREEN_WIDTH);
		int screenYB = (int) (Main.SCREEN_HEIGHT / 2 - s.getFloorHeight() / distanceB * Main.SCREEN_HEIGHT);
		int screenYCeilingB = (int) (Main.SCREEN_HEIGHT / 2 - s.getCeilingHeight() / distanceB * Main.SCREEN_HEIGHT);

		g.fillPolygon(new int[]{screenXA, screenXB, screenXB, screenXA}, new int[]{screenYA, screenYB, screenYCeilingB, screenYCeilingA}, 4);

		g.setColor(Color.WHITE);
		((Graphics2D) g).setStroke(new BasicStroke(3)); // Set the thickness of the line
		g.drawLine(screenXA, screenYA, screenXB, screenYB); // Bottom line
		g.drawLine(screenXA, screenYCeilingA, screenXB, screenYCeilingB); // Top line
		g.drawLine(screenXA, screenYA, screenXA, screenYCeilingA);
    	g.drawLine(screenXB, screenYB, screenXB, screenYCeilingB);
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
		g.drawLine(playerPos.getX(), playerPos.getY(), leftFOVEndPoint.getX(), leftFOVEndPoint.getY());
		g.drawLine(playerPos.getX(), playerPos.getY(), rightFOVEndPoint.getX(), rightFOVEndPoint.getY());
	}

	public void drawBspSegment2D(Graphics g) {
		for (int i : this.bspSegmentVisible) {
			Segment curr = this.convertSeg(this.bspMap.get(i));
			g.setColor(curr.getSector().getColor());
			this.drawWall2D(g, curr);
			
			g.setColor(Color.ORANGE);
			g.fillOval(curr.getA().getX()-3, curr.getA().getY()-3, 6, 6);
			g.fillOval(curr.getB().getX()-3, curr.getB().getY()-3, 6, 6);
			
			Point middle = curr.getMiddle();
			g.drawString(Integer.toString(i), middle.getX(), middle.getY());
		}
	}
	
	public void drawNormalBspSeg2D(Graphics g) {
		g.setColor(Color.RED);
		for (int i : this.bspSegmentVisible)
			this.drawWall2D(g, this.convertSeg(this.bspMap.get(i)).normal(this.convertSeg(this.bspMap.get(i)).getMiddle()));
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
		
		return new Sector(res, sector.getColor(), sector.getFloorHeight(), sector.getCeilHeight());
	}
	
	private Segment convertSeg(Segment segment) {
		Segment res = new Segment(convertPoint(segment.getA()), convertPoint(segment.getB()));
		res.setSector(segment.getSector());
		return res;
	}
	
	private Point convertPoint(Point a) {
		return new Point((a.getX() - this.getMinX()) * (Main.DISPLAY_BOTTOM_RIGHT.getX() - Main.DISPLAY_TOP_LEFT.getX()) / (this.getMaxX() - this.getMinX()) + Main.DISPLAY_TOP_LEFT.getX(),
				(a.getY() - this.getMinY()) * (Main.DISPLAY_BOTTOM_RIGHT.getY() - Main.DISPLAY_TOP_LEFT.getY()) / (this.getMaxY() - this.getMinY()) + Main.DISPLAY_TOP_LEFT.getY());
	}
	
	public void drawCam2D(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillOval(convertPoint(Camera.getSelf().pos()).getX()-5, convertPoint(Camera.getSelf().pos()).getY()-5, 10, 10);
		this.drawWall2D(g, convertSeg(Camera.getSelf().getRotatedVision()));
	}
	
	public void drawNormal(Graphics g) {
		g.setColor(Color.RED);
		for (Sector s : this.convertedMap)
			for (Segment seg : s.getSegments())
				this.drawWall2D(g, seg.normal(seg.getMiddle()));
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
		g.drawLine(s.getA().getX(), s.getA().getY(), s.getB().getX(), s.getB().getY());
	}

	public int getMinX() {
		int min = Integer.MAX_VALUE;
		
		for (Sector s : this.map)
			min = Math.min(min, s.getMinX());

		return min;
	}
	
	public int getMaxX() {
		int max = Integer.MIN_VALUE;
		
		for (Sector s : this.map)
			max = Math.max(max, s.getMaxX());

		return max;
	}
	
	public int getMinY() {
		int min = Integer.MAX_VALUE;
		
		for (Sector s : this.map)
			min = Math.min(min, s.getMinY());

		return min;
	}
	
	public int getMaxY() {
		int max = Integer.MIN_VALUE;
		
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
