package design.patterns.example.editor.model;

public class LocationRange {
	private Location start;
	private Location end;
	
	
	public LocationRange() {
		this(-1, -1, -1, -1);
	}
	
	/**
	 * @param start
	 * @param end
	 */
	public LocationRange(Location start, Location end) {
		super();
		this.start = start;
		this.end = end;
	}
	
	public LocationRange(int x1, int y1, int x2, int y2) {
		this.start = new Location(x1, y1);
		this.end = new Location(x2, y2);
	}

	public Location getStart() {
		return start;
	}

	public void setStart(Location start) {
		this.start = start;
	}

	public Location getEnd() {
		return end;
	}

	public void setEnd(Location end) {
		this.end = end;
	}
	
	
	
}
