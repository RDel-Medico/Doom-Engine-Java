package bsp;

import dataType.BSPNode;
import java.util.ArrayList;
import main.Main;
import utility.Utility;

public class BSPTraversal {
	
	BSPNode root;
	ArrayList<Integer> idToDraw;
	
	public BSPTraversal(BSPNode root) {
		this.root = root;
		this.idToDraw = new ArrayList<>();
	}
	
	public ArrayList<Integer> getId() {
		return this.idToDraw;
	}
	
	public void update() {
		this.idToDraw.clear();
		this.traverse(root);
	}

	public void traverse(BSPNode curr) {
		if (curr == null) return;
		
		if (Utility.isInFront(curr.getSplit(), Main.player.vision)
		&& !Utility.isInFront(curr.getSplit(), Main.player.pos)) {
			this.traverse(curr.getFront());
			this.idToDraw.add(curr.getId());
			this.traverse(curr.getBack());
		} else {
			this.traverse(curr.getBack());
			this.traverse(curr.getFront());
		}
	}
}
