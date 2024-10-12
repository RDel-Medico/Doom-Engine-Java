package bsp;

import utility.Utility;

import java.util.ArrayList;

import dataType.BSPNode;
import main.main;

public class BSPTraversal {
	
	BSPNode root;
	ArrayList<Integer> idToDraw;
	
	public BSPTraversal(BSPNode root) {
		this.root = root;
		this.idToDraw = new ArrayList<Integer>();
	}
	
	public ArrayList<Integer> getId() {
		return this.idToDraw;
	}
	
	public void update() {
		this.idToDraw.clear();;
		this.traverse(root);
	}

	public void traverse(BSPNode curr) {
		if (curr == null) return;
		
		if (!Utility.isInFront(main.player.vision, curr.getSplit())) {
		//if (!Utility.isInFront(curr.getSplit(), main.player.pos)) {
			this.traverse(curr.getFront());
			this.idToDraw.add(curr.getId());
			this.traverse(curr.getBack());
		} else {
			this.traverse(curr.getBack());
			//this.addId(curr.id);
			this.traverse(curr.getFront());
		}
	}
}
