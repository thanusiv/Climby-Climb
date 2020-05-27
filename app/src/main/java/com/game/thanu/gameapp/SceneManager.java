package com.game.thanu.gameapp;

import android.app.Activity;
import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;

class SceneManager {

    private ArrayList<Scene> scenes;
    static int ACTIVE_SCENE;
    static boolean startGameplay;
    private Activity activity;

    SceneManager(Activity activity) {
        scenes = new ArrayList<>();
        this.activity = activity;
        ACTIVE_SCENE = 0;
        scenes.add(new MenuScene());
        startGameplay = false;
    }

    public void update() {
        scenes.get(ACTIVE_SCENE).update();
    }

    public void draw(Canvas canvas) {
        scenes.get(ACTIVE_SCENE).draw(canvas);
    }

    void receiveTouch(MotionEvent event) {
        if (!startGameplay && ACTIVE_SCENE == 1) {
            startGameplay = true;
            scenes.add(new GameplayScene(activity));
        }
        scenes.get(ACTIVE_SCENE).receiveTouch(event);
    }



}
