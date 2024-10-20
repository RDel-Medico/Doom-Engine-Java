package dataType;

import java.awt.Color;
import java.util.ArrayList;

public class Sector {
	private ArrayList<Segment> shape;

	private Color color;

	private int floorHeight;
	private int ceilHeight;
	
        @SuppressWarnings("OverridableMethodCallInConstructor")
	public Sector(ArrayList<Segment> seg, Color color, int floorHeight, int ceilHeight) {
		this.shape = seg;
		this.color = color;
		this.floorHeight = floorHeight;
		this.ceilHeight = ceilHeight;
		this.setSector();
	}

	public void setSector() {
		for (Segment s : this.shape)
			s.setSector(this); {
		}
	}

	public ArrayList<Segment> getSegments() {
		return shape;
	}
	
	public Segment getSegment(int i) {
		return shape.get(i);
	}
	
	public int getNbSegment() {
		return this.shape.size();
	}

	public void setSegments(ArrayList<Segment> seg) {
		this.shape = seg;
		this.setSector();
	}
	
	public int getMaxX () {
		int currMax = Integer.MIN_VALUE;
		
		for (Segment s : this.shape)
			currMax = Math.max(s.getMaxX(), currMax);
		
		return currMax;
	}
	
	public int getMaxY () {
		int currMax = Integer.MIN_VALUE;
		
		for (Segment s : this.shape)
			currMax = Math.max(s.getMaxY(), currMax);
		
		return currMax;
	}
	
	public int getMinX () {
		int currMin = Integer.MAX_VALUE;
		
		for (Segment s : this.shape)
			currMin = Math.min(s.getMinX(), currMin);
		
		return currMin;
	}
	
	public int getMinY () {
		int currMin = Integer.MAX_VALUE;
		
		for (Segment s : this.shape)
			currMin = Math.min(s.getMinY(), currMin);
		
		return currMin;
	}

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getFloorHeight() {
        return floorHeight;
    }

    public void setFloorHeight(int floorHeight) {
        this.floorHeight = floorHeight;
    }

    public int getCeilHeight() {
        return ceilHeight;
    }

    public void setCeilHeight(int ceilHeight) {
        this.ceilHeight = ceilHeight;
    }
}
