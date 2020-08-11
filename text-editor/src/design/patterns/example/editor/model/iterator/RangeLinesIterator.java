package design.patterns.example.editor.model.iterator;

import java.util.List;
import java.util.NoSuchElementException;

import design.patterns.example.editor.model.TextEditorModel;

public class RangeLinesIterator implements LineIterator {
	private int currIndex;
	private int endIndex;
	private List<String> lines;
	
	
	public RangeLinesIterator(TextEditorModel model, int startIndex, int endIndex) {
		super();
		this.lines = model.getLines();
		this.currIndex = startIndex;
		this.endIndex = endIndex;
	}

	@Override
	public boolean hasNext() {
		return currIndex < endIndex && currIndex < lines.size();
	}

	@Override
	public String next() {
		if(!this.hasNext()) {
			System.out.println(currIndex);
			throw new NoSuchElementException("Trying to access element outside of the given range!");
		}
		
		String element = lines.get(currIndex++);
		return element;
	}
	
}
