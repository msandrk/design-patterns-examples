package design.patterns.example.editor;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import design.patterns.example.editor.model.Location;
import design.patterns.example.editor.model.TextEditorModel;

public class TextEditor extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final int MARGIN_TOP_BOTTOM = 45;
	private static final int MARGIN_LEFT_RIGHT = 5;
	private TextEditorModel model;
	
	
	public TextEditor(TextEditorModel model) {
		this.model = model;
		this.setSize(600, 400);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.BLACK);
		Location cursor = new Location(TextEditor.MARGIN_LEFT_RIGHT, TextEditor.MARGIN_TOP_BOTTOM);
		
		for(String s : model.getLines()) {
			cursor.setX(TextEditor.MARGIN_LEFT_RIGHT);
			g.drawString(s, cursor.getX(), cursor.getY());
			cursor.setX(cursor.getX() + g.getFontMetrics().charsWidth(s.toCharArray(), 0, s.length()) + 1);
			cursor.setY(cursor.getY() + g.getFontMetrics().getHeight());
		}
		
		cursor.setY(cursor.getY() - 2 * g.getFontMetrics().getHeight() + 3);
		g.drawLine(cursor.getX(), cursor.getY(), cursor.getX(), cursor.getY() + g.getFontMetrics().getHeight() - 3);
		model.setCursorLocation(cursor);

	}
	
}

