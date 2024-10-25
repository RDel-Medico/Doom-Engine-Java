package dataType;

import java.awt.Color;
import java.awt.image.BufferedImage;
import main.Main;

public class Segment {
	private Point a;
	private Point b;

	private Sector s;
	private Segment originalSegment;

	private Color middle;
	private Color top;
	private Color bottom;

	private BufferedImage middleTexture;
	private BufferedImage topTexture;
	private BufferedImage bottomTexture;

	private boolean collide;

	public Segment (Point a, Point b) {
		this.a = a;
		this.b = b;
		this.originalSegment = this;
		this.middle = null;
		this.top = null;
		this.bottom = null;
		this.middleTexture = null;
		this.topTexture = null;
		this.bottomTexture = null;
		this.collide = false;
	}
	
	public Segment (Point a, Point b, Color middle, Color top, Color bottom, boolean collide) {
		this.a = a;
		this.b = b;
		this.originalSegment = this;
		this.middle = middle;
		this.top = top;
		this.bottom = bottom;
		this.middleTexture = Main.middleWallTexture;
		this.topTexture = Main.topWallTexture;
		this.bottomTexture = Main.bottomWallTexture;
		this.collide = collide;
	}
	
	public Segment (double mvtX, double mvtY, Point a, Color middle, Color top, Color bottom, boolean collide) {
		this.a = new Point(a.getX() , a.getY());
		this.b = new Point(a.getX() + mvtX, a.getY() + mvtY);
		this.originalSegment = this;
		this.middle = middle;
		this.top = top;
		this.bottom = bottom;
		this.middleTexture = Main.middleWallTexture;
		this.topTexture = Main.topWallTexture;
		this.bottomTexture = Main.bottomWallTexture;
		this.collide = collide;
	}

	public void rotate(double yawDegrees) {
        double yawRadians = Math.toRadians(yawDegrees);

        double cosTheta = Math.cos(yawRadians);
        double sinTheta = Math.sin(yawRadians);

        int newMvtX = (int) Math.round(getXMouvement() * cosTheta - getYMouvement() * sinTheta);
        int newMvtY = (int) Math.round(getXMouvement() * sinTheta + getYMouvement() * cosTheta);

        this.b = new Point(this.a.getX() + newMvtX, this.a.getY() + newMvtY);
    }
	
	public Point getMiddlePoint() {
		double middleSegX = Math.min(this.a.getX(), this.b.getX()) + Math.abs((this.a.getX() - this.b.getX())) / 2;
		double middleSegY = Math.min(this.a.getY(), this.b.getY()) + Math.abs((this.a.getY() - this.b.getY())) / 2;
		
		return new Point(middleSegX, middleSegY);
	}

	public Segment getRootSegment() {
        Segment root = this;
        while (root.originalSegment != root) {
            root = root.originalSegment;
        }
        return root;
    }

	public Segment normal(Point a) {
		return new Segment(-this.getYMouvement() / 10, this.getXMouvement() / 10, a, Color.RED, Color.RED, Color.RED, false);
	}

	public int getFloorHeight() {
		return s.getFloorHeight();
	}

	public int getCeilingHeight() {
		return s.getCeilHeight();
	}

	public int getCeilingEnd() {
		return s.getCeilEnd();
	}

	public Sector getSector() {
		return s;
	}

	public void setSector(Sector s) {
		this.s = s;
	}
	
	public Point getA() {
		return a;
	}

	public Point getB() {
		return b;
	}
	
	public void setB(Point b) {
		this.b = b;
	}
	
	public void setA(Point a) {
		this.a = a;
	}
	
	public double getXMouvement() {
		return this.b.getX() - this.a.getX();
	}
	
	public double getYMouvement() {
		return this.b.getY() - this.a.getY();
	}
	
	public double getMaxX() {
		return this.a.getX() > this.b.getX() ? this.a.getX() : this.b.getX();
	}
	
	public double getMinX() {
		return this.a.getX() < this.b.getX() ? this.a.getX() : this.b.getX();
	}
	
	public double getMaxY() {
		return this.a.getY() > this.b.getY() ? this.a.getY() : this.b.getY();
	}
	
	public double getMinY() {
		return this.a.getY() < this.b.getY() ? this.a.getY() : this.b.getY();
	}

    public Segment getOriginalSegment() {
        return originalSegment;
    }

    public void setOriginalSegment(Segment originalSegment) {
        this.originalSegment = originalSegment;
    }

    public Color getTop() {
        return top;
    }

    public void setTop(Color top) {
        this.top = top;
    }

    public Color getBottom() {
        return bottom;
    }

    public void setBottom(Color bottom) {
        this.bottom = bottom;
    }

	public void setMiddle(Color middle) {
		this.middle = middle;
	}

	public Color getMiddle() {
		return middle;
	}

    public BufferedImage getTexture() {
        return middleTexture;
    }

    public void setTexture(BufferedImage texture) {
        this.middleTexture = texture;
    }

    public BufferedImage getTopTexture() {
        return topTexture;
    }

    public void setTopTexture(BufferedImage topTexture) {
        this.topTexture = topTexture;
    }

    public BufferedImage getBottomTexture() {
        return bottomTexture;
    }

    public void setBottomTexture(BufferedImage bottomTexture) {
        this.bottomTexture = bottomTexture;
    }

    public boolean isCollide() {
        return collide;
    }

    public void setCollide(boolean collide) {
        this.collide = collide;
    }
}
