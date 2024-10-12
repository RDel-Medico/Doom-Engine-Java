package bsp;

import java.util.ArrayList;

import dataType.BSPNode;
import dataType.Sector;
import dataType.Segment;
import utility.Utility;

public class BSP {
	private BSPNode root;
	
	private ArrayList<Segment> map;
	private ArrayList<Segment> bspMap;
	
	
	public BSP (ArrayList<Sector> m) {
		this.map = new ArrayList<Segment>();
		
		for (Sector s : m) {
			ArrayList<Segment> temp = s.getSegments();
			for (Segment se : temp)
				this.map.add(se);
		}
		
		this.root = new BSPNode();
		this.bspMap = new ArrayList<>();
		this.buildBSP(this.root, this.map);
	}

	public void buildBSP(BSPNode b, ArrayList<Segment> s) {
		ArrayList<Segment> front = splitFrontSegments(s);
		ArrayList<Segment> back = splitBackSegments(s);
		
		b.setId(this.bspMap.size());
		this.bspMap.add(s.get(0));
		
		b.setSplit(s.get(0));
		
		if (back.size() > 0) {
			b.setBack(new BSPNode());
			buildBSP(b.getBack(), back);
		} else {
			b.setBack(null);;
		}
		
		if (front.size() > 0) {
			b.setFront(new BSPNode());
			buildBSP(b.getFront(), front);
		} else {
			b.setFront(null);
		}
	}
	
	public ArrayList<Segment> splitFrontSegments(ArrayList<Segment> s) {
		Segment splitter = s.get(0);
		ArrayList<Segment> res = new ArrayList<Segment>();
		
		for (int i = 1; i < s.size(); i++) {
			if (Utility.isCollinear(splitter, s.get(i))) {
				res.add(s.get(i));
				continue;
			}
			
			if (Utility.intersection(splitter, s.get(i))) {
				if (Utility.crossProduct2D(new Segment(splitter.getA(), s.get(i).getA()), splitter) > 0) {
					res.add(new Segment(Utility.intersectionPoint(splitter, s.get(i)), s.get(i).getB()));
				} else {
					res.add(new Segment(s.get(i).getA(), Utility.intersectionPoint(splitter, s.get(i))));
				}
				continue;
			}
			
			if (Utility.collisionOnFront(splitter, s.get(i)))
				res.add(s.get(i));
		}
		return res;
	}
	
	public ArrayList<Segment> splitBackSegments(ArrayList<Segment> s) {
		Segment splitter = s.get(0);
		ArrayList<Segment> res = new ArrayList<Segment>();
		
		for (int i = 1; i < s.size(); i++) {
			if (Utility.intersection(splitter, s.get(i))) {
				if (Utility.crossProduct2D(new Segment(splitter.getA(), s.get(i).getA()), splitter) > 0) {
					res.add(new Segment(s.get(i).getA(), Utility.intersectionPoint(splitter, s.get(i))));
				} else {
					res.add(new Segment(Utility.intersectionPoint(splitter, s.get(i)), s.get(i).getB()));
				}
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
}
