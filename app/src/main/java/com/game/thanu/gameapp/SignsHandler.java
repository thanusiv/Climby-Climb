package com.game.thanu.gameapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;

import static com.game.thanu.gameapp.GroundsHandler.grounds;
import static com.game.thanu.gameapp.ScoresHandler.currentScore;

class SignsHandler implements BackgroundObjectHandler {

    private AnimationManager animManagerSignRight;
    private AnimationManager animManagerSignLeft;
    private AnimationManager animManagerSignUp;
    private AnimationManager animManagerSignDown;
    private AnimationManager animManagerSignHold;
    private AnimationManager animManagerSignRelease;
    private ArrayList<Sign> signs;
    private boolean addFinalSign;

    SignsHandler() {
        Bitmap signRight = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.signright);
        Bitmap signLeft = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.signleft);
        Bitmap signUp = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.signup);
        Bitmap signDown = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.signdown);
        Bitmap signHold = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.signhold);
        Bitmap signRelease = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.signrelease);
        Animation signRightPic = new Animation(new Bitmap[]{signRight}, 100f);
        Animation signLeftPic = new Animation(new Bitmap[]{signLeft}, 100f);
        Animation signUpPic = new Animation(new Bitmap[]{signUp}, 100f);
        Animation signDownPic = new Animation(new Bitmap[]{signDown}, 100f);
        Animation signHoldPic = new Animation(new Bitmap[]{signHold}, 100f);
        Animation signReleasePic = new Animation(new Bitmap[]{signRelease}, 100f);
        animManagerSignRight = new AnimationManager(new Animation[]{signRightPic});
        animManagerSignLeft = new AnimationManager(new Animation[]{signLeftPic});
        animManagerSignUp = new AnimationManager(new Animation[]{signUpPic});
        animManagerSignDown = new AnimationManager(new Animation[]{signDownPic});
        animManagerSignHold = new AnimationManager(new Animation[]{signHoldPic});
        animManagerSignRelease = new AnimationManager(new Animation[]{signReleasePic});
        signs = new ArrayList<>();
        addFinalSign = false;
    }

    @Override
    public void addInitialObjects() {
        signs.add(new Sign(findFirstSignDistance(), grounds.get(grounds.size() - 1).getRectangle().top, "Single"));
        signs.add(new Sign(findFirstSignDistance() + Constants.SCREEN_WIDTH / 6, grounds.get(grounds.size() - 2).getRectangle().top, "Double"));
        signs.add(new Sign(findFirstSignDistance() + Constants.SCREEN_WIDTH / 90, grounds.get(grounds.size() - 2).getRectangle().top, "Double"));
        signs.add(new Sign(0, grounds.get(grounds.size() - 3).getRectangle().top, "Double2"));
        signs.add(new Sign(Constants.SCREEN_WIDTH / 4, grounds.get(grounds.size() - 3).getRectangle().top, "Double2"));
    }

    @Override
    public void moveObjectsDown() {
        for (int i = 0; i < signs.size(); i++)
            signs.get(i).incrementY(Constants.OBJECT_CLIMB_SPEED);
    }

    @Override
    public void adjustObjectsDown() {
    }

    @Override
    public void adjustObjectsUp() {
    }

    @Override
    public void addNewObjectAndRemoveOldObject() {

        if (!addFinalSign) {
            signs.add(new Sign(findFirstSignDistance() + Constants.SCREEN_WIDTH / 6, grounds.get(grounds.size() - 3).getRectangle().top, "Single2"));
            addFinalSign = true;
        }

        if (currentScore == 2)
            signs.remove(0);
        else if (currentScore == 3) {
            signs.remove(0);
            signs.remove(0);
        } else if (currentScore == 4) {
            signs.remove(0);
            signs.remove(0);
        } else if (currentScore == 5) {
            signs.remove(0);
        }
        Log.d("Signs", "" + signs.size());
    }

    @Override
    public void playObjectsAnimation() {
        animManagerSignRight.playAnim(0);
        animManagerSignRight.update();
        animManagerSignLeft.playAnim(0);
        animManagerSignLeft.update();
        animManagerSignUp.playAnim(0);
        animManagerSignUp.update();
        animManagerSignDown.playAnim(0);
        animManagerSignDown.update();
        animManagerSignHold.playAnim(0);
        animManagerSignHold.update();
        animManagerSignRelease.playAnim(0);
        animManagerSignRelease.update();
    }

    @Override
    public void drawObjects(Canvas canvas) {
        for (int i = 0; i < signs.size(); i++) {
            if (signs.size() == 5 && !addFinalSign) {
                if (i == 0)
                    animManagerSignRight.draw(canvas, signs.get(i).getRectangle());
                else if (i == 1)
                    animManagerSignLeft.draw(canvas, signs.get(i).getRectangle());
                else if (i == 2)
                    animManagerSignUp.draw(canvas, signs.get(i).getRectangle());
                else if (i == 3)
                    animManagerSignHold.draw(canvas, signs.get(i).getRectangle());
                else if (i == 4)
                    animManagerSignRelease.draw(canvas, signs.get(i).getRectangle());
            } else if (signs.size() == 5 && addFinalSign) {
                if (i == 0)
                    animManagerSignLeft.draw(canvas, signs.get(i).getRectangle());
                else if (i == 1)
                    animManagerSignUp.draw(canvas, signs.get(i).getRectangle());
                else if (i == 2)
                    animManagerSignHold.draw(canvas, signs.get(i).getRectangle());
                else if (i == 3)
                    animManagerSignRelease.draw(canvas, signs.get(i).getRectangle());
                else if (i == 4)
                    animManagerSignDown.draw(canvas, signs.get(i).getRectangle());
            } else if (signs.size() == 3) {
                if (i == 0)
                    animManagerSignHold.draw(canvas, signs.get(i).getRectangle());
                else if (i == 1)
                    animManagerSignRelease.draw(canvas, signs.get(i).getRectangle());
                if (i == 2)
                    animManagerSignDown.draw(canvas, signs.get(i).getRectangle());
            } else if (signs.size() == 1)
                animManagerSignDown.draw(canvas, signs.get(i).getRectangle());
        }
    }

    @Override
    public boolean collideWithObjects(Player player) {
        return false;
    }

    @Override
    public void menuScreenUpdate() {
    }

    private int findFirstSignDistance() {
        //Width: 768 Value: 330
        //Width: 640 Value: 270
        //Width: 720 Value: 320
        //Width: 480 Value: 210
        //Width: 1080 Value: 490
        //Width: 1440 Value: 610

        return (int) ((0.427971 * Constants.SCREEN_WIDTH) - 5.89448);
    }

    void adjustObjects() {

        int counterGround = 0;
        int countSigns = 0;

        for (int i = 0; i < signs.size(); i++) {

            if (signs.get(i).getType().equals("Single") && countSigns == 0)
                countSigns = 1;
            else if (signs.get(i).getType().equals("Double") && countSigns == 0)
                countSigns = 2;
            else if (signs.get(i).getType().equals("Double2") && countSigns == 0)
                countSigns = 2;
            else if (signs.get(i).getType().equals("Single2") && countSigns == 0)
                countSigns = 2;

            signs.get(i).relocate(grounds.get(grounds.size() - (counterGround + 1)).getRectangle().top);
            countSigns--;

            if (countSigns == 0)
                counterGround++;
        }

    }

}
