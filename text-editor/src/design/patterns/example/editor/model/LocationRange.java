package design.patterns.example.editor.model;

public class LocationRange {
	private Location start;
	private Location end;

	public LocationRange() {
		this(-1, -1, -1, -1);
	}

	public LocationRange(LocationRange range) {
		this(range.getStart(), range.getEnd());
	}
	
	public LocationRange(Location start, Location end) {
		super();
		this.start = start;
		this.end = end;
		this.sortRange();
	}

	public LocationRange(int x1, int y1, int x2, int y2) {
		this(new Location(x1, y1), new Location(x2, y2));
	}

	public Location getStart() {
		return new Location(start);
	}

	public void setStart(Location start) {
		this.start = new Location(start);
		this.sortRange();
	}

	public Location getEnd() {
		return new Location(end);
	}

	public void setEnd(Location end) {
		this.end = new Location(end);
		this.sortRange();
	}
	
	public boolean isDefault() {
		return start.getX() == -1 && start.getY() == -1 && end.getX() == -1  && end.getY() == -1;
	}

	@Override
	public String toString() {
		return "[" + start  + ", " + end + "]";
	}
	
	private void sortRange() {
		if(end.compareTo(start) < 0) {
			Location tmp = new Location(end);
			end = new Location(start);
			start = tmp;
		}
	}
	
	
}
