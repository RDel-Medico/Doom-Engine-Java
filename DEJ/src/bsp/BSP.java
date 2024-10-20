package bsp;

import dataType.BSPNode;
import dataType.Sector;
import dataType.Segment;
import java.util.ArrayList;
import utility.Utility;

public class BSP {
	private BSPNode root;
	
	private ArrayList<Segment> map;
	private ArrayList<Segment> bspMap;
	
	
	public BSP (ArrayList<Sector> m) {
		this.map = new ArrayList<>();
		
		for (Sector s : m) {
			ArrayList<Segment> temp = s.getSegments();
			for (Segment se : temp) {
				se.setSector(s);
				this.map.add(se);
			}
		}
		
		this.root = new BSPNode();
		this.bspMap = new ArrayList<>();
		this.buildBSP(this.root, this.map);
	}

	private void buildBSP(BSPNode b, ArrayList<Segment> s) {
		ArrayList<Segment> front = splitFrontSegments(s);
		ArrayList<Segment> back = splitBackSegments(s);
		
		b.setId(this.bspMap.size());
		this.bspMap.add(s.get(0));
		
		b.setSplit(s.get(0));
		
		if (!back.isEmpty()) {
			b.setBack(new BSPNode());
			buildBSP(b.getBack(), back);
		} else {
			b.setBack(null);
		}
		
		if (!front.isEmpty()) {
			b.setFront(new BSPNode());
			buildBSP(b.getFront(), front);
		} else {
			b.setFront(null);
		}
	}
	
	public ArrayList<Segment> splitFrontSegments(ArrayList<Segment> s) {
		Segment splitter = s.get(0);
		ArrayList<Segment> res = new ArrayList<>();
		
		for (int i = 1; i < s.size(); i++) {
			if (Utility.isCollinear(splitter, s.get(i))) {
				res.add(s.get(i));
				continue;
			}
			
			if (Utility.intersection(splitter, s.get(i))) {
				Segment temp;
				if (Utility.crossProduct2D(new Segment(splitter.getA(), s.get(i).getA()), splitter) > 0) {
					temp = new Segment(Utility.intersectionPoint(splitter, s.get(i)), s.get(i).getB());
				} else {
					temp = new Segment(s.get(i).getA(), Utility.intersectionPoint(splitter, s.get(i)));
				}
				temp.setSector(s.get(i).getSector());
				res.add(temp);
				continue;
			}
			
			if (Utility.collisionOnFront(splitter, s.get(i)))
				res.add(s.get(i));
		}
		return res;
	}
	
	public ArrayList<Segment> splitBackSegments(ArrayList<Segment> s) {
		Segment splitter = s.get(0);
		ArrayList<Segment> res = new ArrayList<>();
		
		for (int i = 1; i < s.size(); i++) {
			if (Utility.intersection(splitter, s.get(i))) {
				Segment temp;
				if (Utility.crossProduct2D(new Segment(splitter.getA(), s.get(i).getA()), splitter) > 0) {
					temp = new Segment(s.get(i).getA(), Utility.intersectionPoint(splitter, s.get(i)));
				} else {
					temp = new Segment(Utility.intersectionPoint(splitter, s.get(i)), s.get(i).getB());
				}
				temp.setSector(s.get(i).getSector());
				res.add(temp);
				continue;
			}
			
			if (Utility.collisionOnBack(splitter, s.get(i)))
				res.add(s.get(i));
		}
		return res;
	}
	
	public BSPNode getRoot() {
		return this.root;
	}
	
	public ArrayList<Segment> getSegments() {
		return this.bspMap;
	}

    public void setRoot(BSPNode root) {
        this.root = root;
    }

    public ArrayList<Segment> getMap() {
        return map;
    }

    public void setMap(ArrayList<Segment> map) {
        this.map = map;
    }

    public ArrayList<Segment> getBspMap() {
        return bspMap;
    }

    public void setBspMap(ArrayList<Segment> bspMap) {
        this.bspMap = bspMap;
    }
}
