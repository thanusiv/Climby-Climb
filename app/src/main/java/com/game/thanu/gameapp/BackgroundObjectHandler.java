package com.game.thanu.gameapp;

import android.graphics.Canvas;

interface BackgroundObjectHandler {

    void addInitialObjects();

    void moveObjectsDown();

    void adjustObjectsDown();

    void adjustObjectsUp();

    void addNewObjectAndRemoveOldObject();

    void playObjectsAnimation();

    void drawObjects(Canvas canvas);

    boolean collideWithObjects(Player player);

    void menuScreenUpdate();
}
