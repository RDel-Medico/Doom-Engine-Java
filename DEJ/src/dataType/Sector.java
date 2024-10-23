package dataType;

import java.awt.Color;
import java.util.ArrayList;

public class Sector {
	private ArrayList<Segment> shape;

	private int floorHeight;
	private int ceilHeight;

	private Color floorColor;
	private Color ceilColor;

	private boolean isReversed;
	
        @SuppressWarnings("OverridableMethodCallInConstructor")
	public Sector(ArrayList<Segment> seg, int floorHeight, int ceilHeight, Color floorColor, Color ceilColor, boolean isReversed) {
		this.shape = seg;
		this.floorHeight = floorHeight;
		this.ceilHeight = ceilHeight;
		this.setSector();
		this.floorColor = floorColor;
		this.ceilColor = ceilColor;
		this.isReversed = isReversed;
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
	
	public double getMaxX () {
		double currMax = Integer.MIN_VALUE;
		
		for (Segment s : this.shape)
			currMax = Math.max(s.getMaxX(), currMax);
		
		return currMax;
	}
	
	public double getMaxY () {
		double currMax = Integer.MIN_VALUE;
		
		for (Segment s : this.shape)
			currMax = Math.max(s.getMaxY(), currMax);
		
		return currMax;
	}
	
	public double getMinX () {
		double currMin = Integer.MAX_VALUE;
		
		for (Segment s : this.shape)
			currMin = Math.min(s.getMinX(), currMin);
		
		return currMin;
	}
	
	public double getMinY () {
		double currMin = Integer.MAX_VALUE;
		
		for (Segment s : this.shape)
			currMin = Math.min(s.getMinY(), currMin);
		
		return currMin;
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

    public Color getFloorColor() {
        return floorColor;
    }

    public void setFloorColor(Color floorColor) {
        this.floorColor = floorColor;
    }

    public Color getCeilColor() {
        return ceilColor;
    }

    public void setCeilColor(Color ceilColor) {
        this.ceilColor = ceilColor;
    }

    public boolean isIsReversed() {
        return isReversed;
    }

    public void setIsReversed(boolean isReversed) {
        this.isReversed = isReversed;
    }
}
