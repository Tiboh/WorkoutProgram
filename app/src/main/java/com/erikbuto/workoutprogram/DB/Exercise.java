package com.erikbuto.workoutprogram.DB;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Utilisateur on 26/06/2015.
 */
public class Exercise implements Serializable {

    public static final String MODE_SIMPLE = "simple";
    public static final String MODE_ADVANCED = "advanced";

    private long id;
    private String name;
    private String imageUrl;
    private long programId;
    private int position;
    private String mode; // simple or advanced
    private String description;

    public Exercise(String name, String imageUrl, long programId, int position, String mode, String description) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.programId = programId;
        this.position = position;
        this.mode = mode;
        this.description = description;
    }

    public Exercise(long id, String name, String imageUrl, long programId, int position, String mode, String description) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.programId = programId;
        this.position = position;
        this.mode = mode;
        this.description = description;
    }

    public Exercise() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getProgramId() {
        return programId;
    }

    public void setProgramId(long programId) {
        this.programId = programId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public static class ExerciseComparator implements Comparator<Exercise> {


        public int compare(Exercise e1, Exercise e2) {
            return (e1.getPosition()<e2.getPosition() ? -1 : (e1.getPosition()==e2.getPosition() ? 0 : 1));
        }
    }
}
