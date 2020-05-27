package com.game.thanu.gameapp;

import android.graphics.Rect;

class Ground implements BackgroundObject {

    private Rect rectangle;

    Ground(int distance) {
        rectangle = new Rect(0,0,Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT/10);
        rectangle.set(0, Constants.SCREEN_HEIGHT - distance - rectangle.height(), Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT - distance);
    }

    public Rect getRectangle() {
        return rectangle;
    }

    public void incrementY(float y) {
        rectangle.top += y;
        rectangle.bottom += y;
    }

    public void decrementY(float y) {
        rectangle.top -= y;
        rectangle.bottom -= y;
    }

}
