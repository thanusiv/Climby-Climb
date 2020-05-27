package com.game.thanu.gameapp;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

class BeesHandler implements ObstacleObjectHandler {

    private ArrayList<Bee> bees;

    BeesHandler() {
        bees = new ArrayList<>();
    }

    @Override
    public void moveObjectsDown() {
        for (int i = 0; i < bees.size(); i++)
            bees.get(i).incrementY(Constants.OBJECT_CLIMB_SPEED);
    }

    @Override
    public void removeObjects() {
        for (int i = 0; i < bees.size(); i++) {
            if (bees.get(i).getRectangle().top > Constants.SCREEN_HEIGHT)
                bees.remove(i);
        }
    }

    @Override
    public void adjustObjects() {
        Log.d("Bees size", "" + bees.size());
    }

    @Override
    public void playObjectsAnimation() {
        for (Bee bee : bees) {
            bee.moveBee();
            bee.playAnimation();
        }
        new AsyncCaller().execute();
    }

    @Override
    public void drawObjects(Canvas canvas) {
        for (Bee bee : bees)
            bee.getAnimationManager().draw(canvas, bee.getRectangle());
    }

    @Override
    public boolean collideWithObjects(Player player) {
        for (Bee bee : bees) {
            if (bee.playerCollide(player))
                return true;
        }

        return false;
    }

    @Override
    public void menuScreenUpdate() {
        moveObjectsDown();
        if (bees.size() >= 1) {
            if (bees.get(0).getRectangle().top > Constants.SCREEN_HEIGHT) {
                removeObjects();
            }
        }
    }

    void spawnBee(int yDistance, int chainIndex) {
        bees.add(new Bee(yDistance, chainIndex));
    }

    private void checkIfBeeNeedsToBeSpawned() {

        ArrayList<Bee> beesToSpawnFrom = new ArrayList<>();

        for (Bee bee : bees) {
            if (bee.getCrossedCheckPoint()) {
                if (bee.getRectangle().left > Constants.SCREEN_WIDTH + 10 || bee.getRectangle().right < -10)
                    beesToSpawnFrom.add(bee);
            }
        }

        for (Bee bee : beesToSpawnFrom) {
            bees.remove(bee);
        }

        beesToSpawnFrom.clear();

        for (Bee bee : bees) {
            if (bee.getCrossedCheckPoint()) {
                if (!bee.getSpawnedAnotherBee())
                    beesToSpawnFrom.add(bee);
            }
        }

        for (Bee bee : beesToSpawnFrom) {
            spawnBee(bee.getRectangleTop(), bee.getChainIndex());
            bee.anotherBeeSpawned();
        }
    }

    private class AsyncCaller extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread

        }

        @Override
        protected Void doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here

            checkIfBeeNeedsToBeSpawned();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            //this method will be running on UI thread

        }

    }


}
