package com.game.thanu.gameapp;

import android.graphics.Canvas;

interface ObstacleObjectHandler {

    void moveObjectsDown();

    void removeObjects();

    void adjustObjects();

    void playObjectsAnimation();

    void drawObjects(Canvas canvas);

    boolean collideWithObjects(Player player);

    void menuScreenUpdate();

}
