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

	public void setDefaultSelectionRange() {
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

	/**
	 * Deletes text left of cursor. Should be called upon pressing 'Backspace' key.
	 */
	public void deleteBefore() {
		int cursorX = cursorLocation.getX();
		int lineNumber = cursorLocation.getY();

		if (cursorX == 0 && lineNumber == 0) {
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

	/**
	 * Deletes char on the right of the cursor. Should be called upon pressing the
	 * 'Delete' key.
	 */
	public void deleteAfter() {
		int lineNumber = cursorLocation.getY();
		int cursorX = cursorLocation.getX();
		String currLine = lines.get(lineNumber);

		if (lineNumber == lines.size() - 1 && cursorX == currLine.length()) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		lines.remove(lineNumber);

		if (cursorX == currLine.length()) {
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

		for (int i = start.getY(); i <= end.getY(); i++) {
			lines.remove(start.getY());
		}

		lines.add(start.getY(), sb.toString());
		this.setCursorLocation(start);
		this.setDefaultSelectionRange();
		this.notifyTextObservers();
	}

	/**
	 * Inserts single character.
	 * 
	 * @param c - character to be inserted.
	 */
	public void insert(char c) {
		int lineNumber = cursorLocation.getY();
		int insertAt = cursorLocation.getX();
		String line = lines.remove(lineNumber);
		StringBuilder sb = new StringBuilder(line.substring(0, insertAt));
		if (c == '\n') {
			lines.add(lineNumber, sb.toString());
			lines.add(lineNumber + 1, line.substring(insertAt));
		} else {
			sb.append(c);
			sb.append(line.substring(insertAt < line.length() ? insertAt : line.length()));
			lines.add(lineNumber, sb.toString());
		}
		moveCursorRight();
		notifyTextObservers();
	}

	public void insert(String s) {
		if (s == null)
			return;

		List<String> newLines = new LinkedList<String>(Arrays.asList(s.split("\n")));
//		if (s.lastIndexOf("\n") == s.length() - 1) {
//			newLines = new LinkedList<String>(Arrays.asList(s.split("\n", -1)));
//		} else {
//			newLines = new LinkedList<String>(Arrays.asList(s.split("\n")));
//		}
		
		String firstLine = lines.remove(cursorLocation.getY());
		String partOne = firstLine.substring(0, cursorLocation.getX());
		String partTwo = firstLine.substring(cursorLocation.getX());

		firstLine = partOne + newLines.remove(0);
		newLines.add(0, firstLine);

		String lastLine = newLines.remove(newLines.size() - 1);
		int cursorX = lastLine.length();

		newLines.add(lastLine + partTwo);
		lines.addAll(cursorLocation.getY(), newLines);

		cursorLocation.setY(cursorLocation.getY() + newLines.size() - 1);
		cursorLocation.setX(cursorX);

		notifyTextObservers();
	}

	public String getSelectedText() {
		if (selectionRange.isDefault())
			return null;

		int x1 = selectionRange.getStart().getX();
		int y1 = selectionRange.getStart().getY();
		int x2 = selectionRange.getEnd().getX();
		int y2 = selectionRange.getEnd().getY();

		StringBuilder sb = new StringBuilder();

		for (int i = y1; i <= y2; i++) {
			String line = lines.get(i);
			if (i == y1) { // if first line of selection, start from x1 offset
				line = line.substring(x1);
			}
			if (i == y2) {
				if (i == y1) { // if selection in one line, adjust x2 (line was shortened in first condition)
					line = line.substring(0, x2 - x1);
				} else {
					line = line.substring(0, x2);
				}

			}
			if (i != y2 || i == y2 && x2 == lines.get(i).length()) {
				sb.append(line + "\n");
			} else {
				sb.append(line);
			}
		}

		return sb.toString();
	}

}
