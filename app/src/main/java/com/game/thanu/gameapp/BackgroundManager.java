package com.game.thanu.gameapp;

import android.graphics.Canvas;
import android.util.Log;

import static com.game.thanu.gameapp.GroundsHandler.grounds;
import static com.game.thanu.gameapp.Player.doneClimb;

class BackgroundManager {

    private ChainsHandler chainsHandler;
    private GroundsHandler groundsHandler;
    private ScoresHandler scoresHandler;
    private SignsHandler signsHandler;
    private boolean moveBackground;

    BackgroundManager() {
        chainsHandler = new ChainsHandler();
        groundsHandler = new GroundsHandler();
        scoresHandler = new ScoresHandler(chainsHandler.returnChainIndex());
        signsHandler = new SignsHandler();
        moveBackground = false;
        startingBackground();
    }

    public void update(Player player) {
        playerIsClimbing();
        checkIfClimbNeedsToBeFixed(player);
        playerCompletesClimb();
        playBackgroundObjectAnimations();
    }

    //Lowest ground is highest index
    boolean playerCollideGround(Player player) {
        return groundsHandler.collideWithObjects(player);
    }

    //Lowest chain is highest index
    boolean playerCollideChain(Player player) {
        return chainsHandler.collideWithObjects(player);
    }

    public void draw(Canvas canvas) {
        groundsHandler.drawObjects(canvas);
        chainsHandler.drawObjects(canvas);
        scoresHandler.drawObjects(canvas);
        signsHandler.drawObjects(canvas);
    }

    private void startingBackground() {
        Log.d("ccc", "" + Constants.SCREEN_HEIGHT);
        Log.d("ccc", " " + Constants.SCREEN_WIDTH);
        groundsHandler.addInitialObjects();
        chainsHandler.addInitialObjects();
        scoresHandler.addInitialObjects();
        if (SceneManager.startGameplay)
            signsHandler.addInitialObjects();
    }

    private void playerIsClimbing(){
        if (Player.climbShift) {
            if (grounds.get(grounds.size() - 2).getRectangle().bottom < Constants.SCREEN_HEIGHT) {
                groundsHandler.moveObjectsDown();
                chainsHandler.moveObjectsDown();
                signsHandler.moveObjectsDown();
                moveBackground = true;
            }
            else
                moveBackground = false;
        }
    }

    private void checkIfClimbNeedsToBeFixed(Player player){
        if (Player.climbShift && !doneClimb && !moveBackground && player.getRectangle().top < grounds.get(grounds.size() - 2).getRectangle().top){
            GameplayScene.fixClimb = true;
        }
    }

    private void playerCompletesClimb(){
        if (Player.doneClimb) {
            adjustBackgroundObjectsAfterClimb();
            groundsHandler.addNewObjectAndRemoveOldObject();
            scoresHandler.addNewObjectAndRemoveOldObject();
            scoresHandler.adjustCurrentScore();
            signsHandler.addNewObjectAndRemoveOldObject();
            chainsHandler.addNewObjectAndRemoveOldObject();
        }
    }

    private void adjustBackgroundObjectsAfterClimb(){
        while (grounds.get(grounds.size() - 2).getRectangle().bottom < Constants.SCREEN_HEIGHT || grounds.get(grounds.size() - 2).getRectangle().top > Constants.SCREEN_HEIGHT - grounds.get(grounds.size() - 2).getRectangle().height()) {
            for (int i = 0; i < grounds.size(); i++) {
                if (grounds.get(grounds.size() - 2).getRectangle().bottom < Constants.SCREEN_HEIGHT) {
                    groundsHandler.adjustObjectsDown();
                    chainsHandler.adjustObjectsDown();
                }
                else {
                    groundsHandler.adjustObjectsUp();
                    chainsHandler.adjustObjectsUp();
                }
            }
        }
        signsHandler.adjustObjects();
    }

    void menuScreen(){
        groundsHandler.menuScreenUpdate();
        scoresHandler.menuScreenUpdate();
        chainsHandler.menuScreenUpdate();
        playBackgroundObjectAnimations();
    }

    private void playBackgroundObjectAnimations(){
        groundsHandler.playObjectsAnimation();
        chainsHandler.playObjectsAnimation();
        signsHandler.playObjectsAnimation();
    }

}
