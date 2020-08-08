package design.patterns.example.editor.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextEditorModel {
	List<String> lines;
	LocationRange selectionRange;
	Location cursorLocation;
	
	public TextEditorModel(String text) {
		super();
		lines = new ArrayList<String>(Arrays.asList(text.split("\n")));
		selectionRange = new LocationRange();
		cursorLocation = new Location(1, 0);
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
	
//	public void setCursorX(int x) {
//		this.cursorLocation.setX(x);
//	}
//	
//	public void setCursorY(int y) {
//		this.cursorLocation.setY(y);
//	}
//	
	
}
