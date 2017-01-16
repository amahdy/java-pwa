package org.vaadin.java_pwa.backend;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;

import elemental.json.Json;
import elemental.json.JsonObject;

public class WorkoutDataReader {

	public List<Workout> run() {
		List<Workout> workouts = new ArrayList<>();
		Stream.of(38, 39, 40, 41).map(i -> "data/week-" + i + ".json").map(this::readFile).map(Json::parse)
				.map(json -> json.getArray("workouts")).forEach(jsonArray -> {
					for (int i = 0; i < jsonArray.length(); ++i) {
						JsonObject workout = jsonArray.get(i);
						LocalDate date = LocalDate.parse(workout.getString("date"),
								DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
						Sport sport = Sport.valueOf(workout.getString("sport").toUpperCase());
						Integer duration = (int) workout.getNumber("duration");
						Integer calories = (int) workout.getNumber("calories");
						workouts.add(new Workout(date, sport, duration, calories));
					}
				});
		return workouts;
	}

	private String readFile(String file) {
		try {
			InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(file);
			return IOUtils.toString(resourceAsStream);
		} catch (IOException e) {
			return "{}";
		}
	}

}