package com.game.thanu.gameapp;

import android.graphics.Canvas;
import android.view.MotionEvent;

interface Scene {

    void update();

    void draw(Canvas canvas);

    void receiveTouch(MotionEvent event);
}
