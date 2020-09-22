package design.patterns.example.editor.model;

public class Location {
	private int x;
	private int y;

	public Location() {
		this.x = 0;
		this.y = 0;
	}
	
	public Location(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public Location(Location loc) {
		super();
		this.x = loc.getX();
		this.y = loc.getY();
	}
			
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public void incrementX() {
		x++;
	}
	
	public void decrementX() {
		x--;
	}
	
	public void incrementY() {
		y++;
	}
	
	public void decrementY() {
		y--;
	}
	
	@Override
	public String toString() {
		return "(" + this.x + ", " + this.y + ")";
	}
}
