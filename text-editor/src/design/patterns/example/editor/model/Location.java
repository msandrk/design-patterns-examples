package design.patterns.example.editor.model;

public class Location implements Comparable<Location>{
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

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (this.getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public int compareTo(Location loc) {
		if(y != loc.getY()) {
			return y - loc.getY();
		} else if(x != loc.getX()) {
			return x - loc.getX();
		}
		return 0;
	}

}
