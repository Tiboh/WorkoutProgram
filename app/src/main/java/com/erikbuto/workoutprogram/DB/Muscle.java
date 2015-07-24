package com.erikbuto.workoutprogram.DB;

/**
 * Created by Utilisateur on 24/07/2015.
 */
public class Muscle {

    public static final String MUSCLE_TYPE_PRIMARY = "primary";
    public static final String MUSCLE_TYPE_SECONDARY = "secondary";

    private long id;
    private String name;
    private String type;
    private long exerciseId;

    public Muscle(long id, String name, String type, long exerciseId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.exerciseId = exerciseId;
    }

    public Muscle(String name, String type, long exerciseId) {
        this.name = name;
        this.type = type;
        this.exerciseId = exerciseId;
    }

    public Muscle() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(long exerciseId) {
        this.exerciseId = exerciseId;
    }
}
