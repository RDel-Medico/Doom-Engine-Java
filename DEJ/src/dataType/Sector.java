package dataType;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Main;

public class Sector {
	private ArrayList<Segment> shape;

	private int floorHeight;
	private int ceilHeight;
	private int ceilEnd;

	private Color floorColor;
	private Color ceilColor;

	private BufferedImage ceilingTexture;
	private BufferedImage floorTexture;

	public Sector(ArrayList<Segment> seg, int floorHeight, int ceilHeight, int ceilEnd, Color floorColor,
			Color ceilColor) {
		this.shape = seg;
		this.floorHeight = floorHeight;
		this.ceilHeight = ceilHeight;
		this.floorColor = floorColor;
		this.ceilColor = ceilColor;
		this.ceilEnd = ceilEnd;
		this.ceilingTexture = null;
		this.floorTexture = null;
		initializeSegments();
	}

	public Sector(ArrayList<Segment> seg) {
		this.shape = seg;
		this.floorHeight = Main.FLOOR_HEIGHT;
		this.ceilHeight = Main.CEIL_HEIGHT;
		this.floorColor = Main.FLOOR_COLOR;
		this.ceilColor = Main.CEIL_COLOR;
		this.ceilEnd = Main.CEIL_END;
		this.ceilingTexture = Main.ceilingTexture;
		this.floorTexture = Main.floorTexture;
		initializeSegments();
	}

	public Sector(ArrayList<Segment> seg, int floorHeight, int ceilHeight, int ceilEnd, BufferedImage fText,
			BufferedImage cText, Color fColor, Color cColor) {
		this.shape = seg;
		this.floorHeight = floorHeight;
		this.ceilHeight = ceilHeight;
		this.floorColor = fColor;
		this.ceilColor = cColor;
		this.ceilEnd = ceilEnd;
		this.ceilingTexture = cText;
		this.floorTexture = fText;
		initializeSegments();
	}

	public Sector(ArrayList<Segment> seg, int floorHeight, int ceilHeight, int ceilEnd, BufferedImage fText,
			BufferedImage cText) {
		this.shape = seg;
		this.floorHeight = floorHeight;
		this.ceilHeight = ceilHeight;
		this.floorColor = Main.FLOOR_COLOR;
		this.ceilColor = Main.CEIL_COLOR;
		this.ceilEnd = ceilEnd;
		this.ceilingTexture = cText;
		this.floorTexture = fText;
		initializeSegments();
	}

	public Sector(ArrayList<Segment> seg, int floorHeight, int ceilHeight, int ceilEnd) {
		this.shape = seg;
		this.floorHeight = floorHeight;
		this.ceilHeight = ceilHeight;
		this.floorColor = Main.FLOOR_COLOR;
		this.ceilColor = Main.CEIL_COLOR;
		this.ceilEnd = ceilEnd;
		this.ceilingTexture = Main.ceilingTexture;
		this.floorTexture = Main.floorTexture;
		initializeSegments();
	}

	private void initializeSegments() {
		for (Segment s : this.shape) {
			s.setSector(this);
		}
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
		this.initializeSegments();
	}

	public double getMaxX() {
		double currMax = Integer.MIN_VALUE;

		for (Segment s : this.shape)
			currMax = Math.max(s.getMaxX(), currMax);

		return currMax;
	}

	public double getMaxY() {
		double currMax = Integer.MIN_VALUE;

		for (Segment s : this.shape)
			currMax = Math.max(s.getMaxY(), currMax);

		return currMax;
	}

	public double getMinX() {
		double currMin = Integer.MAX_VALUE;

		for (Segment s : this.shape)
			currMin = Math.min(s.getMinX(), currMin);

		return currMin;
	}

	public double getMinY() {
		double currMin = Integer.MAX_VALUE;

		for (Segment s : this.shape)
			currMin = Math.min(s.getMinY(), currMin);

		return currMin;
	}

	public int getFloorHeight() {
		return floorHeight;
	}

	public void setFloorHeight(int floorHeight) {
		this.floorHeight = floorHeight;
	}

	public int getCeilHeight() {
		return ceilHeight;
	}

	public void setCeilHeight(int ceilHeight) {
		this.ceilHeight = ceilHeight;
	}

	public Color getFloorColor() {
		return floorColor;
	}

	public void setFloorColor(Color floorColor) {
		this.floorColor = floorColor;
	}

	public Color getCeilColor() {
		return ceilColor;
	}

	public void setCeilColor(Color ceilColor) {
		this.ceilColor = ceilColor;
	}

	public int getCeilEnd() {
		return ceilEnd;
	}

	public void setCeilEnd(int ceilEnd) {
		this.ceilEnd = ceilEnd;
	}

	public BufferedImage getCeilingTexture() {
		return ceilingTexture;
	}

	public void setCeilingTexture(BufferedImage ceilingTexture) {
		this.ceilingTexture = ceilingTexture;
	}

	public BufferedImage getFloorTexture() {
		return floorTexture;
	}

	public void setFloorTexture(BufferedImage floorTexture) {
		this.floorTexture = floorTexture;
	}
}
