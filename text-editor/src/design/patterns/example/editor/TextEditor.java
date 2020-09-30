package design.patterns.example.editor;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import design.patterns.example.editor.model.Location;
import design.patterns.example.editor.model.LocationRange;
import design.patterns.example.editor.model.TextEditorModel;
import design.patterns.example.editor.model.iterator.LineIterator;
import design.patterns.example.editor.model.observer.CursorObserver;
import design.patterns.example.editor.model.observer.TextObserver;

public class TextEditor extends JFrame implements CursorObserver, TextObserver {
	private static final long serialVersionUID = 1L;
	public static final int MARGIN_TOP_BOTTOM = 45;
	public static final int MARGIN_LEFT_RIGHT = 5;
	private TextEditorModel model;

	public TextEditor(TextEditorModel model) {
		this.model = model;
		model.setCursorLocation(model.getLines().get(model.getLines().size() - 1).length(),
				model.getLines().size() - 1);
		model.attachCursorObserver(this);
		model.attachTextObserver(this);
		this.setTitle("GoF Text Editor");
		this.setSize(600, 400);
		this.getContentPane().setBackground(Color.WHITE);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		this.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isShiftDown()) {
					LocationRange range = model.getSelectionRange();
					Location rangeStart = range.getStart();
					Location rangeEnd = range.getEnd();
					if (e.getKeyCode() == KeyEvent.VK_LEFT) {
						if (range.isDefault() || rangeStart.compareTo(rangeEnd) == 0) {
							Location end = model.getCursorLocation();
							model.moveCursorLeft();
							range = new LocationRange(model.getCursorLocation(), end);
						} else {
							model.moveCursorLeft();
							Location cursorLocation = model.getCursorLocation();
							if (cursorLocation.compareTo(rangeStart) == 0 && rangeStart.getY() == rangeEnd.getY()
									&& rangeEnd.compareTo(rangeStart) == 1) {
								range = new LocationRange();
							} else if (rangeStart.compareTo(cursorLocation) > 0) {
								range.setStart(cursorLocation);
							} else if (rangeStart.compareTo(cursorLocation) < 0) {
								range.setEnd(cursorLocation);
							}
						}

					} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {

						if (range.isDefault() || range.getStart().compareTo(range.getEnd()) == 0) {
							Location start = model.getCursorLocation();
							model.moveCursorRight();
							range = new LocationRange(start, model.getCursorLocation());
						} else {
							model.moveCursorRight();
							Location cursorLocation = model.getCursorLocation();
							if (cursorLocation.compareTo(rangeEnd) == 0 && rangeStart.getY() == rangeEnd.getY()
									&& rangeEnd.compareTo(rangeStart) == 1) {
								range = new LocationRange();
							} else if (rangeEnd.compareTo(cursorLocation) > 0) {
								range.setStart(cursorLocation);
							} else if (rangeEnd.compareTo(cursorLocation) < 0) {
								range.setEnd(cursorLocation);
							}
						}

					} else if (e.getKeyCode() == KeyEvent.VK_UP) {
						if (range.isDefault() || rangeStart.equals(rangeEnd)) {
							range.setEnd(model.getCursorLocation());
							model.moveCursorUp();
							range.setStart(model.getCursorLocation());
						} else {
							model.moveCursorUp();
							Location cursorLocation = model.getCursorLocation();
							if (rangeStart.equals(cursorLocation) && rangeStart.compareTo(rangeEnd) == 1) {
								range = new LocationRange();
							} else if (rangeStart.compareTo(cursorLocation) > 0) {
								range.setStart(cursorLocation);
							} else if (rangeStart.compareTo(cursorLocation) < 0) {
								range.setEnd(cursorLocation);
							}
						}
					} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
						if (range.isDefault() || rangeStart.equals(rangeEnd)) {
							Location start = model.getCursorLocation();
							model.moveCursorDown();
							range = new LocationRange(start, model.getCursorLocation());
						} else {
							model.moveCursorDown();
							Location cursorLocation = model.getCursorLocation();
							if(rangeEnd.equals(cursorLocation) && rangeEnd.compareTo(rangeStart) == 1) {
								range = new LocationRange();
							} else if(rangeEnd.compareTo(cursorLocation) > 0) {
								range.setStart(cursorLocation);
							} else if(rangeEnd.compareTo(cursorLocation) < 0) {
								range.setEnd(cursorLocation);
							}
						}
					}
					model.setSelectionRange(range);
				} else {
					if (e.getKeyCode() == KeyEvent.VK_LEFT) {
						model.setDeafualtSelectionRange();
						model.moveCursorLeft();
					} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
						model.setDeafualtSelectionRange();
						model.moveCursorRight();
					} else if (e.getKeyCode() == KeyEvent.VK_UP) {
						model.setDeafualtSelectionRange();
						model.moveCursorUp();
					} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
						model.setDeafualtSelectionRange();
						model.moveCursorDown();
					} else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
						if (model.getSelectionRange().isDefault()) {
							model.deleteBefore();
						} else {
							model.deleteRange(model.getSelectionRange());
						}
						model.setDeafualtSelectionRange();
					} else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
						if (model.getSelectionRange().isDefault()) {
							model.deleteAfter();
						} else {
							model.deleteRange(model.getSelectionRange());
						}
						model.setDeafualtSelectionRange();
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// ignore
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// ignore
			}
		});
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.BLACK);

		Location paintLocation = new Location(TextEditor.MARGIN_LEFT_RIGHT, TextEditor.MARGIN_TOP_BOTTOM);
		LineIterator it = model.allLines();
		FontMetrics fontMetrics = g.getFontMetrics();
		while (it.hasNext()) {
			String currLine = it.next();
			paintLocation.setX(TextEditor.MARGIN_LEFT_RIGHT);

			g.drawString(currLine, paintLocation.getX(), paintLocation.getY());
			paintLocation.setY(paintLocation.getY() + fontMetrics.getHeight());
		}

		paintLocation = model.getCursorLocation();
		String currLine = model.getLines().get(paintLocation.getY());

		paintLocation.setX(g.getFontMetrics().stringWidth(currLine.substring(0, paintLocation.getX()))
				+ TextEditor.MARGIN_LEFT_RIGHT);
		paintLocation
				.setY((paintLocation.getY() - 1) * g.getFontMetrics().getHeight() + TextEditor.MARGIN_TOP_BOTTOM + 3);

		g.drawLine(paintLocation.getX(), paintLocation.getY(), paintLocation.getX(),
				paintLocation.getY() + g.getFontMetrics().getHeight() - 3);

	}

	@Override
	public void updateCursorLocation() {
		this.paint(this.getGraphics());
	}

	@Override
	public void updateText() {
		this.paint(this.getGraphics());
	}
}
