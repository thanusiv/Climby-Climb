package com.game.thanu.gameapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

class GroundsHandler implements BackgroundObjectHandler {

    private AnimationManager animManagerGround;
    static ArrayList<Ground> grounds;
    static boolean removedGroundMenu;

    GroundsHandler(){
        Bitmap ground = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.ground);
        Animation groundPic = new Animation(new Bitmap[]{ground}, 100f);
        animManagerGround = new AnimationManager(new Animation[]{groundPic});
        grounds = new ArrayList<>();
        removedGroundMenu = false;
    }

    @Override
    public void addInitialObjects() {
        grounds.add(new Ground(Constants.GROUND_SPACE_DIFFERENCE));
        grounds.add(new Ground(Constants.GROUND_SPACE_DIFFERENCE/2));
        grounds.add(new Ground(0));
    }

    @Override
    public void moveObjectsDown() {
        for (int i = 0; i < grounds.size(); i++)
            grounds.get(i).incrementY(Constants.OBJECT_CLIMB_SPEED);
    }

    @Override
    public void adjustObjectsDown() {
        for (int i = 0; i < grounds.size(); i++)
            grounds.get(i).incrementY(1);
    }

    @Override
    public void adjustObjectsUp() {
        for (int i = 0; i < grounds.size(); i++)
            grounds.get(i).decrementY(1);
    }

    @Override
    public void addNewObjectAndRemoveOldObject() {
        grounds.add(0, new Ground(Constants.GROUND_SPACE_DIFFERENCE));
        grounds.remove(grounds.size() - 1);
        Log.d("Grounds Size", "" +grounds.size());
    }

    @Override
    public void playObjectsAnimation() {
        animManagerGround.playAnim(0);
        animManagerGround.update();
    }

    @Override
    public void drawObjects(Canvas canvas) {
        for (Ground ground : grounds)
            animManagerGround.draw(canvas, ground.getRectangle());
    }

    @Override
    public boolean collideWithObjects(Player player) {
        return Rect.intersects(grounds.get(grounds.size() - 1).getRectangle(), player.getRectangle());
    }

    @Override
    public void menuScreenUpdate() {
        removedGroundMenu = false;
        moveObjectsDown();
        if (grounds.get(grounds.size() - 2).getRectangle().bottom > Constants.SCREEN_HEIGHT) {
            addNewObjectAndRemoveOldObject();
            removedGroundMenu = true;
        }
    }
}
