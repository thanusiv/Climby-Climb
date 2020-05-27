package com.game.thanu.gameapp;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends Activity {

    public static SaveData saveData;
    public static int highScore = 1;
    private AdView adView;
    public static InterstitialAd mInterstitialAd;
    public static boolean load = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        MobileAds.initialize(this, "ca-app-pub-5279834466210854~4429484195");

        // window manager preparation
        WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
        windowParams.gravity = Gravity.BOTTOM;
        windowParams.x = 0;
        windowParams.y = 0;
        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        windowParams.format = PixelFormat.TRANSLUCENT;
        windowParams.windowAnimations = 0;

        WindowManager wm = getWindowManager();

        adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        //this is test
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        //Below is mine
        //adView.setAdUnitId("ca-app-pub-5279834466210854/8203301579");
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        // Add adView to WindowManager
        wm.addView(adView, windowParams);

        loadInterstitialAd();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;
        Constants.SCREEN_RATIO = (double)(Constants.SCREEN_HEIGHT)/(double)(Constants.SCREEN_WIDTH);

        saveData = new SaveData(getApplicationContext());
        if (saveData.getPreferences().contains("HighScore"))
            highScore = saveData.getInt("HighScore");

        setGameMechanics();

        setContentView(new GamePanel(this, this));
    }

    @Override
    protected void onPause() {
        if (adView != null)
            adView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adView != null)
            adView.resume();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (adView != null)
            adView.destroy();
    }

    private void loadInterstitialAd() {
        mInterstitialAd = new InterstitialAd(this);
        //test
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        //my actual id
        //mInterstitialAd.setAdUnitId("ca-app-pub-5279834466210854/9185210755");
        mInterstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if(mInterstitialAd.isLoaded()){
                   load = true;
                }
            }

            @Override
            public void onAdClosed(){
                AdRequest adRequest = new AdRequest.Builder()
                        .build();
                mInterstitialAd.loadAd(adRequest);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }
        });

        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    private void setGameMechanics(){
        setPlayerSpeed();
        setObjectClimbSpeed();
        setGhostSpeed();
        setBeeSpeed();
        setSwipeLength();
        setEdgeDistance();
        setGroundSpaceDifference();
    }

    private void setPlayerSpeed(){
//        if (Constants.SCREEN_WIDTH == 768) {
//            Constants.PLAYER_SPEED = (int) Math.ceil((double) (Constants.SCREEN_WIDTH) / (double) 140);
//        } else if (Constants.SCREEN_WIDTH == 640) {
//            Constants.PLAYER_SPEED = (int) Math.ceil((double) (Constants.SCREEN_WIDTH) / (double) 150);
//        } else if (Constants.SCREEN_WIDTH == 720 && Constants.SCREEN_HEIGHT == 1184) {
//            Constants.PLAYER_SPEED = (int) Math.ceil((double) (Constants.SCREEN_WIDTH) / (double) 135);
//        } else if (Constants.SCREEN_WIDTH == 480 && Constants.SCREEN_HEIGHT >= 800) {
//            Constants.PLAYER_SPEED = (int) Math.ceil((double) (Constants.SCREEN_WIDTH) / (double) 150);
//        } else if (Constants.SCREEN_WIDTH < 1000 && Constants.SCREEN_WIDTH > 500) {
//            Constants.PLAYER_SPEED = (int) Math.ceil((double) (Constants.SCREEN_WIDTH) / (double) 145);
//        } else if (Constants.SCREEN_WIDTH > 1300){
//            Constants.PLAYER_SPEED = (int) Math.ceil((double) (Constants.SCREEN_WIDTH) / (double) 160);
//        } else {
//            Constants.PLAYER_SPEED = (int) Math.floor((double) (Constants.SCREEN_WIDTH) / (double) 154);
//        }
//
//        Constants.PLAYER_SPEED = (int)Math.ceil(0.00495944*Constants.SCREEN_WIDTH + 1.85674);

        double x = ((double)(Constants.SCREEN_WIDTH)/1080.0)*7;

        if (Constants.SCREEN_WIDTH < 1000)
            x++;

        double t = x;

        if (Math.abs(x - Math.floor(t)) < 0.55)
            Math.floor(x);
        else
            Math.ceil(x);

        Constants.PLAYER_SPEED = (int)x;
    }

    private void setObjectClimbSpeed(){ Constants.OBJECT_CLIMB_SPEED = (float)Math.ceil((double)Constants.SCREEN_HEIGHT / 448);}

    private void setGhostSpeed(){
        Constants.GHOST_SPEED = Constants.PLAYER_SPEED/3;
    }

    private void setBeeSpeed(){
        Constants.BEE_SPEED = Constants.PLAYER_SPEED/2;
    }

    private void setSwipeLength(){
        Constants.SWIPE_LENGTH = Constants.SCREEN_WIDTH/9;
    }

    private void setEdgeDistance(){
        Constants.EDGE_DISTANCE = Constants.SCREEN_WIDTH/33;
    }

    private void setGroundSpaceDifference(){Constants.GROUND_SPACE_DIFFERENCE = (int)((Constants.SCREEN_HEIGHT/1.8)*2);}
}
