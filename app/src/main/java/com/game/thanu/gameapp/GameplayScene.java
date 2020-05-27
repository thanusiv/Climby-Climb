package com.game.thanu.gameapp;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.view.MotionEvent;

import static com.game.thanu.gameapp.ScoresHandler.currentScore;

class GameplayScene implements Scene {

    private Player player;
    private Point playerPoint;
    static boolean gameOver;
    static long gameOverTime;
    private BackgroundManager backgroundManager;
    private ObstacleManager obstacleManager;
    private Activity activity;
    static boolean fixClimb = false;
    private int finishWithNoAd = 0;
    private boolean adShown;
    private EndScreen endScreen;

    GameplayScene(Activity activity) {
        this.activity = activity;
        adShown = false;
        player = new Player();
        playerPoint = new Point(0, Constants.SCREEN_HEIGHT / 2);
        player.update(playerPoint, false, false);
        gameOver = false;
        backgroundManager = new BackgroundManager();
        obstacleManager = new ObstacleManager();
        endScreen = new EndScreen();
    }

    @Override
    public void update() {
        if (!gameOver) {
            updatePlayer();
            backgroundManager.update(player);
            checkDeath();
        } else {
            player.updateDeath(playerPoint);
            if (System.currentTimeMillis() - gameOverTime > 2000) {
                endScreen.update();
            }
        }
        obstacleManager.update();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.rgb(135, 206, 235));
        backgroundManager.draw(canvas);
        player.draw(canvas);
        obstacleManager.draw(canvas);

        if (gameOver && System.currentTimeMillis() - gameOverTime > 2000) {
            showInterstitialAd();
            endScreen.draw();
        }
    }

    @Override
    public void receiveTouch(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                if (!gameOver && !Player.climbShift && !Player.doneClimb) {
                    player.getOldCoordinates((int) event.getY(), (int)event.getX());
                    player.storeInitialTap(System.currentTimeMillis());
                }
                checkToReset();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!Player.climbShift && !gameOver && !Player.doneClimb)
                    player.swipe((int) event.getY(), (int)event.getX());
                break;

            case MotionEvent.ACTION_UP:
                player.storeReleaseTap(System.currentTimeMillis());
                break;

        }

    }

    private void reset() {
        player = new Player();
        playerPoint = new Point(0, Constants.SCREEN_HEIGHT / 2);
        player.update(playerPoint, false, false);
        gameOver = false;
        backgroundManager = new BackgroundManager();
        obstacleManager = new ObstacleManager();
        fixClimb = false;
        finishWithNoAd++;
        adShown = false;
        if (finishWithNoAd == 6)
            finishWithNoAd = 0;
    }

    private void updatePlayer() {
        if (!fixClimb)
            player.update(playerPoint, backgroundManager.playerCollideGround(player), backgroundManager.playerCollideChain(player));
        else {
            player.update(playerPoint, backgroundManager.playerCollideGround(player), false);
            fixClimb = false;
        }
    }

    private void checkDeath() {
        if (obstacleManager.playerCollideWithObstacles(player) && !Player.immunity) {
            gameOver = true;
            gameOverTime = System.currentTimeMillis();
        }
    }

    private void showInterstitialAd() {
        if (MainActivity.load && !adShown && finishWithNoAd == 5 && currentScore > 4) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.mInterstitialAd.show();
                }
            });
            adShown = true;
        }
    }

    private void checkToReset() {
        if (gameOver && System.currentTimeMillis() - gameOverTime > 3500) {
            gameOver = false;
            reset();
        }
    }

}
