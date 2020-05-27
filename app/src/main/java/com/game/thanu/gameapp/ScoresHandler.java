package com.game.thanu.gameapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

import static com.game.thanu.gameapp.GroundsHandler.grounds;
import static com.game.thanu.gameapp.MainActivity.saveData;

class ScoresHandler implements BackgroundObjectHandler {

    private ArrayList<Integer> scores;
    private Paint paintScore;
    private Rect r;
    private int chainIndex;
    static int currentScore;

    ScoresHandler(int chainIndex){
        r = new Rect();
        scores = new ArrayList<>();
        paintScore = new Paint();
        paintScore.setTextSize(Constants.SCREEN_WIDTH/2);
        paintScore.setColor(Color.rgb(176,224,230));
        this.chainIndex = chainIndex;
        currentScore = 1;
    }

    @Override
    public void addInitialObjects() {
        scores.add(3);
        scores.add(2);
        scores.add(1);
    }

    @Override
    public void moveObjectsDown() {}

    @Override
    public void adjustObjectsDown() {}

    @Override
    public void adjustObjectsUp() {}

    @Override
    public void addNewObjectAndRemoveOldObject() {
        scores.remove(scores.size() - 1);
        scores.add(0, chainIndex);
        chainIndex++;
    }

    @Override
    public void playObjectsAnimation() {}

    @Override
    public void drawObjects(Canvas canvas) {
        for (int i = 0; i < scores.size(); i++)
            drawScore(canvas, paintScore, Integer.toString(scores.get(i)), grounds.get(i).getRectangle().top);
    }

    @Override
    public boolean collideWithObjects(Player player) {
        return false;
    }

    @Override
    public void menuScreenUpdate() {
        if (GroundsHandler.removedGroundMenu)
            addNewObjectAndRemoveOldObject();
    }

    void adjustCurrentScore(){
        currentScore = scores.get(scores.size() - 1);
        if (currentScore >= MainActivity.highScore){
            saveData.clear();
            saveData.putInt("HighScore", currentScore);
            MainActivity.highScore = currentScore;
        }
    }

    private void drawScore(Canvas canvas, Paint paint, String text, int yValue) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = (float)(yValue - Constants.SCREEN_HEIGHT/6.5);
        canvas.drawText(text, x, y, paint);
    }
}
