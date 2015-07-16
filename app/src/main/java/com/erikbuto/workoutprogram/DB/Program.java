package com.erikbuto.workoutprogram.DB;

/**
 * Created by Utilisateur on 26/06/2015.
 */
public class Program {

    private String name;
    private long id;

    public Program(long id, String name) {

        this.id = id;
        this.name = name;
    }

    public Program(String name) {
        this.name = name;
    }

    public Program() {
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
}
