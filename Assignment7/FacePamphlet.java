/* 
 * File: FacePamphlet.java
 * -----------------------
 * When it is finished, this program will implement a basic social network
 * management system.
 */

import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.event.*;
import java.util.HashMap;

import javax.swing.*;

public class FacePamphlet extends ConsoleProgram 
					implements FacePamphletConstants {

	/**
	 * This method has the responsibility for initializing the 
	 * interactors in the application, and taking care of any other 
	 * initialization that needs to be performed.
	 */
	public void init() {
		// Nort Border Iteractors
		// Name Text Field
		add(new JLabel("Name"), NORTH);
		nameTF = new JTextField(TEXT_FIELD_SIZE);
		add(nameTF, NORTH);
		
		//Add, Felete and Lookup Button
		add(new JButton("Add"), NORTH);
		add(new JButton("Delete"), NORTH);
		add(new JButton("Lookup"), NORTH);
		
		// West Border Iteractors
		// Change Status
		statusTF = new JTextField(TEXT_FIELD_SIZE);
		add(statusTF, WEST);
		statusTF.addActionListener(this);
		add(new JButton("Change Status"), WEST);
		
		// Vertical Space
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);
		
		// Change Picture
		pictureTF = new JTextField(TEXT_FIELD_SIZE);
		add(pictureTF, WEST);
		pictureTF.addActionListener(this);
		add(new JButton("Change Picture"), WEST);
		
		// Vertical Space
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);
		
		// Add Friend
		friendTF = new JTextField(TEXT_FIELD_SIZE);
		add(friendTF, WEST);
		friendTF.addActionListener(this);
		add(new JButton("Add Friend"), WEST);
		
		addActionListeners();
		
		// Create Database
		database = new FacePamphletDatabase();
    }
    
  
    /**
     * This class is responsible for detecting when the buttons are
     * clicked or interactors are used, so you will have to add code
     * to respond to these actions.
     */
    public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Add")) {
			String name = nameTF.getText();
			
			// If name text field is not empty
			if (!name.equals("")) {
				FacePamphletProfile profile;
				
				// If profile does not exists in database
				if (!database.containsProfile(name)) {
					profile = new FacePamphletProfile(name);
					database.addProfile(profile);
					println("Add new profile: " + profile);
				} else {
					profile = database.getProfile(name);
					println("Add: profile for " + name
							+ " already exists: " + profile);
				}
			}
		}
		
		if (e.getActionCommand().equals("Delete")) {
			String name = nameTF.getText();
			
			// If name text field is not empty
			if (!name.equals("")) {
				
				// If profile exists in database
				if (database.containsProfile(name)) {
					database.deleteProfile(name);
					println("Delete: profile of " + name 
							+ " deleted");
				} else {
					println("Delete: profile with name " + name
							+ " does not exist");
				}
			}
		}
		
		if (e.getActionCommand().equals("Lookup")) {
			String name = nameTF.getText();
			
			// If name text field is not empty
			if (!name.equals("")) {
				
				// If profile exists in database
				if (database.containsProfile(name)) {
					FacePamphletProfile profile;
					profile = database.getProfile(name);
					println("Lookup: " + profile);
				} else {
					println("Lookup: profile with name " + name
							+ " does not exist");
				}
			}
		}
		
		if (e.getSource() == statusTF 
				|| e.getActionCommand().equals("Change Status")) {
			if (!statusTF.getText().equals("")) {
				println("Change Status: " + statusTF.getText());
			}
		}
		
		if (e.getSource() == pictureTF 
				|| e.getActionCommand().equals("Change Picture")) {
			if (!pictureTF.getText().equals("")) {
				println("Change Picture: " + pictureTF.getText());
			}
		}
		
		if (e.getSource() == friendTF 
				|| e.getActionCommand().equals("Add Friend")) {
			if (!friendTF.getText().equals("")) {
				println("Add Friend: " + friendTF.getText());
			}
		}
	}
    
	/* Private instance variables */
    private JTextField nameTF, statusTF, pictureTF, friendTF;
    private FacePamphletDatabase database;

}
