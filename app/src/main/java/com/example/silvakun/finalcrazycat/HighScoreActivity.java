package com.example.silvakun.finalcrazycat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;

public class HighScoreActivity extends SimpleBaseGameActivity {

    private final int CAMERA_WIDTH=800;
    private final int CAMERA_HEIGHT=480;
    private final int CenterX=CAMERA_WIDTH/2;
    private  final  int CenterY=CAMERA_HEIGHT/2;
    private Camera mHighScoreCamera;

    private Scene HighScoreScene;
    private Sprite HighScoreSprite;

    private BitmapTextureAtlas myBackgroundBitMap;
    private TextureRegion myBackgroundRegion;
    private  String pathImagebackground="gfx/background/";

    public Font mFont;
    private String fontPath="gfx/font/";
    private Text txtHighScore;

    public Font mFontHighScore;
    private Text Name;
    private Text Score;


//    HightScore
    private Text getScore;
    private Text getName;
    private  Font myFont;
    private SharedPreferences myScoreShared;
    private SharedPreferences.Editor editMyScoreShared;
    private SharedPreferences myNameShared;
    private SharedPreferences.Editor editMyNameShared;




    @Override
    public EngineOptions onCreateEngineOptions() {
        myScoreShared=getSharedPreferences("KEY_SCORE",MODE_PRIVATE);
        editMyScoreShared=myScoreShared.edit();
        myNameShared=getSharedPreferences("KEY_NAME",this.MODE_PRIVATE);
        editMyNameShared=myScoreShared.edit();
        mHighScoreCamera=new Camera(0,0,CAMERA_WIDTH,CAMERA_HEIGHT);
        EngineOptions engineOptions=new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
                new FillResolutionPolicy(),mHighScoreCamera);
        engineOptions.getAudioOptions().setNeedsMusic(true);
        return engineOptions;
    }

    @Override
    protected void onCreateResources() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(pathImagebackground);
        myBackgroundBitMap=new BitmapTextureAtlas(this.getTextureManager(),800,604, TextureOptions.DEFAULT);
        myBackgroundRegion=BitmapTextureAtlasTextureRegionFactory.createFromAsset(myBackgroundBitMap,this,"backgroundOption.png",0,0);
        myBackgroundBitMap.load();
        onLoadFont();
        onLoadHighScore();
        onLoadFontName();

    }

    private void onLoadFontName() {
        FontFactory.setAssetBasePath(fontPath);
        this.myFont=FontFactory.createFromAsset(this.getFontManager(),this.getTextureManager(),CAMERA_WIDTH,CAMERA_HEIGHT,this.getAssets(),
                "Daddys.ttf",35,true, Color.BLACK);
        myFont.load();
    }

    private void onLoadFont() {
        FontFactory.setAssetBasePath(fontPath);
        this.mFont=FontFactory.createFromAsset(this.getFontManager(),this.getTextureManager(),CAMERA_WIDTH,CAMERA_HEIGHT,this.getAssets(),
                "Daddys.ttf",100,true, Color.BLACK);
        mFont.load();

    }
    private void onLoadHighScore() {
        FontFactory.setAssetBasePath(fontPath);
        this.mFontHighScore=FontFactory.createFromAsset(this.getFontManager(),this.getTextureManager(),CAMERA_WIDTH,CAMERA_HEIGHT,this.getAssets(),
                "Daddys.ttf",40,true, Color.BLACK);
        mFontHighScore.load();

    }
    @Override
    protected Scene onCreateScene() {
        HighScoreScene=new Scene();
        HighScoreSprite=new Sprite(0,0,myBackgroundRegion,getVertexBufferObjectManager());
        txtHighScore=new Text(0,0,this.mFont,"Điểm Cao",50,getVertexBufferObjectManager());
        txtHighScore.setPosition(CenterX-txtHighScore.getWidth()*0.5f,5);
        Name=new Text(0,0,this.mFontHighScore,"Tên người chơi",50,getVertexBufferObjectManager());
        Score=new Text(0,0,this.mFontHighScore,"Điểm",50,getVertexBufferObjectManager());
        Name.setPosition(100,txtHighScore.getY()+150);
        Score.setPosition(Name.getX()+CenterX,txtHighScore.getY()+150);
        HighScoreScene.attachChild(HighScoreSprite);
        HighScoreScene.attachChild(txtHighScore);
        HighScoreScene.attachChild(Name);
        HighScoreScene.attachChild(Score);
        getScore=new Text(Score.getX(),Score.getY()+50,myFont,"",50,getVertexBufferObjectManager());
        getName=new Text(100,Name.getY()+50,myFont,"",50,getVertexBufferObjectManager());
        int s= myScoreShared.getInt("KEY_SCORE",0);
        String s1=myNameShared.getString("KEY_NAME","Erik");

        if(s<myNameShared.getInt("KEY_SCORE",0)){
            s=s;
        }
        else {
            getScore.setText(String.valueOf(myScoreShared.getInt("KEY_SCORE",0)));
        }

        getName.setText(s1);
        HighScoreScene.attachChild(getName);
        HighScoreScene.attachChild(getScore);
        return HighScoreScene;
    }
    @Override
    public void onBackPressed() {
        Intent myIntent=new Intent(HighScoreActivity.this,MenuGameActivity.class);
        startActivity(myIntent);
        finish();
        super.onBackPressed();
    }
}
