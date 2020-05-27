package com.game.thanu.gameapp;

import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import static com.game.thanu.gameapp.GroundsHandler.grounds;
import static com.game.thanu.gameapp.GroundsHandler.removedGroundMenu;

class ObstacleManager {

    private SpikesHandler spikesHandler;
    private GhostsHandler ghostsHandler;
    private BeesHandler beesHandler;
    private Random r;
    static ArrayList<String> allObstaclesSpawned;
    private int chainIndex;
    private boolean addBeeTutorial;

    ObstacleManager() {
        spikesHandler = new SpikesHandler();
        spikesHandler.addInitialObjects();
        ghostsHandler = new GhostsHandler();
        beesHandler = new BeesHandler();
        r = new Random();
        allObstaclesSpawned = new ArrayList<>();
        if (!SceneManager.startGameplay) {
            allObstaclesSpawned.add("Spikes");
            allObstaclesSpawned.add("Spikes");
            allObstaclesSpawned.add("Spikes");
            addBeeTutorial = false;
        }
        else{
            ghostsHandler.addInitialObjects();
            allObstaclesSpawned.add("Spikes");
            allObstaclesSpawned.add("Spikes");
            allObstaclesSpawned.add("Ghost");
            addBeeTutorial = true;
        }
        chainIndex = 3;
    }

    private void chooseObstacle() {
        int obstacle = r.nextInt(3) + 1;
        ++chainIndex;

        if (addBeeTutorial) {
            obstacle = 3;
            addBeeTutorial = false;
        }

        switch (obstacle) {
            case 1:
                spikesHandler.spawnSpikes(grounds.get(grounds.size() - 3).getRectangle().top);
                allObstaclesSpawned.add("Spikes");
                break;
            case 2:
                ghostsHandler.spawnGhost(grounds.get(grounds.size() - 3).getRectangle().top);
                allObstaclesSpawned.add("Ghost");
                break;
            case 3:
                beesHandler.spawnBee(grounds.get(grounds.size() - 3).getRectangle().top, chainIndex);
                allObstaclesSpawned.add("Bees");
                break;
        }
        Log.d("All Obstacles Spawned", "" + allObstaclesSpawned.size());
    }

    public void update() {
        playerIsClimbing();
        playerCompletesClimb();
        playObstacleObjectAnimations();
    }

    boolean playerCollideWithObstacles(Player player) {
        if (allObstaclesSpawned.get(0).equals("Spikes"))
            return spikesHandler.collideWithObjects(player);
        else if (allObstaclesSpawned.get(0).equals("Ghost"))
            return ghostsHandler.collideWithObjects(player);
        else
            return beesHandler.collideWithObjects(player);
    }

    public void draw(Canvas canvas) {
        spikesHandler.drawObjects(canvas);
        ghostsHandler.drawObjects(canvas);
        beesHandler.drawObjects(canvas);
    }

    void menuScreen() {
        spikesHandler.menuScreenUpdate();
        ghostsHandler.menuScreenUpdate();
        beesHandler.menuScreenUpdate();
        chooseNextMenuObstacle();
        playObstacleObjectAnimations();
    }

    private void playerIsClimbing() {
        if (Player.climbShift) {
            spikesHandler.moveObjectsDown();
            ghostsHandler.moveObjectsDown();
            beesHandler.moveObjectsDown();
        }
    }

    private void playerCompletesClimb() {
        if (Player.doneClimb) {
            removeObstacles();
            chooseObstacle();
            adjustObstacleObjectsAfterClimb();
            Player.doneClimb = false;
        }
    }

    private void adjustObstacleObjectsAfterClimb() {
        spikesHandler.adjustObjects();
        ghostsHandler.adjustObjects();
        beesHandler.adjustObjects();
    }

    //Last index is newest object
    private void removeObstacles() {

        if (allObstaclesSpawned.get(0).equals("Spikes"))
            spikesHandler.removeObjects();
        else if (allObstaclesSpawned.get(0).equals("Ghost"))
            ghostsHandler.removeObjects();
        else
            beesHandler.removeObjects();

        allObstaclesSpawned.remove(0);
    }

    private void playObstacleObjectAnimations() {
        spikesHandler.playObjectsAnimation();
        ghostsHandler.playObjectsAnimation();
        beesHandler.playObjectsAnimation();
    }

    private void chooseNextMenuObstacle() {
        if (removedGroundMenu) {
            chooseObstacle();
            allObstaclesSpawned.remove(0);
        }
    }

}
