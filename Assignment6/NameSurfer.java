/*
 * File: NameSurfer.java
 * ---------------------
 * When it is finished, this program will implements the viewer for
 * the baby-name database described in the assignment handout.
 */

import acm.program.*;
import java.awt.event.*;
import javax.swing.*;

public class NameSurfer extends Program implements NameSurferConstants {

/* Method: init() */
/**
 * This method has the responsibility for reading in the data base
 * and initializing the interactors at the bottom of the window.
 */
	public void init() {
		// Create database
		db = new NameSurferDataBase(NAMES_DATA_FILE);
		
	    // Name Text Field
	    nameField = new JTextField(TEXT_FIELD_WIDTH);
	    add(new JLabel("Name"), SOUTH);
	    add(nameField, SOUTH);
	    nameField.addActionListener(this);
	    
	    // Graph Button
	    add(new JButton("Graph"), SOUTH);
	    
	    // Clear Button
	    add(new JButton("Clear"), SOUTH);
	    
	    // Add name surfer graph
	    graph = new NameSurferGraph();
	    add(graph);
	    
	    addActionListeners();
	}

/* Method: actionPerformed(e) */
/**
 * This class is responsible for detecting when the buttons are
 * clicked, so you will have to define a method to respond to
 * button actions.
 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Clear")) {
			graph.clear();
		}
		
		if (e.getSource() == nameField || e.getActionCommand().equals("Graph")) {
			graph.addEntry(db.findEntry(nameField.getText()));
		}
	}
	
	/* Private instance variables */
	private JTextField nameField;
	private NameSurferDataBase db;
	private NameSurferGraph graph;
}
