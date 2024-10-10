package map;

public class Sector {
	private Segment[] segments;
	
	public Sector() {
		this.setSegments(new Segment[0]);
	}
	
	public Sector(Segment[] seg) {
		this.setSegments(seg);
	}

	public Segment[] getSegments() {
		return segments;
	}
	
	public int getNbSegment() {
		return this.segments.length;
	}

	public void setSegments(Segment[] segments) {
		this.segments = segments;
	}
	
	public int getMaxX () {
		int currMax = Integer.MIN_VALUE;
		
		for (Segment s : this.segments) {
			int curr = s.getA().getX();
			if (s.getA().getX() > currMax) currMax = curr;
			curr = s.getB().getX();
			if (s.getB().getX() > currMax) currMax = curr;
		}
		
		return currMax;
	}
	
	public int getMaxY () {
		int currMax = Integer.MIN_VALUE;
		
		for (Segment s : this.segments) {
			int curr = s.getA().getY();
			if (s.getA().getY() > currMax) currMax = curr;
			curr = s.getB().getY();
			if (s.getB().getY() > currMax) currMax = curr;
		}
		
		return currMax;
	}
	
	public int getMinX () {
		int currMin = Integer.MAX_VALUE;
		
		for (Segment s : this.segments) {
			int curr = s.getA().getX();
			if (s.getA().getX() < currMin) currMin = curr;
			curr = s.getB().getX();
			if (s.getB().getX() < currMin) currMin = curr;
		}
		
		return currMin;
	}
	
	public int getMinY () {
		int currMin = Integer.MAX_VALUE;
		
		for (Segment s : this.segments) {
			int curr = s.getA().getY();
			if (s.getA().getY() < currMin) currMin = curr;
			curr = s.getB().getY();
			if (s.getB().getY() < currMin) currMin = curr;
		}
		
		return currMin;
	}
}
