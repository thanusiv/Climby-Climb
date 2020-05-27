package com.game.thanu.gameapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;

import java.util.Random;

class Bee implements ObstacleObject {

    private Rect rectangle;
    private boolean moveLeft;
    private boolean moveRight;
    private AnimationManager animManagerBee;
    private int distanceFromGroundTop;
    private int chainIndex;
    private boolean crossedCheckPoint;
    private boolean spawnedAnotherBee;
    private int rectangleTop;

    Bee(int rectangleTop, int chainIndex) {

        this.rectangleTop = rectangleTop;

        this.chainIndex = chainIndex;
        spawnedAnotherBee = false;

        rectangle = new Rect(0, 0, Constants.SCREEN_WIDTH / 8, Constants.SCREEN_HEIGHT / 10);

        int groundSpaceDifference = Constants.GROUND_SPACE_DIFFERENCE/2;
        groundSpaceDifference -= GroundsHandler.grounds.get(0).getRectangle().height() * 2;

        Random r = new Random();
        distanceFromGroundTop = r.nextInt(groundSpaceDifference);

        if (chainIndex % 2 == 0) {
            rectangle.set(-Constants.SCREEN_WIDTH / 4, rectangleTop - distanceFromGroundTop - rectangle.height(), -Constants.SCREEN_WIDTH / 4 + rectangle.width(), rectangleTop - distanceFromGroundTop);
            moveRight = true;
            moveLeft = false;
        } else {
            rectangle.set(Constants.SCREEN_WIDTH / 4 + Constants.SCREEN_WIDTH, rectangleTop - distanceFromGroundTop - rectangle.height(), Constants.SCREEN_WIDTH / 4 + Constants.SCREEN_WIDTH + rectangle.width(), rectangleTop - distanceFromGroundTop);
            moveRight = false;
            moveLeft = true;
        }

        Bitmap beeFirstPic = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.bee);
        Bitmap beeSecondPic = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.bee_fly);
        Animation beeLeft = new Animation(new Bitmap[]{beeFirstPic, beeSecondPic}, 0.5f);

        Matrix m = new Matrix();
        m.preScale(-1, 1);
        beeFirstPic = Bitmap.createBitmap(beeFirstPic, 0, 0, beeFirstPic.getWidth(), beeFirstPic.getHeight(), m, false);
        beeSecondPic = Bitmap.createBitmap(beeSecondPic, 0, 0, beeSecondPic.getWidth(), beeSecondPic.getHeight(), m, false);
        Animation beeRight = new Animation(new Bitmap[]{beeFirstPic, beeSecondPic}, 0.5f);

        animManagerBee = new AnimationManager(new Animation[]{beeLeft, beeRight});

        crossedCheckPoint = false;
    }

    int getRectangleTop(){
       return rectangleTop;
    }

    int getChainIndex(){
        return chainIndex;
    }

    boolean getCrossedCheckPoint() {
        return crossedCheckPoint;
    }

    private void checkIfBeeCrossedCheckPoint() {
        if (!crossedCheckPoint) {
            if (chainIndex % 2 == 0) {
                if (rectangle.right > Constants.SCREEN_WIDTH / 2)
                    crossedCheckPoint = true;
            } else {
                if (rectangle.left < Constants.SCREEN_WIDTH - Constants.SCREEN_WIDTH / 2)
                    crossedCheckPoint = true;
            }
        }
    }

    boolean getSpawnedAnotherBee(){
        return spawnedAnotherBee;
    }

    void anotherBeeSpawned(){
        spawnedAnotherBee = true;
    }

    @Override
    public Rect getRectangle() {
        return rectangle;
    }

    @Override
    public AnimationManager getAnimationManager() {
        return animManagerBee;
    }

    @Override
    public void incrementY(float y) {
        rectangle.top += y;
        rectangle.bottom += y;
        rectangleTop += y;
    }

    @Override
    public boolean playerCollide(Player player) {
        return Rect.intersects(player.getRectangle(), rectangle);
    }

    @Override
    public void relocate(int yDistance) {
        rectangle.set(rectangle.left, distanceFromGroundTop + yDistance - rectangle.height(), rectangle.left + rectangle.width(), distanceFromGroundTop + yDistance);
    }

    void playAnimation() {
        if (moveLeft)
            animManagerBee.playAnim(0);
        else if (moveRight)
            animManagerBee.playAnim(1);

        animManagerBee.update();
    }

    void moveBee() {
        if (moveRight) {
            rectangle.left += Constants.BEE_SPEED;
            rectangle.right += Constants.BEE_SPEED;
        } else if (moveLeft) {
            rectangle.left -= Constants.BEE_SPEED;
            rectangle.right -= Constants.BEE_SPEED;
        }

        checkIfBeeCrossedCheckPoint();
    }

}
