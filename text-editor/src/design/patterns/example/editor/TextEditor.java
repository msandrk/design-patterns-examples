package design.patterns.example.editor;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import design.patterns.example.editor.model.Location;
import design.patterns.example.editor.model.TextEditorModel;
import design.patterns.example.editor.model.iterator.LineIterator;

public class TextEditor extends JFrame {
	private static final long serialVersionUID = 1L;
	public static final int MARGIN_TOP_BOTTOM = 45;
	public static final int MARGIN_LEFT_RIGHT = 5;
	private TextEditorModel model;
	
	
	public TextEditor(TextEditorModel model) {
		this.model = model;
		model.setCursorLocation(new Location(TextEditor.MARGIN_LEFT_RIGHT, TextEditor.MARGIN_TOP_BOTTOM));
		this.setSize(600, 400);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.BLACK);
		
		Location cursor = new Location(TextEditor.MARGIN_LEFT_RIGHT, TextEditor.MARGIN_TOP_BOTTOM);	
		LineIterator it = model.allLines();
		
		while(it.hasNext()) {
			String currLine = it.next();
			cursor.setX(TextEditor.MARGIN_LEFT_RIGHT);
			g.drawString(currLine, cursor.getX(), cursor.getY());
			cursor.setX(cursor.getX() + g.getFontMetrics().charsWidth(currLine.toCharArray(), 0, currLine.length()) + 1);
			cursor.setY(cursor.getY() + g.getFontMetrics().getHeight());
		}
		
		cursor.setY(cursor.getY() - 2 * g.getFontMetrics().getHeight() + 3);
		g.drawLine(cursor.getX(), cursor.getY(), cursor.getX(), cursor.getY() + g.getFontMetrics().getHeight() - 3);
		
		model.setCursorLocation(cursor);
	}
	
}

