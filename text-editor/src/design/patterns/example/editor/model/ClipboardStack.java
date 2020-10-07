package design.patterns.example.editor.model;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import design.patterns.example.editor.model.observer.ClipboardObserver;

public class ClipboardStack {
	private Deque<String> stack;
	private List<ClipboardObserver> clipboardObservers;
	
	public ClipboardStack() {
		stack = new LinkedList<String>();
		clipboardObservers = new LinkedList<ClipboardObserver>();
	}
	
	public void push(String s) {
		stack.addFirst(s);
		notifyClipboardObservers();
	}
	
	public String pop() {
		String result = stack.removeFirst();
		notifyClipboardObservers();
		return result;
	}
	
	public String peek() {
		return stack.peekFirst(); 
	}
	
	public void emptyStack() {
		stack.clear();
		notifyClipboardObservers();
	}
	
	public void attachClipboardObserver(ClipboardObserver o) {
		clipboardObservers.add(o);
	}
	
	public void dettachClipboardObserver(ClipboardObserver o) {
		clipboardObservers.remove(o);
	}
	
	public void notifyClipboardObservers() {
		for(ClipboardObserver o : clipboardObservers) {
			o.updateClipboard();
		}
	}
}
