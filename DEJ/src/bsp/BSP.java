package bsp;

import java.util.ArrayList;

import map.Point;
import map.Sector;
import map.Segment;
import utility.Utility;

public class BSP {
	private BSPNode root;
	private Segment[] map;
	
	private Segment[] bspMap;
	
	public BSP (Sector[] m){
		int nbSegment = 0;
		for (Sector s : m) {
			nbSegment += s.getNbSegment();
		}
		
		this.map = new Segment[nbSegment];
		
		int ind = 0;
		for (Sector s : m) {
			Segment[] temp = s.getSegments();
			for (Segment se : temp) {
				this.map[ind++] = se;
			}
		}
		
		this.root = new BSPNode();
		this.buildBSP(this.root, this.bspMap);
	}
	
	public void addSegment(Segment splitter, BSPNode b) {
		b.id = this.bspMap.length;
		this.addSeg(splitter);
	}
	
	private void addSeg(Segment splitter) {
		Segment[] newSeg = new Segment[this.bspMap.length + 1];
		
		for (int i = 0; i < this.bspMap.length; i++) {
			newSeg[i] = this.bspMap[i];
		}
		
		newSeg[this.bspMap.length] = splitter;
		
		this.bspMap = newSeg;
	}

	public void buildBSP(BSPNode b, Segment[] s) {
		if (s.length == 0) return;
		
		Segment[] front = splitFrontSegments(s);
		Segment[] back = splitBackSegments(s);
		
		this.addSegment(s[0], b);
		
		if (front.length > 0) {
			b.front = new BSPNode();
			buildBSP(b.front, front);
		}
		
		if (back.length > 0) {
			b.back = new BSPNode();
			buildBSP(b.back, back);
		}
	}
	
	public Segment[] splitFrontSegments(Segment[] s) {
		Segment splitter = s[0];
		ArrayList<Segment> res = new ArrayList<>();
		
		for (int i = 1; i < s.length; i++) {
			if (Utility.isCollinear(splitter, s[i])) {
				res.add(s[i]);
				continue;
			}
			
			if (Utility.intersection(splitter, s[i])) {
				Point intersection = Utility.intersectionPoint(splitter, s[i]);
				
				Segment rightSegment = new Segment(s[i].getA(), intersection);
				Segment leftSegment = new Segment(intersection, s[i].getB());
				
				if (Utility.crossProduct2D(new Segment(splitter.getA(), s[i].getA()), splitter) > 0) {
					Segment temp = rightSegment;
					rightSegment = leftSegment;
					leftSegment = temp;
				}
				
				res.add(rightSegment);
				continue;
			}
			
			if (Utility.collisionOnFront(splitter, s[i])) {
				res.add(s[i]);
			}
		}
		return (Segment[])res.toArray();
	}
	
	public Segment[] splitBackSegments(Segment[] s) {
		Segment splitter = s[0];
		ArrayList<Segment> res = new ArrayList<>();
		
		for (int i = 1; i < s.length; i++) {
			
			if (Utility.intersection(splitter, s[i])) {
				Point intersection = Utility.intersectionPoint(splitter, s[i]);
				
				Segment rightSegment = new Segment(s[i].getA(), intersection);
				Segment leftSegment = new Segment(intersection, s[i].getB());
				
				if (Utility.crossProduct2D(new Segment(splitter.getA(), s[i].getA()), splitter) > 0) {
					Segment temp = rightSegment;
					rightSegment = leftSegment;
					leftSegment = temp;
				}
				
				res.add(leftSegment);
				continue;
			}
			
			if (Utility.collisionOnBack(splitter, s[i])) {
				res.add(s[i]);
			}
		}
		return (Segment[])res.toArray();
	}
}
