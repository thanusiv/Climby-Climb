package com.game.thanu.gameapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;

import static com.game.thanu.gameapp.GroundsHandler.grounds;

public class Player {

    private Rect rectangle;
    private AnimationManager animManager;
    private int gravity;
    private boolean stopGravity;
    private int chainNum;
    static boolean climbShift;
    static boolean doneClimb;
    private int oldX;
    private int oldY;
    private boolean jump;
    private boolean crouch;
    private int state;
    private int jumpForce;
    private boolean deadJump;
    private boolean moveRight;
    private boolean moveLeft;
    private long initialTap;
    private boolean swipe;
    static boolean immunity;

    public Player() {
        rectangle = new Rect(0, 0, Constants.SCREEN_HEIGHT / 12, Constants.SCREEN_HEIGHT / 12);
        gravity = 1;
        jump = false;
        crouch = false;
        chainNum = 0;
        state = 0;

        Bitmap idleImg = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.alienblue);
        Bitmap walk1 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.alienblue_walk1);
        Bitmap walk2 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.alienblue_walk2);
        Bitmap climb1 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.alienblue_climb);
        Bitmap climb2 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.alienblue_climb1);
        Bitmap jump1 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.alienblue_jump);
        Bitmap idleJump1 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.alienblue_stand);
        Bitmap crouch1 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.alienblue_duck);
        Bitmap death1 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.alienblue_hurt);

        Animation idle = new Animation(new Bitmap[]{idleImg}, 2);
        Animation walkRight = new Animation(new Bitmap[]{walk1, walk2}, 0.5f);
        Animation climb = new Animation(new Bitmap[]{climb1, climb2}, 0.4f);
        Animation jumpRight = new Animation(new Bitmap[]{jump1}, 0.4f);
        Animation idleJumpRight = new Animation(new Bitmap[]{idleJump1}, 0.4f);
        Animation crouchRight = new Animation(new Bitmap[]{crouch1}, 0.4f);
        Animation deathRight = new Animation(new Bitmap[]{death1}, 0.4f);

        Matrix m = new Matrix();
        m.preScale(-1, 1);
        walk1 = Bitmap.createBitmap(walk1, 0, 0, walk1.getWidth(), walk1.getHeight(), m, false);
        walk2 = Bitmap.createBitmap(walk2, 0, 0, walk2.getWidth(), walk2.getHeight(), m, false);
        jump1 = Bitmap.createBitmap(jump1, 0, 0, jump1.getWidth(), jump1.getHeight(), m, false);
        crouch1 = Bitmap.createBitmap(crouch1, 0, 0, crouch1.getWidth(), crouch1.getHeight(), m, false);
        idleJump1 = Bitmap.createBitmap(idleJump1, 0, 0, idleJump1.getWidth(), idleJump1.getHeight(), m, false);
        death1 = Bitmap.createBitmap(death1, 0, 0, death1.getWidth(), death1.getHeight(), m, false);

        Animation walkLeft = new Animation(new Bitmap[]{walk1, walk2}, 0.5f);
        Animation jumpLeft = new Animation(new Bitmap[]{jump1}, 0.4f);
        Animation crouchLeft = new Animation(new Bitmap[]{crouch1}, 0.4f);
        Animation idleJumpLeft = new Animation(new Bitmap[]{idleJump1}, 0.4f);
        Animation deathLeft = new Animation(new Bitmap[]{death1}, 0.4f);

        animManager = new AnimationManager(new Animation[]{idle, walkRight, walkLeft, climb, jumpRight, jumpLeft, idleJumpRight, idleJumpLeft, crouchRight, crouchLeft, deathRight, deathLeft});
        stopGravity = false;
        climbShift = false;
        doneClimb = false;
        deadJump = true;
        moveLeft = false;
        moveRight = false;
        swipe = false;
        oldX = 0;
        oldY = 0;
        initialTap = 0;
        setJumpForce();
        immunity = false;
    }

    public Rect getRectangle() {
        return rectangle;
    }

    public void draw(Canvas canvas) {
        animManager.draw(canvas, rectangle);
    }

    void getOldCoordinates(int oldY, int oldX) {
        this.oldY = oldY;
        this.oldX = oldX;
    }

    void storeInitialTap(long initialTap) {
        this.initialTap = initialTap;
    }

    void storeReleaseTap(long release) {

        if (release - initialTap <= 145 && !swipe) {
            if (!jump && stopGravity && !climbShift && !crouch) {
                if (state == 3)
                    doneClimb = true;
                jump = true;
            }
        }
        else if (release - initialTap >= 400 && !swipe) {
            moveLeft = false;
            moveRight = false;
        }
    }

    void swipe(int y, int x) {

        if (oldY - y > Constants.SWIPE_LENGTH) {
            crouch = false;
            swipe = true;
        } else if (oldY - y < -Constants.SWIPE_LENGTH) {
            if (!jump && stopGravity && !climbShift && !crouch) {
                crouch = true;
                swipe = true;
            }
        } else
            swipe = false;

        if (oldX - x > Constants.SWIPE_LENGTH) {
            if (stopGravity && !climbShift) {
                moveLeft = true;
                swipe = true;
                crouch = false;
                moveRight = false;
            }
        } else if (oldX - x < -Constants.SWIPE_LENGTH) {
            if (stopGravity && !climbShift) {
                moveRight = true;
                swipe = true;
                crouch = false;
                moveLeft = false;
            }
        }
        else
            swipe = false;

    }

    public void update(Point point, boolean reverse, boolean climb) {
        float oldLeft = rectangle.left;
        immunity = climb;
        checkJumpAndUpdate(point, reverse);
        checkMovingAndUpdate(climb, point);
        checkClimbAndUpdate(climb, crouch, point);
        checkCrouchAndUpdate(point);
        setPlayerOnGround(point);
        playPlayerAnimation(climb, oldLeft);
    }

    void updateDeath(Point point) {
        if (!(point.y > Constants.SCREEN_HEIGHT + 100)) {
            checkDeathJumpAndUpdate(point);
            setPlayerOnGround(point);
            playPlayerAnimation(false, 0);
        }
    }

    private void playPlayerAnimation(boolean climb, float oldLeft) {

        if (!GameplayScene.gameOver) {

            if (!climb && !jump && stopGravity && crouch) {

                if (state == 1 || (chainNum % 2 == 0 && state == 0))
                    state = 8;
                else if (state == 2 || (chainNum % 2 != 0 && state == 0))
                    state = 9;

            } else if (climb && !jump && stopGravity && !crouch) {
                state = 3;
            } else if (jump || !stopGravity) {

                if (rectangle.left - oldLeft > 4)
                    state = 4;
                else if (rectangle.left - oldLeft < -4)
                    state = 5;
                else {
                    if (state == 1 || state == 4 || state == 0 || state == 8)
                        state = 6;
                    else if (state == 2 || state == 5 || state == 9)
                        state = 7;
                }

            } else if (moveRight) {
                if (rectangle.left - oldLeft > Constants.PLAYER_SPEED / 2)
                    state = 1;

            } else if (moveLeft) {
                if (rectangle.left - oldLeft < -Constants.PLAYER_SPEED / 2)
                    state = 2;

            } else {

                if (state == 3)
                    doneClimb = true;

                state = 0;
            }

            animManager.playAnim(state);
            animManager.update();
        } else {
            int x;

            if (state == 1 || state == 4 || state == 6)
                x = 10;
            else
                x = 11;

            animManager.playAnim(x);
            animManager.update();
        }
    }

    private void setPlayerOnGround(Point point) {
        rectangle.set(point.x - rectangle.width() / 2, point.y - rectangle.height() / 2, point.x + rectangle.width() / 2, point.y + rectangle.height() / 2);
    }

    private void setJumpForce() {
//        if (Constants.SCREEN_WIDTH == 768) {
//            jumpForce = (int) Math.ceil((double) (Constants.SCREEN_HEIGHT) / (double) 56);
//        } else if (Constants.SCREEN_WIDTH == 640) {
//            jumpForce = (int) Math.ceil((double) (Constants.SCREEN_HEIGHT) / (double) 52);
//        } else if (Constants.SCREEN_WIDTH == 720 && Constants.SCREEN_HEIGHT == 1184) {
//            jumpForce = (int) Math.ceil((double) (Constants.SCREEN_HEIGHT) / (double) 52);
//        } else if (Constants.SCREEN_WIDTH == 480 && Constants.SCREEN_HEIGHT >= 800) {
//            jumpForce = (int) Math.ceil((double) (Constants.SCREEN_HEIGHT) / (double) 47);
//        } else if (Constants.SCREEN_WIDTH < 1000 && Constants.SCREEN_WIDTH > 500) {
//            jumpForce = (int) Math.ceil((double) (Constants.SCREEN_HEIGHT) / (double) 52);
//        } else if (Constants.SCREEN_WIDTH > 1300) {
//            jumpForce = (int) Math.ceil((double) (Constants.SCREEN_HEIGHT) / (double) 75);
//        } else {
//            jumpForce = (int) Math.floor((double) (Constants.SCREEN_HEIGHT) / (double) 66);
//        }
        jumpForce = (int) Math.floor(0.00821809 * Constants.SCREEN_HEIGHT + 12.6727);
    }

    private void checkCrouchAndUpdate(Point point) {
        if (crouch) {
            moveRight = false;
            moveLeft = false;
            point.y = (int) (grounds.get(grounds.size() - 1).getRectangle().top - grounds.get(grounds.size() - 1).getRectangle().height() / 2.3);
        }
    }

    private void checkJumpAndUpdate(Point point, boolean reverse) {

        if (jump && !crouch) {
            point.y -= jumpForce;
            jumpForce--;
        } else if (reverse) {
            point.y = grounds.get(grounds.size() - 1).getRectangle().top - rectangle.height() / 2;
            stopGravity = true;
            gravity = 0;
        } else if (!stopGravity) {
            point.y += gravity;
            gravity++;
        }

        if (jumpForce == 0) {
            jump = false;
            stopGravity = false;
            setJumpForce();
        }
    }

    private void checkMovingAndUpdate(boolean climb, Point point) {
        if (!climb && !climbShift) {
            if (state == 3)
                doneClimb = true;

            if (moveLeft) {
                if (point.x - Constants.PLAYER_SPEED >= Constants.EDGE_DISTANCE)
                    point.x -= Constants.PLAYER_SPEED;
                else
                    moveLeft = false;
            }

            if (moveRight) {
                if (point.x + Constants.PLAYER_SPEED <= Constants.SCREEN_WIDTH - Constants.EDGE_DISTANCE)
                    point.x += Constants.PLAYER_SPEED;
                else
                    moveRight = false;
            }

        }
    }

    private void checkClimbAndUpdate(boolean climb, boolean crouch, Point point) {
        if (climb && !jump && stopGravity && !crouch) {
            if (!climbShift) {
                if (chainNum % 2 != 0)
                    point.x -= Constants.SCREEN_WIDTH / 12;
                else
                    point.x += Constants.SCREEN_WIDTH / 12;
                climbShift = true;
                moveLeft = false;
                moveRight = false;
                chainNum++;
            }
        } else {
            climbShift = false;
        }
    }

    private void checkDeathJumpAndUpdate(Point point) {

        if (deadJump) {
            point.y -= jumpForce;
            jumpForce--;
        } else if (!stopGravity) {
            point.y += gravity;
            gravity++;
        }

        if (jumpForce == 0) {
            deadJump = false;
            stopGravity = false;
        }
    }

}