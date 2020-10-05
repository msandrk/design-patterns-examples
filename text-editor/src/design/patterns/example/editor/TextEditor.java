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
		this.initGUI();
		this.initKeyListeners();
	}

	private void initGUI() {
		this.setTitle("GoF Text Editor");
		this.setSize(600, 400);
		this.setLocationRelativeTo(null);
		this.getContentPane().setBackground(Color.WHITE);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	private void initKeyListeners() {
		this.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				// if key is alphanumeric (ASCII signs), space or enter, call insert
				if(e.getKeyChar() >= 33 && e.getKeyChar() <= 126 
						|| e.getKeyCode() == KeyEvent.VK_SPACE
						|| e.getKeyCode() == KeyEvent.VK_ENTER) {
					
					if(!model.getSelectionRange().isDefault()) {
						model.deleteRange(model.getSelectionRange());
					}

					model.setDefaultSelectionRange();
					model.insert(e.getKeyChar());
				}
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
							Location prevCursorLoc = model.getCursorLocation();
							model.moveCursorUp();
							Location cursorLocation = model.getCursorLocation();
							if (!prevCursorLoc.equals(cursorLocation) && rangeStart.equals(cursorLocation)
									&& rangeEnd.compareTo(rangeStart) == 1) {
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
							Location prevCursorLoc = model.getCursorLocation();
							model.moveCursorDown();
							Location cursorLocation = model.getCursorLocation();
							if (!prevCursorLoc.equals(cursorLocation) && rangeEnd.equals(cursorLocation)
									&& rangeEnd.compareTo(rangeStart) == 1) {
								range = new LocationRange();
							} else if (rangeEnd.compareTo(cursorLocation) > 0) {
								range.setStart(cursorLocation);
							} else if (rangeEnd.compareTo(cursorLocation) < 0) {
								range.setEnd(cursorLocation);
							}
						}
					}
					if (!range.isDefault() && range.getStart().equals(range.getEnd())) {
						range = new LocationRange();
					}
					model.setSelectionRange(range);
					paint(getGraphics());
				} else {
					if (e.getKeyCode() == KeyEvent.VK_LEFT) {
						model.setDefaultSelectionRange();
						model.moveCursorLeft();
					} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
						model.setDefaultSelectionRange();
						model.moveCursorRight();
					} else if (e.getKeyCode() == KeyEvent.VK_UP) {
						model.setDefaultSelectionRange();
						model.moveCursorUp();
					} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
						model.setDefaultSelectionRange();
						model.moveCursorDown();
					} else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
						if (model.getSelectionRange().isDefault()) {
							model.deleteBefore();
						} else {
							model.deleteRange(model.getSelectionRange());
						}
						model.setDefaultSelectionRange();
						paint(getGraphics());
					} else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
						if (model.getSelectionRange().isDefault()) {
							model.deleteAfter();
						} else {
							model.deleteRange(model.getSelectionRange());
						}
						model.setDefaultSelectionRange();
						paint(getGraphics());
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

		FontMetrics fontMetrics = g.getFontMetrics();
		Location paintLoc = new Location(TextEditor.MARGIN_LEFT_RIGHT, TextEditor.MARGIN_TOP_BOTTOM);
		LocationRange selectRange = model.getSelectionRange();

		LineIterator it = model.allLines();

		for (int i = 0; it.hasNext(); i++) {
			String currLine = it.next();
			paintLoc.setX(TextEditor.MARGIN_LEFT_RIGHT);
			
			// if there is an active text selection range
			if (!selectRange.isDefault() && i >= selectRange.getStart().getY()
					&& i <= selectRange.getEnd().getY()) {
				
				int startX = TextEditor.MARGIN_LEFT_RIGHT - 1;	// -1 for prettier print
				int endX = TextEditor.MARGIN_LEFT_RIGHT + fontMetrics.stringWidth(currLine);
				int startY = paintLoc.getY();

				Location selectionStart = selectRange.getStart();
				Location selectionEnd = selectRange.getEnd();
				
				// if first row of selected text, adjust starting x-coordinate and ending x-coordinate
				if (i == selectionStart.getY()) {
					int endIndex = selectionStart.getX() < currLine.length() ? selectionStart.getX()
							: currLine.length();
					startX += fontMetrics.stringWidth(currLine.substring(0, endIndex));
				}
				
				// if last row of selected text, adjust ending x-coordinate
				if (i == selectionEnd.getY()) {
					endX = fontMetrics.stringWidth(currLine.substring(0, selectionEnd.getX()));
					endX += TextEditor.MARGIN_LEFT_RIGHT;
				}
				
				// add 30 to all rgb components of light grey to make it brighter
				int rgbValue = Color.LIGHT_GRAY.getRed() + 30;
				Color selectionColor = new Color(rgbValue, rgbValue, rgbValue);
				
				g.setColor(selectionColor);
				g.fillRect(startX, startY - fontMetrics.getHeight() + 3, endX - startX, fontMetrics.getHeight());
				g.setColor(Color.BLACK);
			}
			
			g.drawString(currLine, paintLoc.getX(), paintLoc.getY());
			paintLoc.setY(paintLoc.getY() + fontMetrics.getHeight());
		}
		// draw cursor
		paintLoc = model.getCursorLocation();
		String line = model.getLines().get(paintLoc.getY());

		paintLoc.setX(fontMetrics.stringWidth(line.substring(0, paintLoc.getX()))
				+ TextEditor.MARGIN_LEFT_RIGHT);
		paintLoc.setY((paintLoc.getY() - 1) * fontMetrics.getHeight() + TextEditor.MARGIN_TOP_BOTTOM
				+ fontMetrics.getDescent());

		g.drawLine(paintLoc.getX(), paintLoc.getY(), paintLoc.getX(), 
				paintLoc.getY() + fontMetrics.getHeight() - 2);
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
