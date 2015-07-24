package com.erikbuto.workoutprogram.DB;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Utilisateur on 24/07/2015.
 */
public class Image implements Serializable {

    private long id;
    private String url;
    private int position;
    private long exerciseId;

    public Image() {
    }

    public Image(long id, String url, int position, long exerciseId) {
        this.id = id;
        this.url = url;
        this.position = position;
        this.exerciseId = exerciseId;
    }

    public Image(String url, int position, long exerciseId) {
        this.url = url;
        this.position = position;
        this.exerciseId = exerciseId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public static class ImageComparator implements Comparator<Image> {

        public int compare(Image e1, Image e2) {
            return (e1.getPosition()<e2.getPosition() ? -1 : (e1.getPosition()==e2.getPosition() ? 0 : 1));
        }
    }
}
