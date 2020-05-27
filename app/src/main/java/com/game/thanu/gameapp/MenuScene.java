package com.game.thanu.gameapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

class MenuScene implements Scene {

    private BackgroundManager backgroundManager;
    private ObstacleManager obstacleManager;
    private AnimationManager animManagerMenu;
    private Rect menuRectangle;
    private Paint paint;
    private static final int BLINK_DURATION = 650;
    private long lastUpdateTime = 0;
    private long blinkStart = 0;
    private boolean blink;
    private Rect r;

    MenuScene() {
        r = new Rect();
        backgroundManager = new BackgroundManager();
        obstacleManager = new ObstacleManager();
        Bitmap menu = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.menu);
        Animation menuPic = new Animation(new Bitmap[]{menu}, 100f);
        animManagerMenu = new AnimationManager(new Animation[]{menuPic});
        menuRectangle = new Rect(0,0,Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        Point c = new Point(Constants.SCREEN_WIDTH/2, Constants.SCREEN_HEIGHT/5);
        menuRectangle.set(c.x - menuRectangle.width() / 2, c.y - menuRectangle.height() / 2, c.x + menuRectangle.width() / 2, c.y + menuRectangle.height() / 2);
        paint = new Paint();
        paint.setTextSize(Constants.SCREEN_WIDTH/9);
        paint.setColor(Color.WHITE);
        paint.setFakeBoldText(true);
        blink = false;
    }

    @Override
    public void update() {
        backgroundManager.menuScreen();
        obstacleManager.menuScreen();
        animManagerMenu.playAnim(0);
        animManagerMenu.update();
        textUpdate();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.rgb(135,206,235));
        backgroundManager.draw(canvas);
        obstacleManager.draw(canvas);
        animManagerMenu.draw(canvas, menuRectangle);

        if (blink){
            drawCenterText(canvas, paint, "Tap Anywhere");
        }
    }

    @Override
    public void receiveTouch(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                    SceneManager.ACTIVE_SCENE = 1;
                break;
        }

    }

    private void textUpdate() {
        if (System.currentTimeMillis() - lastUpdateTime >= BLINK_DURATION
                && !blink) {
            blink = true;
            blinkStart = System.currentTimeMillis();
        }
        if (System.currentTimeMillis() - blinkStart >= 150 && blink) {
            blink = false;
            lastUpdateTime = System.currentTimeMillis();
        }

    }

    private void drawCenterText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom + (float)(Constants.SCREEN_WIDTH/2.7);
        canvas.drawText(text, x, y, paint);
    }
}
