package design.patterns.example.editor.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import design.patterns.example.editor.TextEditor;
import design.patterns.example.editor.model.iterator.AllLinesIterator;
import design.patterns.example.editor.model.iterator.LineIterator;
import design.patterns.example.editor.model.iterator.RangeLinesIterator;

public class TextEditorModel {
	private List<String> lines;
	private LocationRange selectionRange;
	private Location cursorLocation;
	
	public TextEditorModel(String text) {
		super();
		lines = new ArrayList<String>(Arrays.asList(text.split("\n")));
		selectionRange = new LocationRange();
		cursorLocation = new Location(TextEditor.MARGIN_LEFT_RIGHT, TextEditor.MARGIN_TOP_BOTTOM);
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
		
}
