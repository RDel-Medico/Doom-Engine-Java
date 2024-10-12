package dataType;

import java.util.ArrayList;

public class Sector {
	private ArrayList<Segment> shape;
	
	public Sector(ArrayList<Segment> seg) {
		this.shape = seg;
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
}
