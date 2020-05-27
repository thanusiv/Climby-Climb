package com.game.thanu.gameapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import java.util.Random;

class Ghost implements ObstacleObject{

    private Rect rectangle;
    private boolean moveLeft;
    private boolean moveRight;
    private final int left;
    private final int right;
    private AnimationManager animManagerGhost;

    Ghost(int yDistance){

        left = (int) Math.ceil((double) (Constants.SCREEN_WIDTH) / 6);
        right = Constants.SCREEN_WIDTH - (int) Math.ceil((double) (Constants.SCREEN_WIDTH) / 6);

        Random r = new Random();
        int x = r.nextInt(right - Constants.SCREEN_WIDTH/6) + left;
        rectangle = new Rect(0,0,Constants.SCREEN_WIDTH/8, Constants.SCREEN_HEIGHT/10);
        rectangle.set(x, yDistance - rectangle.height(), x + rectangle.width(), yDistance);

        if (r.nextInt(2) == 0) {
            moveLeft = false;
            moveRight = true;
        }
        else{
            moveLeft = true;
            moveRight = false;
        }

        Bitmap ghostNormalPic = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.ghost);
        Bitmap ghostRagePic = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.ghost_normal);
        Animation ghostRageLeft = new Animation(new Bitmap[]{ghostRagePic, ghostNormalPic}, 0.5f);

        Matrix m = new Matrix();
        m.preScale(-1, 1);
        ghostNormalPic = Bitmap.createBitmap(ghostNormalPic, 0, 0, ghostNormalPic.getWidth(), ghostNormalPic.getHeight(), m, false);
        ghostRagePic = Bitmap.createBitmap(ghostRagePic, 0, 0, ghostRagePic.getWidth(), ghostRagePic.getHeight(), m, false);
        Animation ghostRageRight = new Animation(new Bitmap[]{ghostRagePic, ghostNormalPic}, 0.5f);

        animManagerGhost = new AnimationManager(new Animation[]{ghostRageLeft, ghostRageRight});
    }

    @Override
    public Rect getRectangle() {
        return rectangle;
    }

    @Override
    public AnimationManager getAnimationManager(){return animManagerGhost;}

    @Override
    public void incrementY(float y) {
        rectangle.top += y;
        rectangle.bottom += y;
    }

    @Override
    public boolean playerCollide(Player player) {
        return Rect.intersects(player.getRectangle(), rectangle);
    }

    @Override
    public void relocate(int yDistance) {
        rectangle.set(rectangle.left, yDistance - rectangle.height(), rectangle.left + rectangle.width(), yDistance);
    }

    void moveGhost(){

        if (rectangle.left < left){
            moveLeft = false;
            moveRight = true;
        }
        else if (rectangle.right > right){
            moveRight = false;
            moveLeft = true;
        }

        if (moveRight){
            rectangle.left += Constants.GHOST_SPEED;
            rectangle.right += Constants.GHOST_SPEED;
        }
        else if (moveLeft){
            rectangle.left -= Constants.GHOST_SPEED;
            rectangle.right -= Constants.GHOST_SPEED;
        }

    }

    void playAnimation(){
        if (moveLeft)
            animManagerGhost.playAnim(0);
        else if (moveRight)
            animManagerGhost.playAnim(1);

        animManagerGhost.update();
    }



}
