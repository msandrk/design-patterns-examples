package design.patterns.example.editor.model.iterator;

import java.util.List;
import java.util.NoSuchElementException;

import design.patterns.example.editor.model.TextEditorModel;

public class AllLinesIterator implements LineIterator {
	private List<String> lines;
	private int currIndex;
	
	public AllLinesIterator(TextEditorModel model) {
		lines = model.getLines();
		currIndex = 0;
	}
	
	
	@Override
	public boolean hasNext() {
		return currIndex < lines.size();
	}

	@Override
	public String next() {
		if(!this.hasNext()) {
			throw new NoSuchElementException("Trying to access element beyond the end of the collection!");
		}
		
		String element = lines.get(currIndex++);
		return element;
	}

}
