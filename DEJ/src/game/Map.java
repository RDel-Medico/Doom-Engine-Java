package game;

import dataType.Point;
import dataType.Sector;
import dataType.Segment;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;
import main.Main;
import player.Camera;
import utility.Utility;

public class Map extends JPanel{
	private ArrayList<Sector> map;
	private ArrayList<Sector> convertedMap;
	
	private ArrayList<Segment> bspMap;
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
        
        this.draw2DMap(g);
		//this.draw3DMap(g);
	}

	public void draw2DMap(Graphics g) {
		drawAllSector2D(g);
		drawBspSegment2D(g);
		drawNormalBspSeg2D(g);
		drawCam2D(g);
	}

	public void draw3DMap(Graphics g) {
		for (Sector s : this.convertedMap)
			for (Segment seg : s.getSegments())
				this.drawWall3D(g, seg);
	}

	private void drawWall3D(Graphics g, Segment s) {
		Point a = s.getA();
		Point b = s.getB();
		
		Point screenA = Utility.project(a);
		Point screenB = Utility.project(b);

		int x1 = screenA.getX();
		int y1 = screenA.getY();
		int x2 = screenB.getX();
		int y2 = screenB.getY();

		int x3 = x1;
		int y3 = Main.DISPLAY_BOTTOM_RIGHT.getY();
		int x4 = x2;
		int y4 = Main.DISPLAY_BOTTOM_RIGHT.getY();

		g.setColor(Color.WHITE);
		g.drawLine(x1, y1, x3, y3);
		g.drawLine(x2, y2, x4, y4);

		g.setColor(Color.GRAY);
		g.drawLine(x3, y3, x4, y4);
	}
	

	public void drawBspSegment2D(Graphics g) {
		for (int i : this.bspSegmentVisible) {
			Segment curr = this.bspMap.get(i);
			g.setColor(Color.WHITE);
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
			this.drawWall2D(g, this.bspMap.get(i).normal(this.bspMap.get(i).getMiddle()));
	}
	
	public void updateBspSegment(ArrayList<Segment> s, ArrayList<Integer> id) {
		this.bspMap.clear();
		
		for (int i = 0; i < s.size(); i++)
			this.bspMap.add(this.convertSeg(s.get(i)));
		
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
		
		return new Sector(res);
	}
	
	private Segment convertSeg(Segment segment) {
		return new Segment(convertPoint(segment.getA()), convertPoint(segment.getB()));
	}
	
	private Point convertPoint(Point a) {
		return new Point((a.getX() - this.getMinX()) * (Main.DISPLAY_BOTTOM_RIGHT.getX() - Main.DISPLAY_TOP_LEFT.getX()) / (this.getMaxX() - this.getMinX()) + Main.DISPLAY_TOP_LEFT.getX(),
				(a.getY() - this.getMinY()) * (Main.DISPLAY_BOTTOM_RIGHT.getY() - Main.DISPLAY_TOP_LEFT.getY()) / (this.getMaxY() - this.getMinY()) + Main.DISPLAY_TOP_LEFT.getY());
	}
	
	public void drawCam2D(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillOval(convertPoint(Camera.getSelf().pos()).getX()-5, convertPoint(Camera.getSelf().pos()).getY()-5, 10, 10);
		this.drawWall2D(g, convertSeg(Camera.getSelf().vision));
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
