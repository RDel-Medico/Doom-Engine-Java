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

	public void traverseWithoutAdd(BSPNode curr) {
		if (curr == null) return;
		this.traverse(curr.getBack());
		this.traverse(curr.getFront());
	}

	public void traverse(BSPNode curr) {
		if (curr == null) return;
		
		if (Utility.isInFront(curr.getSplit(), Main.player.pos)) {
			traverseWithoutAdd(curr);
			return;
		}

		if (!Utility.isWithinFOV(curr.getSplit(), Main.player.pos(), Main.player.getFOV(), Main.player.getYaw())) {
			traverseWithoutAdd(curr);
			return;
		}

		this.traverse(curr.getFront());
		this.idToDraw.add(curr.getId());
		this.traverse(curr.getBack());
	}
}
