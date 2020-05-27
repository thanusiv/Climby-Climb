package com.game.thanu.gameapp;

import android.graphics.Rect;

class Spike implements ObstacleObject {

    private Rect rectangle;

    Spike(int xDistance, int yDistance) {
        rectangle = new Rect(0,0,Constants.SCREEN_WIDTH/6, Constants.SCREEN_HEIGHT/22);
        rectangle.set(xDistance, yDistance - rectangle.height(), xDistance + rectangle.width(), yDistance);
    }

    public Rect getRectangle() {
        return rectangle;
    }

    public void incrementY(float y) {
        rectangle.top += y;
        rectangle.bottom += y;
    }

    public boolean playerCollide(Player player) {

        if (Constants.SCREEN_WIDTH == 768) {
            if (player.getRectangle().right - 20 > rectangle.left && player.getRectangle().bottom - 5 > rectangle.top && player.getRectangle().left + 20 < rectangle.right)
                return true;
        }
        else if (Constants.SCREEN_WIDTH == 640) {
            if (player.getRectangle().right - 20 > rectangle.left && player.getRectangle().bottom - 3 > rectangle.top && player.getRectangle().left + 20 < rectangle.right)
                return true;
        }
        else if (Constants.SCREEN_WIDTH == 720 && Constants.SCREEN_HEIGHT == 1184) {
            if (player.getRectangle().right - 20 > rectangle.left && player.getRectangle().bottom - 7 > rectangle.top && player.getRectangle().left + 20 < rectangle.right)
                return true;
        }
        else if (Constants.SCREEN_WIDTH == 480 && Constants.SCREEN_HEIGHT >= 800) {
            if (player.getRectangle().right - 20 > rectangle.left && player.getRectangle().bottom - 7 > rectangle.top && player.getRectangle().left + 20 < rectangle.right)
                return true;
        }
        else if (Constants.SCREEN_WIDTH < 1000 && Constants.SCREEN_WIDTH > 500) {
            if (player.getRectangle().right - 20 > rectangle.left && player.getRectangle().bottom - 10 > rectangle.top && player.getRectangle().left + 20 < rectangle.right)
                return true;
        }
        else {
            if (player.getRectangle().right - 40 > rectangle.left && player.getRectangle().bottom - 10 > rectangle.top && player.getRectangle().left + 40 < rectangle.right)
                return true;
        }

        return false;
    }

    public void relocate(int yDistance){
        rectangle.set(rectangle.left, yDistance - rectangle.height(), rectangle.left + rectangle.width(), yDistance);
    }

    @Override
    public AnimationManager getAnimationManager() {
        return null;
    }
}
