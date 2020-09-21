package design.patterns.example.editor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import design.patterns.example.editor.model.Location;
import design.patterns.example.editor.model.TextEditorModel;
import design.patterns.example.editor.model.iterator.LineIterator;
import design.patterns.example.editor.model.observer.CursorObserver;

public class TextEditor extends JFrame implements CursorObserver {
	private static final long serialVersionUID = 1L;
	public static final int MARGIN_TOP_BOTTOM = 45;
	public static final int MARGIN_LEFT_RIGHT = 5;
	private TextEditorModel model;
	private boolean drawCursor;

	public TextEditor(TextEditorModel model) {
		this.model = model;
		model.setCursorLocation(model.getLines().get(model.getLines().size() - 1).length(), model.getLines().size()-1);
		model.attachCursorObserver(this);
		drawCursor = true;
		this.setTitle("GoF Text Editor");
		this.setSize(600, 400);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		this.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					model.moveCursorLeft();
				} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					model.moveCursorRight();
				} else if (e.getKeyCode() == KeyEvent.VK_UP) {
					model.moveCursorUp();
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					model.moveCursorDown();
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

		while (it.hasNext()) {
			String currLine = it.next();
			paintLocation.setX(TextEditor.MARGIN_LEFT_RIGHT);
			g.drawString(currLine, paintLocation.getX(), paintLocation.getY());
			paintLocation.setX(paintLocation.getX() + g.getFontMetrics().charsWidth(currLine.toCharArray(), 0, currLine.length()) + 1);
			paintLocation.setY(paintLocation.getY() + g.getFontMetrics().getHeight());
		}

		paintLocation.setY(paintLocation.getY() - 2 * g.getFontMetrics().getHeight() + 3);
		g.drawLine(paintLocation.getX(), paintLocation.getY(), paintLocation.getX(), paintLocation.getY() + g.getFontMetrics().getHeight() - 3);

	}

	@Override
	public void updateCursorLocation() {
		this.paint(this.getGraphics());
	}

}
