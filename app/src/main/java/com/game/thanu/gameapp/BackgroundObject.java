package com.game.thanu.gameapp;

import android.graphics.Rect;

interface BackgroundObject {

    Rect getRectangle();

    void incrementY(float y);

    void decrementY(float y);

}
