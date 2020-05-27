package com.game.thanu.gameapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

class ChainsHandler implements BackgroundObjectHandler {

    private AnimationManager animManagerChain;
    private ArrayList<Chain> chains;
    private int chainIndex;

    ChainsHandler() {
        Bitmap chain = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.longchain);
        Animation chainPic = new Animation(new Bitmap[]{chain}, 100f);
        animManagerChain = new AnimationManager(new Animation[]{chainPic});
        chains = new ArrayList<>();
        chainIndex = 4;
    }

    @Override
    public void addInitialObjects() {
        chains.add(new Chain(3));
        chains.add(new Chain(2));
        chains.add(new Chain(1));
    }

    @Override
    public void moveObjectsDown() {
        for (int i = 0; i < chains.size(); i++)
            chains.get(i).incrementY(Constants.OBJECT_CLIMB_SPEED);
    }

    @Override
    public void adjustObjectsDown() {
        for (int i = 0; i < chains.size(); i++)
            chains.get(i).incrementY(1);
    }

    @Override
    public void adjustObjectsUp() {
        for (int i = 0; i < chains.size(); i++)
            chains.get(i).decrementY(1);
    }

    @Override
    public void addNewObjectAndRemoveOldObject() {
        chains.add(0, new Chain(chainIndex));
        chainIndex++;
        chains.remove(chains.size() - 1);
        Log.d("Chains Size", "" + chains.size());
    }

    @Override
    public void playObjectsAnimation() {
        animManagerChain.playAnim(0);
        animManagerChain.update();
    }

    @Override
    public void drawObjects(Canvas canvas) {
        for (Chain chain : chains)
            animManagerChain.draw(canvas, chain.getRectangle());
    }

    @Override
    public boolean collideWithObjects(Player player) {
        return Rect.intersects(chains.get(chains.size() - 1).getRectangle(), player.getRectangle());
    }

    @Override
    public void menuScreenUpdate() {
        moveObjectsDown();
        if (chains.get(chains.size() - 2).getRectangle().bottom > Constants.SCREEN_HEIGHT)
            addNewObjectAndRemoveOldObject();
    }

    int returnChainIndex() {
        return chainIndex;
    }

}
