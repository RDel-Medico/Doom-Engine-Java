package dataType;

public class BSPNode {
	private BSPNode front;
	private BSPNode back;
	private int id;
	private Segment split;
	
	public BSPNode getFront() {
		return front;
	}

	public void setFront(BSPNode front) {
		this.front = front;
	}

	public BSPNode getBack() {
		return back;
	}

	public void setBack(BSPNode back) {
		this.back = back;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Segment getSplit() {
		return split;
	}

	public void setSplit(Segment split) {
		this.split = split;
	}
}
