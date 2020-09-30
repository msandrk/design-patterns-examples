package design.patterns.example.editor.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import design.patterns.example.editor.model.iterator.AllLinesIterator;
import design.patterns.example.editor.model.iterator.LineIterator;
import design.patterns.example.editor.model.iterator.RangeLinesIterator;
import design.patterns.example.editor.model.observer.CursorObserver;
import design.patterns.example.editor.model.observer.TextObserver;

public class TextEditorModel {
	private List<String> lines;
	private LocationRange selectionRange;
	private Location cursorLocation;
	private List<CursorObserver> cursorObservers;
	private List<TextObserver> textObservers;

	public TextEditorModel(String text) {
		super();
		lines = new ArrayList<String>(Arrays.asList(text.split("\n")));
		selectionRange = new LocationRange();
		cursorLocation = new Location(0, 0);
		cursorObservers = new LinkedList<CursorObserver>();
		textObservers = new LinkedList<TextObserver>();
	}

	public List<String> getLines() {
		return lines;
	}

	public void setLines(List<String> lines) {
		this.lines = lines;
	}

	public LocationRange getSelectionRange() {
		return new LocationRange(selectionRange);
	}

	public void setSelectionRange(LocationRange selectionRange) {
		this.selectionRange = new LocationRange(selectionRange);
	}
	
	public void setDeafualtSelectionRange() {
		this.setSelectionRange(new LocationRange());
	}
	
	public Location getCursorLocation() {
		return new Location(cursorLocation);
	}

	public void setCursorLocation(Location cursorLocation) {
		this.cursorLocation = new Location(cursorLocation);
	}

	public void setCursorLocation(int x, int y) {
		this.cursorLocation = new Location(x, y);
	}

	// ITERATORS

	public LineIterator allLines() {
		return new AllLinesIterator(this);
	}

	public LineIterator linesRange(int index1, int index2) {
		if (index2 <= index1) {
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
		if (cursorLocation.getX() > 0) {
			cursorLocation.decrementX();
			this.notifyCursorObservers();
		} else if (cursorLocation.getY() > 0) {
			cursorLocation.decrementY();
			cursorLocation.setX(lines.get(cursorLocation.getY()).length());
			this.notifyCursorObservers();
		}
	}

	public void moveCursorRight() {
		if (cursorLocation.getY() < lines.size()) {
			if (cursorLocation.getX() < lines.get(cursorLocation.getY()).length()) {
				cursorLocation.incrementX();
			} else if (cursorLocation.getY() < lines.size() - 1) {
				cursorLocation.incrementY();
				cursorLocation.setX(0);
			}
			this.notifyCursorObservers();
		}
	}

	public void moveCursorUp() {
		if (cursorLocation.getY() > 0) {
			cursorLocation.decrementY();
			if (cursorLocation.getX() > lines.get(cursorLocation.getY()).length()) {
				cursorLocation.setX(lines.get(cursorLocation.getY()).length());
			}
			this.notifyCursorObservers();
		}
	}

	public void moveCursorDown() {
		if (cursorLocation.getY() < lines.size() - 1) {
			cursorLocation.incrementY();
			if (cursorLocation.getX() > lines.get(cursorLocation.getY()).length()) {
				cursorLocation.setX(lines.get(cursorLocation.getY()).length());
			}
			this.notifyCursorObservers();
		}
	}

	// CURSOR OBSERVERS METHODS

	public void attachCursorObserver(CursorObserver o) {
		cursorObservers.add(o);
	}

	public void dettachCursorObserver(CursorObserver o) {
		cursorObservers.remove(o);
	}

	public void notifyCursorObservers() {
		for (CursorObserver o : cursorObservers) {
			o.updateCursorLocation();
		}
	}

	// TEXT OBSERVERS METHODS

	public void attachTextObserver(TextObserver o) {
		textObservers.add(o);
	}

	public void dettachTextObserver(TextObserver o) {
		textObservers.remove(o);
	}

	public void notifyTextObservers() {
		for (TextObserver o : textObservers) {
			o.updateText();
		}
	}

	// TEXT MANIPULATION

	public void deleteBefore() {
		int cursorX = cursorLocation.getX();
		int lineNumber = cursorLocation.getY();

		if(cursorX == 0 && lineNumber == 0) {
			return;
		}
			
		String currLine = lines.get(lineNumber);
		lines.remove(lineNumber);

		if (cursorX == 0) {
			String prevLine = lines.get(lineNumber - 1);
			StringBuilder sb = new StringBuilder(prevLine);
			sb.append(currLine);

			lines.remove(lineNumber - 1);
			lines.add(lineNumber - 1, sb.toString());

			cursorLocation.setX(prevLine.length());
			cursorLocation.decrementY();

		} else {

			StringBuilder sb = new StringBuilder(currLine.length());
			sb.append(currLine.substring(0, cursorX - 1));
			if (cursorX < currLine.length()) {
				sb.append(currLine.substring(cursorX));
			}

			lines.add(lineNumber, sb.toString());
			cursorLocation.decrementX();
		}
		
		this.notifyTextObservers();
			
	}
	
	public void deleteAfter() {
		int lineNumber = cursorLocation.getY();
		int cursorX = cursorLocation.getX();
		String currLine = lines.get(lineNumber);
		
		if(lineNumber == lines.size() - 1 && cursorX == currLine.length()) {
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		lines.remove(lineNumber);
		
		if(cursorX == currLine.length()) {
			sb.append(currLine);
			sb.append(lines.get(lineNumber));
			lines.remove(lineNumber);
		} else {
			sb.append(currLine.substring(0, cursorX));
			sb.append(currLine.substring(cursorX + 1));
		}
		
		lines.add(lineNumber, sb.toString());
		this.notifyTextObservers();
	}
	
	public void deleteRange(LocationRange range) {
		StringBuilder sb = new StringBuilder();
		Location start = range.getStart();
		Location end = range.getEnd();
		
		sb.append(lines.get(start.getY()).substring(0, start.getX()));
		sb.append(lines.get(end.getY()).substring(end.getX()));
		
		for(int i = start.getY(); i <= end.getY(); i++) {
			lines.remove(start.getY());
		}
		
		lines.add(sb.toString());
		this.setCursorLocation(start);
		this.notifyTextObservers();
	}
	
}
