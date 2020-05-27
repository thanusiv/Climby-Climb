package com.game.thanu.gameapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import static com.game.thanu.gameapp.MainThread.canvas;
import static com.game.thanu.gameapp.ScoresHandler.currentScore;

class EndScreen {

    private AnimationManager animManagerEnd;
    private Rect endRectangle;
    private Rect r;
    private Paint endPaint;

    EndScreen(){
        r = new Rect();
        Bitmap endScreen = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.endscreen);
        Animation endPic = new Animation(new Bitmap[]{endScreen}, 0.3f);
        animManagerEnd = new AnimationManager(new Animation[]{endPic});
        endRectangle = new Rect(0,0,Constants.SCREEN_WIDTH + 20, Constants.SCREEN_HEIGHT);
        endPaint = new Paint();
        endPaint.setTextSize(Constants.SCREEN_HEIGHT/13);
        endPaint.setColor(Color.rgb(255,255,255));
    }

    public void update(){
        animManagerEnd.playAnim(0);
        animManagerEnd.update();
    }

    public void draw(){
        animManagerEnd.draw(canvas, endRectangle);
        drawEndScreen(canvas,endPaint, "Your Score",Constants.SCREEN_HEIGHT/6);
        drawEndScreen(canvas,endPaint, Integer.toString(currentScore),(int)(Constants.SCREEN_HEIGHT/3.4));
        drawEndScreen(canvas,endPaint, "High Score",(int)(Constants.SCREEN_HEIGHT/2.4));
        drawEndScreen(canvas,endPaint, Integer.toString(MainActivity.highScore),(int)(Constants.SCREEN_HEIGHT/1.8));
        drawEndScreen(canvas,endPaint, "Tap To",(int)(Constants.SCREEN_HEIGHT/1.44));
        drawEndScreen(canvas,endPaint, "Play Again",(int)(Constants.SCREEN_HEIGHT/1.28));
        if (4500 - Math.ceil(System.currentTimeMillis() - GameplayScene.gameOverTime) > 1000)
            drawEndScreen(canvas,endPaint, "(" + (Integer.toString(4500 - ((int) Math.ceil(System.currentTimeMillis() - GameplayScene.gameOverTime))).substring(0,1)) + ")",(int)(Constants.SCREEN_HEIGHT/1.14));
    }

    private void drawEndScreen(Canvas canvas, Paint paint, String text, int yValue) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = endRectangle.top + yValue;
        canvas.drawText(text, x, y, paint);
    }

}
