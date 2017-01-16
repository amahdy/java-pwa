package org.vaadin.java_pwa.backend;

import java.time.LocalDate;

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;

public class Workout {

	private Long id;

	private LocalDate date;
	private Resource sport;
	private Integer duration;
	private Integer calories;

	public Workout() {
	}

	public Workout(LocalDate date, Sport sport, Integer duration, Integer calories) {
		this.date = date;
		setSport(sport);
		this.duration = duration;
		this.calories = calories;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Resource getSport() {
		return sport;
	}

	public void setSport(Sport sport) {
		this.sport = new ThemeResource(sport.getFilename());
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getCalories() {
		return calories;
	}

	public void setCalories(Integer calories) {
		this.calories = calories;
	}

	@Override
	public String toString() {
		return "{ Date: " + getDate().toString() + ", Sport: " + getSport() + ", Duration: " + getDuration()
				+ ", Calories: " + getCalories() + " }";
	}
}