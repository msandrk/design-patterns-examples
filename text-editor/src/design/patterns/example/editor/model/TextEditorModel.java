package design.patterns.example.editor.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import design.patterns.example.editor.model.iterator.AllLinesIterator;
import design.patterns.example.editor.model.iterator.LineIterator;
import design.patterns.example.editor.model.iterator.RangeLinesIterator;
import design.patterns.example.editor.model.observer.CursorObserver;

public class TextEditorModel {
	private List<String> lines;
	private LocationRange selectionRange;
	private Location cursorLocation;
	private List<CursorObserver> cursorObservers;
	
	public TextEditorModel(String text) {
		super();
		lines = new ArrayList<String>(Arrays.asList(text.split("\n")));
		selectionRange = new LocationRange();
		cursorLocation = new Location(0, 0);
		cursorObservers = new LinkedList<CursorObserver>();
	}

	public List<String> getLines() {
		return lines;
	}

	public void setLines(List<String> lines) {
		this.lines = lines;
	}

	public LocationRange getSelectionRange() {
		return selectionRange;
	}

	public void setSelectionRange(LocationRange selectionRange) {
		this.selectionRange = selectionRange;
	}

	public Location getCursorLocation() {
		return cursorLocation;
	}

	public void setCursorLocation(Location cursorLocation) {
		this.cursorLocation = cursorLocation;
	}
	
	public void setCursorLocation(int x, int y) {
		setCursorLocation(new Location(x, y));
	}
	
	//ITERATORS
	
	public LineIterator allLines() {
		return new AllLinesIterator(this);
	}
	
	public LineIterator linesRange(int index1, int index2) {
		if(index2 <= index1) {
			int tmp = index1;
			index1 = index2;
			index2 = tmp;
		}
		index1 = index1 < 0 ? 0 : index1;
		index2 = index2 > this.lines.size() ? this.lines.size() : index2;
		return new RangeLinesIterator(this, index1, index2);
	}
	
	// CURSOR MOVEMENTS
	
	public void moveCursorLeft() {
		if(cursorLocation.getX() > 0) {
			cursorLocation.decrementX();
			this.notifyCursorObservers();
		} else if (cursorLocation.getY() > 0) {
			cursorLocation.decrementY();
			cursorLocation.setX(lines.get(cursorLocation.getY()).length());
			this.notifyCursorObservers();
		}
	}
	
	public void moveCursorRight() {
		if(cursorLocation.getY() < lines.size()) {
			if(cursorLocation.getX() < lines.get(cursorLocation.getY()).length()) {
				cursorLocation.incrementX();
			} else {
				cursorLocation.incrementY();
				cursorLocation.setX(0);
			}
			this.notifyCursorObservers();
		}
	}
	
	public void moveCursorUp() {
		if(cursorLocation.getY() > 0) {
			cursorLocation.decrementY();
			if (cursorLocation.getX() > lines.get(cursorLocation.getY()).length()) {
				cursorLocation.setX(lines.get(cursorLocation.getY()).length());
			}
			this.notifyCursorObservers();
		}
	}
	
	public void moveCursorDown() {
		if(cursorLocation.getY() < lines.size()) {
			cursorLocation.incrementY();
			if(cursorLocation.getX() > lines.get(cursorLocation.getY()).length()) {
				cursorLocation.setX(lines.get(cursorLocation.getY()).length());
			}
			this.notifyCursorObservers();
		}
	}
	
	// Cursor observers methods
	
	public void notifyCursorObservers() {
		for(CursorObserver o : cursorObservers) {
			o.updateCursorLocation();
		}
	}
	
	public void attachCursorObserver(CursorObserver o) {
		cursorObservers.add(o);
	}
	
	public void dettachCursorObserver(CursorObserver o) {
		cursorObservers.remove(o);
	}
	
}
