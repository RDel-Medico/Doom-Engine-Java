package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import dataType.Point;
import dataType.Sector;
import dataType.Segment;
import player.Camera;
import main.main;

public class Map extends JPanel{
	private ArrayList<Sector> map;
	private ArrayList<Sector> convertedMap;
	
	private ArrayList<Segment> bspMap;
	private ArrayList<Integer> bspSegmentVisible;
	
	
	public Map (ArrayList<Sector> map) {
		this.map = map;
		this.convertedMap = this.convertSectors(map);
		this.bspMap = new ArrayList<Segment>();
	}
	
	@Override
    protected void paintComponent (Graphics g) {
		super.paintComponent(g);
        this.setBackground(Color.BLACK);
        
        drawAllSector(g);
        
        //drawNormal(g);
        
        drawBspSegment(g);
        
        drawNormalBspSeg(g);
        
        drawCam(g);
	}
	
	public void drawBspSegment(Graphics g) {
		for (int i : this.bspSegmentVisible) {
			Segment curr = this.bspMap.get(i);
			g.setColor(Color.WHITE);
			this.drawWall(g, curr);
			
			g.setColor(Color.ORANGE);
			g.fillOval(curr.getA().getX()-3, curr.getA().getY()-3, 6, 6);
			g.fillOval(curr.getB().getX()-3, curr.getB().getY()-3, 6, 6);
			
			Point middle = curr.getMiddle();
			g.drawString(Integer.toString(i), middle.getX(), middle.getY());
		}
	}
	
	public void drawNormalBspSeg(Graphics g) {
		g.setColor(Color.RED);
		for (int i : this.bspSegmentVisible)
			this.drawWall(g, this.bspMap.get(i).normal(this.bspMap.get(i).getMiddle()));
	}
	
	public void updateBspSegment(ArrayList<Segment> s, ArrayList<Integer> id) {
		this.bspMap.clear();
		
		for (int i = 0; i < s.size(); i++)
			this.bspMap.add(this.convertSeg(s.get(i)));
		
		this.bspSegmentVisible = id;
	}
	
	private ArrayList<Sector> convertSectors (ArrayList<Sector> sectors) {
		ArrayList<Sector> res = new ArrayList<Sector>();
		
		for (int i = 0; i < sectors.size(); i++)
			res.add(convertSector(sectors.get(i)));
		
		return res;
	}
	
	private Sector convertSector(Sector sector) {
		ArrayList<Segment> res = new ArrayList<Segment>();
		
		for (int i = 0; i < sector.getNbSegment(); i++)
			res.add(convertSeg(sector.getSegment(i)));
		
		return new Sector(res);
	}
	
	private Segment convertSeg(Segment segment) {
		return new Segment(convertPoint(segment.getA()), convertPoint(segment.getB()));
	}
	
	private Point convertPoint(Point a) {
		return new Point((a.getX() - this.getMinX()) * (main.DISPLAY_BOTTOM_RIGHT.getX() - main.DISPLAY_TOP_LEFT.getX()) / (this.getMaxX() - this.getMinX()) + main.DISPLAY_TOP_LEFT.getX(),
				(a.getY() - this.getMinY()) * (main.DISPLAY_BOTTOM_RIGHT.getY() - main.DISPLAY_TOP_LEFT.getY()) / (this.getMaxY() - this.getMinY()) + main.DISPLAY_TOP_LEFT.getY());
	}
	
	public void drawCam(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillOval(convertPoint(Camera.getSelf().pos()).getX()-5, convertPoint(Camera.getSelf().pos()).getY()-5, 10, 10);
		this.drawWall(g, convertSeg(Camera.getSelf().vision));
	}
	
	public void drawNormal(Graphics g) {
		g.setColor(Color.RED);
		for (Sector s : this.convertedMap)
			for (Segment seg : s.getSegments())
				this.drawWall(g, seg.normal(seg.getMiddle()));
	}
	
	private void drawAllSector(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		for (Sector s : this.convertedMap)
			this.drawAllSegment(g, s);
	}

	private void drawAllSegment(Graphics g, Sector s) {
		for (Segment seg : s.getSegments())
			drawWall(g, seg);
	}

	private void drawWall(Graphics g, Segment s) {
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
}
