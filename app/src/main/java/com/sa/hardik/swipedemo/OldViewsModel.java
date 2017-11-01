package com.sa.hardik.swipedemo;

/**
 * Created by hardik.chauhan on 01/11/17.
 */

public class OldViewsModel {

    private int id;
    private String text;
    private boolean isFilled;

    OldViewsModel(int id, String text, boolean isFilled) {
        this.id = id;
        this.text = text;
        this.isFilled = isFilled;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public boolean isFilled() {
        return isFilled;
    }
}
