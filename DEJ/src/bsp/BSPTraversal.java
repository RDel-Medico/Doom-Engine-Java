package bsp;

import map.Point;
import map.Segment;
import utility.Utility;

public class BSPTraversal {
	
	BSPNode root;
	Segment[] map;
	
	Segment cam;
	int[] idToDraw;
	
	public BSPTraversal(BSPNode root, Segment[] map) {
		this.root = root;
		this.map = map;
		
		this.cam = new Segment(new Point(0,0), new Point(1, 1));
		this.idToDraw = new int[0];
	}
	
	public int[] getId() {
		return this.idToDraw;
	}
	
	public void update() {
		this.idToDraw = new int[0];
		this.traverse(root);
	}
	
	private void addId(int id) {
		int[] newId = new int[this.idToDraw.length + 1];
		
		for (int i = 0; i < this.idToDraw.length; i++) {
			newId[i] = this.idToDraw[i];
		}
		
		newId[this.idToDraw.length] = id;
		
		this.idToDraw = newId;
	}

	public void traverse(BSPNode curr) {
		if (curr == null) return;
		
		if (Utility.isInFront(cam, curr.split)) {
			this.traverse(curr.front);
			this.addId(curr.id);
			this.traverse(curr.back);
		} else {
			this.traverse(curr.back);
			this.addId(curr.id);
			this.traverse(curr.front);
		}
	}
}
