package com.erikbuto.workoutprogram.DB;

import java.util.Comparator;

/**
 * Created by Utilisateur on 26/06/2015.
 */
public class Set {

    private long id;
    private int nbRep;
    private int weight;
    private int restTimeMinute;
    private int restTimeSecond;
    private long exerciseId;
    private int position;

    public Set() {
    }

    public Set(int nbRep, int weight, int restTimeMinute, int restTimeSecond, long exerciseId, int position) {
        this.nbRep = nbRep;
        this.weight = weight;
        this.restTimeMinute = restTimeMinute;
        this.restTimeSecond = restTimeSecond;
        this.exerciseId = exerciseId;
        this.position = position;
    }


    public Set(long id, int nbRep, int weight, int restTimeMinute, int restTimeSecond, long exerciseId, int position) {

        this.id = id;
        this.nbRep = nbRep;
        this.weight = weight;
        this.restTimeMinute = restTimeMinute;
        this.restTimeSecond = restTimeSecond;
        this.exerciseId = exerciseId;
        this.position = position;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNbRep() {
        return nbRep;
    }

    public void setNbRep(int nbRep) {
        this.nbRep = nbRep;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getRestTimeMinute() {
        return restTimeMinute;
    }

    public void setRestTimeMinute(int restTimeMinute) {
        this.restTimeMinute = restTimeMinute;
    }

    public int getRestTimeSecond() {
        return restTimeSecond;
    }

    public void setRestTimeSecond(int restTimeSecond) {
        this.restTimeSecond = restTimeSecond;
    }

    public long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public static class SetComparator implements Comparator<Set> {

        public int compare(Set s1, Set s2) {
            return (s1.getPosition()<s2.getPosition() ? -1 : (s1.getPosition()==s2.getPosition() ? 0 : 1));
        }
    }
}
