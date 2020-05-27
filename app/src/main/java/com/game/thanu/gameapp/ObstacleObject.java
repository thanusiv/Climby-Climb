package com.game.thanu.gameapp;

import android.graphics.Rect;

interface ObstacleObject {

    Rect getRectangle();

    void incrementY(float y);

    boolean playerCollide(Player player);

    void relocate(int yDistance);

    AnimationManager getAnimationManager();
}
