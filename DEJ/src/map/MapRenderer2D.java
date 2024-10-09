package map;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class MapRenderer2D extends JPanel {
	private Sector[] map;
	Point topLeft;
	Point bottomRight;
	private final static int MAP_MIN_X = 100;
	private final static int MAP_MIN_Y = 100;
	private final static int MAP_MAX_X = 1400;
	private final static int MAP_MAX_Y = 800;
	
	Segment[] bspMap;
	int[] segId;
	
	public MapRenderer2D (int width, int height) {
		this.map = new Sector[0];
		this.topLeft = new Point (100, 100);
		this.topLeft = new Point (width - 100, height - 100);
	}
	
	public MapRenderer2D (Sector[] map) {
		this.topLeft = new Point(this.getMinX(map), this.getMinY(map));
		this.bottomRight = new Point(this.getMaxX(map), this.getMaxY(map));
		this.map = this.convertSectors(map);
	}
	
	public void setSegment(Segment[] s, int[] id) {
		Segment[] mapped = new Segment[s.length];
		
		for (int i = 0; i < s.length; i++) {
			mapped[i] = this.convertSeg(s[i]);
		}
		
		this.segId = id;
		this.bspMap = mapped;
	}
	
	private int getMinX(Sector[] map) {
		int currMin = Integer.MAX_VALUE;
		for (Sector s : map) {
			int curr = s.getMinX();
			if (curr < currMin) {
				currMin = curr;
			}
		}
		
		return currMin;
	}

	private int getMinY(Sector[] map) {
		int currMin = Integer.MAX_VALUE;
		
		for (Sector s : map) {
			int curr = s.getMinY();
			if (curr < currMin) {
				currMin = curr;
			}
		}
		
		return currMin;
	}
	
	private int getMaxX(Sector[] map) {
		int currMax = Integer.MIN_VALUE;
		for (Sector s : map) {
			int curr = s.getMaxX();
			if (curr > currMax) {
				currMax = curr;
			}
		}
		
		return currMax;
	}

	private int getMaxY(Sector[] map) {
		int currMax = Integer.MIN_VALUE;
		
		for (Sector s : map) {
			int curr = s.getMaxY();
			if (curr > currMax) {
				currMax = curr;
			}
		}
		
		return currMax;
	}

	@Override
    protected void paintComponent (Graphics g) {
		super.paintComponent(g);
        this.setBackground(Color.BLACK);
        
        g.setColor(Color.DARK_GRAY);
        drawOriginalSegment(g);
        
        drawBspSegment(g);
        
	}
	
	public void drawBspSegment(Graphics g) {
		for (int i : this.segId) {
			g.setColor(Color.WHITE);
			this.drawWall(g, this.bspMap[i]);
			g.setColor(Color.ORANGE);
			g.fillOval(this.bspMap[i].getA().getX()-3, this.bspMap[i].getA().getY()-3, 6, 6);
			g.fillOval(this.bspMap[i].getB().getX()-3, this.bspMap[i].getB().getY()-3, 6, 6);
		}
	}
	
	public void drawOriginalSegment(Graphics g) {
		for (Sector s : map) {
        	this.drawSector(g, s);
        }
	}
	
	private Sector[] convertSectors (Sector[] sectors) {
		Sector[] res = new Sector[sectors.length];
		
		for (int i = 0; i < sectors.length; i++) {
			res[i] = convertSector(sectors[i]);
		}
		
		return res;
	}
	
	private Sector convertSector(Sector sector) {
		Segment[] seg = sector.getSegments();
		Segment[] res = new Segment[seg.length];
		
		for (int i = 0; i < seg.length; i++) {
			res[i] = convertSeg(seg[i]);
		}
		
		return new Sector(res);
	}
	
	private Point convertPoint(Point a) {
		return new Point((a.getX() - topLeft.getX()) * (MAP_MAX_X - MAP_MIN_X) / (bottomRight.getX() - topLeft.getX()) + MAP_MIN_X,
				(a.getY() - topLeft.getY()) * (MAP_MAX_Y - MAP_MIN_Y) / (bottomRight.getY() - topLeft.getY()) + MAP_MIN_Y);
	}

	private Segment convertSeg(Segment segment) {
		return new Segment(convertPoint(segment.getA()), convertPoint(segment.getB()));
	}

	private void drawSector (Graphics g, Sector sec) {
		Segment[] walls = sec.getSegments();
		
		for (Segment seg : walls) {
			this.drawWall(g, seg);
		}
	}
	
	private void drawWall (Graphics g, Segment s) {
		g.drawLine(s.getA().getX(), s.getA().getY(), s.getB().getX(), s.getB().getY());
	}
}
