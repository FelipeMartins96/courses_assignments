/*
 * File: NameSurferGraph.java
 * ---------------------------
 * This class represents the canvas on which the graph of
 * names is drawn. This class is responsible for updating
 * (redrawing) the graphs whenever the list of entries changes or the window is resized.
 */

import acm.graphics.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;

public class NameSurferGraph extends GCanvas
	implements NameSurferConstants, ComponentListener {

	/**
	* Creates a new NameSurferGraph object that displays the data.
	*/
	public NameSurferGraph() {
		addComponentListener(this);

	    entries = new ArrayList<NameSurferEntry>();
		update();
	}
	
	/**
	* Clears the list of name surfer entries stored inside this class.
	*/
	public void clear() {
		entries.clear();
		update();
	}
	
	/* Method: addEntry(entry) */
	/**
	* Adds a new NameSurferEntry to the list of entries on the display.
	* Note that this method does not actually draw the graph, but
	* simply stores the entry; the graph is drawn by calling update.
	*/
	public void addEntry(NameSurferEntry entry) {
		if (entry != null) {
			entries.add(entry);
			update();			
		}
	}
	
	/**
	* Updates the display image by deleting all the graphical objects
	* from the canvas and then reassembling the display according to
	* the list of entries. Your application must call update after
	* calling either clear or addEntry; update is also called whenever
	* the size of the canvas changes.
	*/
	public void update() {
		// Clear canvas
		removeAll();
		
		addBackgroundHorizontalLines();
		addBackgroundVerticalLines();
		
		for (int i = 0; i < entries.size(); i++) {
			addEntryToCanvas(entries.get(i), getColor(i % 4));
		}
	}
	

	
	private void addEntryToCanvas(NameSurferEntry entry, Color color) {
		double x, y, previousX = 0, previousY = 0;
		String labelText;
		int columnWidth = getWidth() / NDECADES;
		
		for (int i = 0; i < NDECADES; i++) {
			x = (i * columnWidth);
			if (entry.getRank(i) == 0) {
				y = getHeight() - GRAPH_MARGIN_SIZE;
				labelText = entry.getName() + " *";
			} else {
				double rankSpacing;
				rankSpacing = ((double)getHeight() - (GRAPH_MARGIN_SIZE * 2)) / MAX_RANK;
				y =  (rankSpacing * (entry.getRank(i) - 1)) + GRAPH_MARGIN_SIZE;
				labelText = entry.getName() + " " + entry.getRank(i);
			}
			GLabel label = new GLabel(labelText, x, y);
			label.setColor(color);
			add(label);
			
			if (i > 0) {
				GLine line = new GLine(previousX, previousY, x, y);
				line.setColor(color);
				add(line);
			}
			
			previousX = x;
			previousY = y;
		}
		
	}
	

	private Color getColor(int i) {
		Color color;
		switch (i) {
			case 0:
				color =  Color.BLACK;
				break;
			case 1:
				color = Color.RED;
				break;
			case 2:
				color = Color.BLUE;
				break;
			case 3:
				color = Color.MAGENTA;
				break;
			default: 
				color = Color.BLACK;
				break;
		}
		
		return color;
	}

	private void addBackgroundVerticalLines() {
		int columnWidth = getWidth() / NDECADES;
		
		for (int i = 0; i < NDECADES; i++) {
			// Add lines
			double x = columnWidth * i;
			add(new GLine(x, 0, x, getHeight()));
			
			// Add lines labels
			int decade = 1900 + (i * 10);
			add(new GLabel(Integer.toString(decade), x, getHeight()));
		}
	}

	private void addBackgroundHorizontalLines() {
		// Top margin
		double y = GRAPH_MARGIN_SIZE;
		add(new GLine(0, y, getWidth(), y));
		
		// Bottom Margin
		y = getHeight() - GRAPH_MARGIN_SIZE;
		add(new GLine(0, y, getWidth(), y));
	}

	/* Implementation of the ComponentListener interface */
	public void componentHidden(ComponentEvent e) { }
	public void componentMoved(ComponentEvent e) { }
	public void componentResized(ComponentEvent e) { update(); }
	public void componentShown(ComponentEvent e) { }
	
	/* Private instance variables */
	private ArrayList<NameSurferEntry> entries;
}
