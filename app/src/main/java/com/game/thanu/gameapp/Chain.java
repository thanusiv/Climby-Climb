package com.game.thanu.gameapp;

import android.graphics.Rect;

import static com.game.thanu.gameapp.GroundsHandler.grounds;

class Chain implements BackgroundObject {

    private Rect rectangle;

    Chain(int index) {

        rectangle = new Rect(0, 0, Constants.SCREEN_WIDTH / 18, Constants.SCREEN_HEIGHT / 12);
        int y;

        if (index >= 3)
            y = (int) (grounds.get(0).getRectangle().top - grounds.get(0).getRectangle().height() / 1.5);
        else
            y = (int) (grounds.get(grounds.size() - index).getRectangle().top - grounds.get(grounds.size() - index).getRectangle().height() / 1.5);

        if (index % 2 == 0) {
            int left = (int) Math.ceil((double) (Constants.SCREEN_WIDTH) / 21.6);
            if (Constants.SCREEN_WIDTH == 768)
                rectangle.set(left, y, left - 4 + (int) Math.ceil((double) Constants.SCREEN_WIDTH / 21.75), y + (int) (Constants.SCREEN_HEIGHT / 17.8));
            else if (Constants.SCREEN_WIDTH == 640)
                rectangle.set(left, y, left - 1 + (int) Math.ceil((double) Constants.SCREEN_WIDTH / 21.75), y + (int) (Constants.SCREEN_HEIGHT / 17.8));
            else if (Constants.SCREEN_WIDTH == 720 && Constants.SCREEN_HEIGHT == 1184)
                rectangle.set(left, y, left - 2 + (int) Math.ceil((double) Constants.SCREEN_WIDTH / 21.75), y + (int) (Constants.SCREEN_HEIGHT / 17.8));
            else if (Constants.SCREEN_WIDTH == 480 && Constants.SCREEN_HEIGHT == 854)
                rectangle.set(left, y, left + (int) Math.ceil((double) Constants.SCREEN_WIDTH / 21.75), y + (int) (Constants.SCREEN_HEIGHT / 17.8));
            else if (Constants.SCREEN_WIDTH < 1000 && Constants.SCREEN_WIDTH > 500)
                rectangle.set(left, y, left + 1 + (int) Math.ceil((double) Constants.SCREEN_WIDTH / 21.75), y + (int) (Constants.SCREEN_HEIGHT / 17.8));
            else if (Constants.SCREEN_WIDTH > 1300)
                rectangle.set(left, y, left - 1 + (int) Math.floor((double) Constants.SCREEN_WIDTH / 21.75), y + (int) (Constants.SCREEN_HEIGHT / 17.8));
            else
                rectangle.set(left, y, left + (int) Math.floor((double) Constants.SCREEN_WIDTH / 21.75), y + (int) (Constants.SCREEN_HEIGHT / 17.8));
        } else {
            int right = Constants.SCREEN_WIDTH - (int) Math.ceil((double) (Constants.SCREEN_WIDTH) / 10.8);
            if (Constants.SCREEN_WIDTH == 768)
                rectangle.set(right, y, right - 4 + (int) Math.ceil((double) Constants.SCREEN_WIDTH / 21.75), y + (int) (Constants.SCREEN_HEIGHT / 17.8));
            else if (Constants.SCREEN_WIDTH == 640)
                rectangle.set(right, y, right - 1 + (int) Math.ceil((double) Constants.SCREEN_WIDTH / 21.75), y + (int) (Constants.SCREEN_HEIGHT / 17.8));
            else if (Constants.SCREEN_WIDTH == 720 && Constants.SCREEN_HEIGHT == 1184)
                rectangle.set(right, y, right - 2 + (int) Math.ceil((double) Constants.SCREEN_WIDTH / 21.75), y + (int) (Constants.SCREEN_HEIGHT / 17.8));
            else if (Constants.SCREEN_WIDTH == 480 && Constants.SCREEN_HEIGHT == 854)
                rectangle.set(right, y, right + (int) Math.ceil((double) Constants.SCREEN_WIDTH / 21.75), y + (int) (Constants.SCREEN_HEIGHT / 17.8));
            else if (Constants.SCREEN_WIDTH < 1000 && Constants.SCREEN_WIDTH > 500)
                rectangle.set(right, y, right + 1 + (int) Math.ceil((double) Constants.SCREEN_WIDTH / 21.75), y + (int) (Constants.SCREEN_HEIGHT / 17.8));
            else if (Constants.SCREEN_WIDTH > 1300)
                rectangle.set(right, y, right - 1 + (int) Math.floor((double) Constants.SCREEN_WIDTH / 21.75), y + (int) (Constants.SCREEN_HEIGHT / 17.8));
            else
                rectangle.set(right, y, right + (int) Math.floor((double) Constants.SCREEN_WIDTH / 21.75), y + (int) (Constants.SCREEN_HEIGHT / 17.8));
        }
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

}
