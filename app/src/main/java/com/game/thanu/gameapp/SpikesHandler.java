package com.game.thanu.gameapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import static com.game.thanu.gameapp.GroundsHandler.grounds;
import static com.game.thanu.gameapp.ScoresHandler.currentScore;

class SpikesHandler implements ObstacleObjectHandler {

    private AnimationManager animManagerSpike;
    private ArrayList<Spike> spikes;
    private int distanceFirstSpike;
    private int extraDistance;
    private boolean tutorial;
    private Random r;

    SpikesHandler() {
        Bitmap spike = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.spikes);
        Animation spikePic = new Animation(new Bitmap[]{spike}, 100f);
        animManagerSpike = new AnimationManager(new Animation[]{spikePic});
        spikes = new ArrayList<>();

        tutorial = true;

        r = new Random();

        distanceFirstSpike = findDistanceForFirstSpike();
        extraDistance = findExtraDistance();
    }

    void addInitialObjects() {
        if (!SceneManager.startGameplay) {
            spawnSpikes(grounds.get(grounds.size() - 1).getRectangle().top);
            spawnSpikes(grounds.get(grounds.size() - 2).getRectangle().top);
            spawnSpikes(grounds.get(grounds.size() - 3).getRectangle().top);
        } else
            spawnASpike(grounds.get(grounds.size() - 2).getRectangle().top);
    }

    @Override
    public void moveObjectsDown() {
        for (int i = 0; i < spikes.size(); i++)
            spikes.get(i).incrementY(Constants.OBJECT_CLIMB_SPEED);
    }

    @Override
    public void removeObjects() {
        if (!tutorial) {
            spikes.remove(0);
            spikes.remove(0);
        } else if (currentScore == 3) {
            spikes.remove(0);
            tutorial = false;
        }
    }

    public void adjustObjects() {
        int x = 0;
        for (int i = 0; i < ObstacleManager.allObstaclesSpawned.size(); i++) {
            if (ObstacleManager.allObstaclesSpawned.get(i).equals("Spikes")) {
                if (!tutorial) {
                    spikes.get(x).relocate(grounds.get(grounds.size() - (i + 1)).getRectangle().top);
                    spikes.get(++x).relocate(grounds.get(grounds.size() - (i + 1)).getRectangle().top);
                } else if (tutorial && i == 0)
                    spikes.get(x).relocate(grounds.get(grounds.size() - 1).getRectangle().top);

                ++x;
            }
        }
        Log.d("Spikes Spawned", "" + spikes.size() / 2);
    }

    @Override
    public void playObjectsAnimation() {
        animManagerSpike.playAnim(0);
        animManagerSpike.update();
    }

    @Override
    public void drawObjects(Canvas canvas) {
        for (Spike spike : spikes)
            animManagerSpike.draw(canvas, spike.getRectangle());
    }

    @Override
    public boolean collideWithObjects(Player player) {
        if (spikes.size() >= 2 && currentScore >= 3)
            return spikes.get(0).playerCollide(player) || spikes.get(1).playerCollide(player);
        else
            return currentScore == 2 && spikes.get(0).playerCollide(player);
    }

    @Override
    public void menuScreenUpdate() {
        moveObjectsDown();
        if (spikes.size() >= 4) {
            if (spikes.get(2).getRectangle().top > Constants.SCREEN_HEIGHT) {
                spikes.remove(0);
                spikes.remove(0);
            }
        }
    }

    void spawnSpikes(int yDistance) {
        int currX = Constants.SCREEN_WIDTH + 100;
        int i = 0;
        while (i < 2) {
            if (i == 0) {
                currX = r.nextInt(distanceFirstSpike) + extraDistance;
                //currX = distanceFirstSpike + extraDistance;
            } else
                currX += (double) Constants.SCREEN_WIDTH / 3;

            spikes.add(new Spike(currX, yDistance));
            i++;
        }
    }

    private void spawnASpike(int yDistance) {
        spikes.add(new Spike(findCenterSpikeDistance(), yDistance));
    }

    private int findCenterSpikeDistance() {
        //Width: 768 Value: 320
        //Width: 640 Value: 270
        //Width: 720 Value: 310
        //Width: 480 Value: 200
        //Width: 1080 Value: 453
        //Width: 1440 Value: 600

        return (int) ((0.414377 * Constants.SCREEN_WIDTH) + 4.6789);
    }

    private int findDistanceForFirstSpike() {
        //Width: 768 Value: 95
        //Width: 640 Value: 80
        //Width: 720 Value: 86
        //Width: 480 Value: 55
        //Width: 1080 Value: 122
        //Width: 1440 Value: 150

        return (int) ((0.0908218 * Constants.SCREEN_WIDTH) + 15.3776);
    }

    private int findExtraDistance() {
        //Width: 768 Value: 140
        //Width: 640 Value: 125
        //Width: 720 Value: 145
        //Width: 480 Value: 100
        //Width: 1080 Value: 213
        //Width: 1440 Value: 300

        return (int) ((0.210797 * Constants.SCREEN_WIDTH) - 9.66092);
    }
}
