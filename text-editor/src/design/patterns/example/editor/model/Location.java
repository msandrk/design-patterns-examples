package design.patterns.example.editor.model;

public class Location {
	private int x, y;

	public Location(int x, int y) {
		super();
		this.x = x;
		this.y = y;
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
		addToX(1);
	}
	
	public void decrementX() {
		addToX(-1);
	}
	
	public void addToX(int deltaX) {
		this.x += deltaX;
	}
	
	public void incrementY() {
		addToY(1);
	}
	
	public void decrementY() {
		addToY(-1);
	}
	
	public void addToY(int deltaY) {
		this.y += deltaY;
	}
}
