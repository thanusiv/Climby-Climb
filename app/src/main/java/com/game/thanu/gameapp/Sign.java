package com.game.thanu.gameapp;

import android.graphics.Rect;

class Sign implements BackgroundObject {

    private Rect rectangle;
    private String type;

    Sign(int xDistance, int top, String type) {
        rectangle = new Rect(0, 0, Constants.SCREEN_WIDTH / 3, Constants.SCREEN_HEIGHT / 9);
        int y = Constants.SCREEN_HEIGHT - top;
        rectangle.set(xDistance, Constants.SCREEN_HEIGHT - y - rectangle.height(), xDistance + rectangle.width(), Constants.SCREEN_HEIGHT - y);
        this.type = type;
    }

    @Override
    public Rect getRectangle() {
        return rectangle;
    }

    @Override
    public void incrementY(float y) {
        rectangle.top += y;
        rectangle.bottom += y;
    }

    @Override
    public void decrementY(float y) {
        rectangle.top -= y;
        rectangle.bottom -= y;
    }

    void relocate(int yDistance) {
        rectangle.set(rectangle.left, yDistance - rectangle.height(), rectangle.left + rectangle.width(), yDistance);
    }

    String getType(){
        return type;
    }

}
