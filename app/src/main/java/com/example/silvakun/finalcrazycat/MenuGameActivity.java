package com.example.silvakun.finalcrazycat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import java.io.IOException;

public class MenuGameActivity extends SimpleBaseGameActivity {
    public static final int CAMERA_WIDTH=800;
    public static final int CAMERA_HEIGHT=480;
    public Camera mCAMERA;
    final Scene mScene=new Scene();


    private BitmapTextureAtlas BackgroundbitmapTextureAtlas;
    private TextureRegion BackgroundtextureRegion;
    private String pathBackground="gfx/background/backgroundOption.png";

    private BitmapTextureAtlas btnPlayBitMap,btnOptionBitMap,btnHighscoreBitMap,btnHelpBitMap,btnExitBitMap;
    private TextureRegion btnPlayRegion,btnOptionRegion,btnHighscoreRegion,btnHelpRegion,btnExitRegion;
    private Sprite btnPlaySprite,btnOptionSprite,btnHighscoreSprite,btnExitSprite,btnHelpSprite;
    private String pathButton="gfx/button/";

    private BitmapTextureAtlas nameGameBitMap;
    private TextureRegion nameGameRegion;
    private String nameGamePath="gfx/item/";
    public  Sprite nameGameSprite;

    private final int CenterX=CAMERA_WIDTH/2;
    private  final int CenterY=CAMERA_HEIGHT/2;

    public Music audioMenu;
    public String pathAudio="mfx/";


    private BitmapTextureAtlas btnPauseMusicBitMap,btnPlayMusicBitMap;
    private TextureRegion btnPauseMusicRegion,btnPlayMusicRegion;
    private String pathButtonMusic="gfx/button/";
    private Sprite btnPlayMusicSprite;
    private Sprite btnPauseMusicSprite;
    private SharedPreferences mySheraedSound;

    @Override
    public EngineOptions onCreateEngineOptions() {
        mySheraedSound=getSharedPreferences("KEY_SOUND",MODE_PRIVATE);
        mCAMERA=new Camera(0,0,CAMERA_WIDTH,CAMERA_HEIGHT);
        EngineOptions engineOptions=new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,new
                FillResolutionPolicy(),mCAMERA);
        engineOptions.getAudioOptions().setNeedsMusic(true);
        return  engineOptions;

    }

    @Override
    protected void onCreateResources() {
        onLoadAudio();
        onLoadBackground();
        onLoadNameGame();
        onLoadButton();
        onLoadButtonPlayMussic();

    }

    public void onLoadAudio() {
        MusicFactory.setAssetBasePath(this.pathAudio);
        try {
            audioMenu=MusicFactory.createMusicFromAsset(mEngine.getMusicManager(),this,"Background.mp3");
            audioMenu.setLooping(true);
            float myVolume=mySheraedSound.getFloat("KEY_MUSIC",0);
            audioMenu.setVolume(myVolume);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onLoadButton() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(this.pathButton);
        btnPlayBitMap=new BitmapTextureAtlas(this.getTextureManager(),256,128,TextureOptions.DEFAULT);
        btnPlayRegion=BitmapTextureAtlasTextureRegionFactory.createFromAsset(btnPlayBitMap,this,"Play.png",0,0);

        btnOptionBitMap=new BitmapTextureAtlas(this.getTextureManager(),256,128,TextureOptions.DEFAULT);
        btnOptionRegion=BitmapTextureAtlasTextureRegionFactory.createFromAsset(btnOptionBitMap,this,"Option.png",0,0);

        btnHighscoreBitMap=new BitmapTextureAtlas(this.getTextureManager(),256,128,TextureOptions.DEFAULT);
        btnHighscoreRegion=BitmapTextureAtlasTextureRegionFactory.createFromAsset(btnHighscoreBitMap,this,"Highscore.png",0,0);

        btnExitBitMap=new BitmapTextureAtlas(this.getTextureManager(),256,128,TextureOptions.DEFAULT);
        btnExitRegion=BitmapTextureAtlasTextureRegionFactory.createFromAsset(btnExitBitMap,this,"Quit.png",0,0);

        btnHelpBitMap=new BitmapTextureAtlas(this.getTextureManager(),256,128,TextureOptions.DEFAULT);
        btnHelpRegion=BitmapTextureAtlasTextureRegionFactory.createFromAsset(btnHelpBitMap,this,"Help.png",0,0);
        btnPlayBitMap.load();
        btnOptionBitMap.load();
        btnHighscoreBitMap.load();
        btnExitBitMap.load();
        btnHelpBitMap.load();



    }

    private void onLoadNameGame() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(nameGamePath);
        nameGameBitMap=new BitmapTextureAtlas(this.getTextureManager(),512,256,TextureOptions.DEFAULT);
        nameGameRegion=BitmapTextureAtlasTextureRegionFactory.createFromAsset(nameGameBitMap,this,"Name.png",0,0);
        nameGameBitMap.load();

    }

    private void onLoadBackground() {
        BackgroundbitmapTextureAtlas=new BitmapTextureAtlas(this.getTextureManager(),800,604, TextureOptions.DEFAULT);
        BackgroundtextureRegion= BitmapTextureAtlasTextureRegionFactory.createFromAsset(BackgroundbitmapTextureAtlas,this,this.pathBackground,0,0);
        BackgroundbitmapTextureAtlas.load();
    }
    private void onLoadButtonPlayMussic() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(pathButtonMusic);
        btnPlayMusicBitMap=new BitmapTextureAtlas(this.getTextureManager(),128,128,TextureOptions.DEFAULT);
        btnPlayMusicRegion=BitmapTextureAtlasTextureRegionFactory.createFromAsset(btnPlayMusicBitMap,this,"playmusic.png",0,0);
        btnPlayMusicBitMap.load();
        btnPauseMusicBitMap=new BitmapTextureAtlas(this.getTextureManager(),128,128,TextureOptions.DEFAULT);
        btnPauseMusicRegion=BitmapTextureAtlasTextureRegionFactory.createFromAsset(btnPauseMusicBitMap,this,"pausemusic.png",0,0);
        btnPauseMusicBitMap.load();

    }

    @Override
    protected Scene onCreateScene() {
        mEngine.registerUpdateHandler(new FPSLogger());
        this.audioMenu.play();
        Sprite BackgroundSprite=new Sprite(0,0,BackgroundtextureRegion,getVertexBufferObjectManager());
        btnPlaySprite=new Sprite(0,0,btnPlayRegion,getVertexBufferObjectManager()){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionDown()){
                    btnPlaySprite.setColor(org.andengine.util.color.Color.RED);
                }
                if(pSceneTouchEvent.isActionUp()){
                    Intent myIntent=new Intent(MenuGameActivity.this,GameActivity.class);
                    startActivity(myIntent);
                    finish();

                }
                return true;
            }
        };
        btnPlaySprite.setPosition(CAMERA_WIDTH*0.03f,CAMERA_HEIGHT*0.01f);

        btnOptionSprite=new Sprite(0,0,btnOptionRegion,getVertexBufferObjectManager()){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionDown()){
                    btnOptionSprite.setColor(org.andengine.util.color.Color.RED);
                }
                if(pSceneTouchEvent.isActionUp()){
                    Intent myIntent=new Intent(MenuGameActivity.this,OptionActivity.class);
                    startActivity(myIntent);
                    finish();
                    audioMenu.pause();
                }
                return true;
            }
        };
        btnOptionSprite.setPosition(btnPlaySprite.getX()+btnOptionSprite.getWidth()*0.5f,CenterY-(btnOptionSprite.getHeight()*1.3f));

        btnHighscoreSprite=new Sprite(0,0,btnHighscoreRegion,getVertexBufferObjectManager()){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionDown()){
                    btnHighscoreSprite.setColor(org.andengine.util.color.Color.RED);
                }
                if(pSceneTouchEvent.isActionUp()){
                    Intent myIntent=new Intent(MenuGameActivity.this,HighScoreActivity.class);
                    startActivity(myIntent);
                    finish();
                }
                return true;
            }
        };
        btnHighscoreSprite.setPosition(btnOptionSprite.getX()+btnHighscoreSprite.getWidth()*0.5f,CAMERA_HEIGHT*0.001f);

        btnHelpSprite=new Sprite(0,0,btnHelpRegion,getVertexBufferObjectManager()){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionDown()){
                    btnHelpSprite.setColor(org.andengine.util.color.Color.RED);
                }
                if(pSceneTouchEvent.isActionUp()){
                    Intent myIntent=new Intent(MenuGameActivity.this,HelpActivity.class);
                    startActivity(myIntent);
                    finish();
                }
                return true;
            }
        };
        btnHelpSprite.setPosition(btnHighscoreSprite.getX()+btnHelpSprite.getWidth()*0.5f,CenterY-(btnOptionSprite.getHeight()*1.3f));

        btnExitSprite=new Sprite(0,0,btnExitRegion,getVertexBufferObjectManager()){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionDown()){
                    btnExitSprite.setColor(org.andengine.util.color.Color.RED);
                }
                if(pSceneTouchEvent.isActionUp()){
                    finish();

                }
                return true;
            }
        };
        btnExitSprite.setPosition(btnHelpSprite.getX()+btnExitSprite.getWidth()*0.5f,CAMERA_HEIGHT*0.01f);
        nameGameSprite =new Sprite(0,0,nameGameRegion,getVertexBufferObjectManager());
        nameGameSprite.setPosition(CenterX-nameGameSprite.getWidth()*0.5f,CAMERA_HEIGHT-nameGameSprite.getHeight()+20);
        btnPlayMusicSprite=new Sprite(0,0,btnPlayMusicRegion,getVertexBufferObjectManager()){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionDown()){
                    onPausePlayMusic();
                }
                return true;
            }
        };
        btnPlayMusicSprite.setPosition(btnPlayMusicSprite.getX()+5,CAMERA_HEIGHT-btnPlayMusicSprite.getHeight()-5);
        mScene.attachChild(BackgroundSprite);
        mScene.attachChild(nameGameSprite);
        mScene.attachChild(btnPlaySprite);
        mScene.attachChild(btnOptionSprite);
        mScene.attachChild(btnHighscoreSprite);
        mScene.attachChild(btnExitSprite);
        mScene.attachChild(btnHelpSprite);
        mScene.registerTouchArea(btnPlaySprite);
        mScene.registerTouchArea(btnOptionSprite);
        mScene.registerTouchArea(btnHighscoreSprite);
        mScene.registerTouchArea(btnExitSprite);
        mScene.registerTouchArea(btnHelpSprite);
        mScene.attachChild(btnPlayMusicSprite);
        mScene.registerTouchArea(btnPlayMusicSprite);
        return mScene;
    }
    public void onPausePlayMusic(){
        if(audioMenu.isPlaying()){
            audioMenu.pause();
            btnPauseMusicSprite=new Sprite(btnPlayMusicSprite.getX(),btnPlayMusicSprite.getY(),btnPauseMusicRegion,getVertexBufferObjectManager());
            mScene.attachChild(btnPauseMusicSprite);
            mScene.detachChild(btnPlayMusicSprite);
            mScene.registerTouchArea(btnPauseMusicSprite);
        }
        else {
            audioMenu.resume();
            mScene.attachChild(btnPlayMusicSprite);
            mScene.detachChild(btnPauseMusicSprite);

        }
    }

    @Override
    public void onBackPressed() {
        Intent myIntent=new Intent(MenuGameActivity.this,SplashScreenActivity.class);
        startActivity(myIntent);
        finish();
        super.onBackPressed();
    }
}
