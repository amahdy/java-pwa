package org.vaadin.java_pwa.backend;

public enum Sport {
    RUNNING, SWIMMING, TENNIS;
	
	public String getFilename() {
		return "images/" + this.name().toLowerCase() + "-15.png";
	}
}