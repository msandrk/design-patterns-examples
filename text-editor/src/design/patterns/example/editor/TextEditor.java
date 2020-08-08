package design.patterns.example.editor;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import design.patterns.example.editor.model.Location;
import design.patterns.example.editor.model.TextEditorModel;

public class TextEditor extends JFrame {
	private TextEditorModel model;
	
	public TextEditor(TextEditorModel model) {
		this.model = model;
		this.setSize(1024, 786);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.BLACK);
		Location cursor = model.getCursorLocation();
		for(String s : this.model.getLines()) {
			cursor.setX(1);
			g.drawString(s, cursor.getX(), cursor.getY());
//			model.setCursorLocation(new Location(model.getCursorLocation().getX() + s.length(), model.getCursorLocation().getY() + g.getFontMetrics().getHeight()));
			cursor.setX(cursor.getX() + s.length());
			cursor.setY(cursor.getY() + g.getFontMetrics().getHeight());
		}
		
		g.drawLine(cursor.getX(), cursor.getY() - g.getFontMetrics().getHeight(), cursor.getX(), cursor.getY());
		model.setCursorLocation(cursor);
	}
}

