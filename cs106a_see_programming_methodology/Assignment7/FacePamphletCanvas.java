/*
 * File: FacePamphletCanvas.java
 * -----------------------------
 * This class represents the canvas on which the profiles in the social
 * network are displayed.  NOTE: This class does NOT need to update the
 * display when the window is resized.
 */


import acm.graphics.*;

import java.awt.*;
import java.util.*;

public class FacePamphletCanvas extends GCanvas 
					implements FacePamphletConstants {
	
	/** 
	 * Constructor
	 * This method takes care of any initialization needed for 
	 * the display
	 */
	public FacePamphletCanvas() {
		// You fill this in
	}

	
	/** 
	 * This method displays a message string near the bottom of the 
	 * canvas.  Every time this method is called, the previously 
	 * displayed message (if any) is replaced by the new message text 
	 * passed in.
	 */
	public void showMessage(String msg) {
		// Remove previous Message
		if (message != null) {
			remove(message);
		}
		
		// Add message label
		message = new GLabel(msg);
		double messageX, messageY;
		messageX = (getWidth() / 2) - (message.getWidth() / 2);
		messageY = getHeight() - BOTTOM_MESSAGE_MARGIN;
		message.setLocation(messageX, messageY);
		message.setFont(MESSAGE_FONT);
		add(message);
	}
	
	
	/** 
	 * This method displays the given profile on the canvas.  The 
	 * canvas is first cleared of all existing items (including 
	 * messages displayed near the bottom of the screen) and then the 
	 * given profile is displayed.  The profile display includes the 
	 * name of the user from the profile, the corresponding image 
	 * (or an indication that an image does not exist), the status of
	 * the user, and a list of the user's friends in the social network.
	 */
	public void displayProfile(FacePamphletProfile profile) {
		removeAll();
		if (profile != null){
			displayName(profile.getName());
			displayImage(profile.getImage());
			displayFriends(profile.getFriends());
			displayStatus(profile.getStatus(), profile.getName());
			showMessage("Displaying " + profile.getName());
		}
	}

	private void displayStatus(String status, String name) {
		// previous baseline is top of image
		baseline = baseline + IMAGE_HEIGHT + STATUS_MARGIN;
		GLabel label;
		if (status.equals("")) {
			label = new GLabel("No current status");
		} else {
			label = new GLabel(name + " is " + status);
		}
		label.setFont(PROFILE_STATUS_FONT);
		label.setLocation(LEFT_MARGIN, baseline);
		add(label);
	}

	private void displayFriends(Iterator<String> friends) {
		double friendsX = getWidth() / 2;
		// baseline is top of image
		double friendsY = baseline;
		
		GLabel label;
		label = new GLabel("Friends:");
		label.setFont(PROFILE_FRIEND_LABEL_FONT);
		label.setLocation(friendsX, friendsY);
		add(label);
		
		while (friends.hasNext()) {
			friendsY = friendsY + label.getHeight();
			label = new GLabel(friends.next());
			label.setFont(PROFILE_FRIEND_FONT);
			label.setLocation(friendsX, friendsY);
			add(label);
		}
	}

	private void displayImage(GImage image) {
		// Name label baseline plus image margin
		baseline = baseline + IMAGE_MARGIN;
		
		// if there is no profile image
		if (image == null) {
			add(new GRect(LEFT_MARGIN, baseline,
					IMAGE_WIDTH, IMAGE_HEIGHT));
			GLabel label = new GLabel("No Image");
			label.setFont(PROFILE_IMAGE_FONT);
			double labelX, labelY;
			labelX = LEFT_MARGIN + (IMAGE_WIDTH / 2) -
						(label.getWidth() / 2);
			labelY = baseline + (IMAGE_HEIGHT / 2) +
						(label.getAscent() / 2);
			label.setLocation(labelX, labelY);
			add(label);
		} else {
			image.scale(IMAGE_WIDTH /image.getWidth(),
					IMAGE_HEIGHT / image.getHeight());
			image.setLocation(LEFT_MARGIN, baseline);
			add(image);
		}
	}


	private void displayName(String name) {
		GLabel nameLabel;
		nameLabel = new GLabel(name);
		nameLabel.setColor(Color.BLUE);
		nameLabel.setFont(PROFILE_NAME_FONT);
		baseline = TOP_MARGIN + nameLabel.getAscent();
		nameLabel.setLocation(LEFT_MARGIN, baseline);
		
		add(nameLabel);
	}

	/* Private instance variables */
	private GLabel message;
	private double baseline;
}
