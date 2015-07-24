package com.erikbuto.workoutprogram.DB;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Utilisateur on 26/06/2015.
 */
public class Exercise implements Serializable {

    private long id;
    private String name;
    private long programId;
    private int position;
    private String description;

    public Exercise(String name, long programId, int position, String description) {
        this.name = name;
        this.programId = programId;
        this.position = position;
        this.description = description;
    }

    public Exercise(long id, String name, long programId, int position, String description) {
        this.id = id;
        this.name = name;
        this.programId = programId;
        this.position = position;
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
