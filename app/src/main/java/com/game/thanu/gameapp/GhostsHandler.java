package com.game.thanu.gameapp;

import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;

import static com.game.thanu.gameapp.GroundsHandler.grounds;

class GhostsHandler implements ObstacleObjectHandler{

    private ArrayList<Ghost> ghosts;

    GhostsHandler(){
        ghosts = new ArrayList<>();
    }

    void addInitialObjects() {
        spawnGhost(grounds.get(grounds.size() - 3).getRectangle().top);
    }

    @Override
    public void moveObjectsDown() {
        for (int i = 0; i < ghosts.size(); i++)
            ghosts.get(i).incrementY(Constants.OBJECT_CLIMB_SPEED);
    }

    @Override
    public void removeObjects() {
        ghosts.remove(0);
    }

    @Override
    public void adjustObjects() {
        int x = 0;
        for (int i = 0; i < ObstacleManager.allObstaclesSpawned.size(); i++) {
            if (ObstacleManager.allObstaclesSpawned.get(i).equals("Ghost")) {
                ghosts.get(x).relocate(grounds.get(grounds.size() - (i + 1)).getRectangle().top);
                ++x;
            }
        }
        Log.d("Ghosts Spawned", "" + ghosts.size());

    }

    @Override
    public void playObjectsAnimation() {
        for (Ghost ghost : ghosts) {
            ghost.moveGhost();
            ghost.playAnimation();
        }
    }

    @Override
    public void drawObjects(Canvas canvas) {
        for (Ghost ghost : ghosts)
            ghost.getAnimationManager().draw(canvas, ghost.getRectangle());
    }

    @Override
    public boolean collideWithObjects(Player player) {
        return ghosts.get(0).playerCollide(player);
    }

    @Override
    public void menuScreenUpdate() {
        moveObjectsDown();
        if (ghosts.size() >= 1) {
            if (ghosts.get(0).getRectangle().top > Constants.SCREEN_HEIGHT) {
                ghosts.remove(0);
            }
        }
    }

    void spawnGhost(int yDistance) {
        ghosts.add(new Ghost(yDistance));
    }
}
