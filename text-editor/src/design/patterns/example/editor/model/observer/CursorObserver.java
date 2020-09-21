package design.patterns.example.editor.model.observer;

import design.patterns.example.editor.model.Location;

public interface CursorObserver {
	void updateCursorLocation(Location loc);
}
