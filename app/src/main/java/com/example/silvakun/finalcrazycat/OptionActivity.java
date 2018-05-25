package com.example.silvakun.finalcrazycat;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import java.io.IOException;

public class OptionActivity extends SimpleBaseGameActivity {

    private final int CAMERA_WIDTH=800;
    private final int CAMERA_HEIGHT=480;
    private final int CenterX=CAMERA_WIDTH/2;
    private  final  int CenterY=CAMERA_HEIGHT/2;
    private Camera mOptionCamera;

    private Scene OptionScene;
    private Sprite OptionSprite;

    private BitmapTextureAtlas myBackgroundBitMap;
    private TextureRegion myBackgroundRegion;
    private  String pathImagebackground="gfx/background/";
//    font
    public Font mFont;
    private Text Score;
    private String fontPath="gfx/font/";
    private Text txtOption;
    public Font fontmusicsound;
    private Text music;
    private Text sound;
//    music
    public Music audioGame;
    public String pathAudio="mfx/";
    private SharedPreferences.Editor VolumeEditor;
    private SharedPreferences sharedPreferences;

//    volume control
    private BitmapTextureAtlas myVolumeBitMap,myIconBitmap;
    private TextureRegion myVolumeRegion,myIconRegion;
    private String pathVolumeBar="gfx/control/";
    private Sprite volumeSprite_music,volumeSprite_sound,iconSprite_music,iconSprite_sound;


    @Override
    public EngineOptions onCreateEngineOptions() {
        sharedPreferences=getSharedPreferences("KEY_MUSIC",MODE_PRIVATE);
        VolumeEditor=sharedPreferences.edit();
        sharedPreferences=getSharedPreferences("KEY_SOUND",MODE_PRIVATE);
        VolumeEditor=sharedPreferences.edit();
        mOptionCamera=new Camera(0,0,CAMERA_WIDTH,CAMERA_HEIGHT);
        EngineOptions engineOptions=new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
                new FillResolutionPolicy(),mOptionCamera);
        engineOptions.getAudioOptions().setNeedsMusic(true);
        return engineOptions;
    }

    @Override
    protected void onCreateResources() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(pathImagebackground);
        myBackgroundBitMap=new BitmapTextureAtlas(this.getTextureManager(),800,604, TextureOptions.DEFAULT);
        myBackgroundRegion=BitmapTextureAtlasTextureRegionFactory.createFromAsset(myBackgroundBitMap,this,"backgroundOption.png",0,0);
        myBackgroundBitMap.load();
        onLoadControlVolume();
        onLoadFont();
        onLoadFontMusicSound();
        onLoadAutio();

    }

    private void onLoadControlVolume() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(pathVolumeBar);
        myVolumeBitMap=new BitmapTextureAtlas(this.getTextureManager(),512,50, TextureOptions.DEFAULT);
        myVolumeRegion=BitmapTextureAtlasTextureRegionFactory.createFromAsset(myVolumeBitMap,this,"volume.png",0,0);
        myVolumeBitMap.load();
        myIconBitmap=new BitmapTextureAtlas(this.getTextureManager(),64,64, TextureOptions.DEFAULT);
        myIconRegion=BitmapTextureAtlasTextureRegionFactory.createFromAsset(myIconBitmap,this,"icon.png",0,0);
        myIconBitmap.load();
    }

    private void onLoadFont() {
        FontFactory.setAssetBasePath(fontPath);
        this.mFont=FontFactory.createFromAsset(this.getFontManager(),this.getTextureManager(),CAMERA_WIDTH,CAMERA_HEIGHT,this.getAssets(),
                "Daddys.ttf",60,true, Color.BLACK);
        mFont.load();

    }
    private void onLoadFontMusicSound() {
        FontFactory.setAssetBasePath(fontPath);
        this.fontmusicsound=FontFactory.createFromAsset(this.getFontManager(),this.getTextureManager(),CAMERA_WIDTH,CAMERA_HEIGHT,this.getAssets(),
                "Daddys.ttf",40,true, Color.BLACK);
        fontmusicsound.load();

    }

    public void onLoadAutio() {
        MusicFactory.setAssetBasePath(this.pathAudio);
        try {
            audioGame=MusicFactory.createMusicFromAsset(mEngine.getMusicManager(),this,"Background.mp3");
            audioGame.setLooping(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected Scene onCreateScene() {
        OptionScene=new Scene();
        float getVolumeCurrent=sharedPreferences.getFloat("KEY_MUSIC",0);
        final float getSoundcurrent=sharedPreferences.getFloat("KEY_SOUND",0);
        OptionSprite=new Sprite(0,0,myBackgroundRegion,getVertexBufferObjectManager());
        txtOption=new Text(0,0,this.mFont,"Tùy Chọn",50,getVertexBufferObjectManager());
        music=new Text(0,0,this.fontmusicsound,"Âm nhạc",50,getVertexBufferObjectManager());
        sound=new Text(0,0,this.fontmusicsound,"Âm thanh",50,getVertexBufferObjectManager());
        volumeSprite_music=new Sprite(0,0,myVolumeRegion,getVertexBufferObjectManager()){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionMove()){
                    iconSprite_music.setPosition(pTouchAreaLocalX,iconSprite_music.getY());
                    getVolumeMusic();

                }
                return true;
            }
        };
        volumeSprite_sound=new Sprite(0,0,myVolumeRegion,getVertexBufferObjectManager()){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionMove()){
                    iconSprite_sound.setPosition(pTouchAreaLocalX,iconSprite_sound.getY());
                   getVolumeSound();
                }
                return true;
            }
        };
        txtOption.setPosition(CenterX-txtOption.getWidth()*0.5f,5);
        music.setPosition(10,txtOption.getY()+150);
        sound.setPosition(10,music.getY()+150);
        volumeSprite_music.setPosition(10,music.getY()+40);
        volumeSprite_sound.setPosition(10,sound.getY()+40);
        iconSprite_music=new Sprite(0,0,myIconRegion,getVertexBufferObjectManager());
        iconSprite_sound=new Sprite(0,0,myIconRegion,getVertexBufferObjectManager());
        iconSprite_music.setPosition(getVolumeCurrent*512,volumeSprite_music.getY()-4f);
        iconSprite_sound.setPosition(getSoundcurrent*512,volumeSprite_sound.getY()-4f);
        OptionScene.attachChild(OptionSprite);
        OptionScene.attachChild(txtOption);
        OptionScene.attachChild(music);
        OptionScene.attachChild(sound);
        OptionScene.attachChild(volumeSprite_music);
        OptionScene.attachChild(volumeSprite_sound);
        OptionScene.registerTouchArea(volumeSprite_music);
        OptionScene.registerTouchArea(volumeSprite_sound);
        OptionScene.attachChild(iconSprite_music);
        OptionScene.attachChild(iconSprite_sound);

        audioGame.setVolume(getVolumeCurrent);
        audioGame.play();
        return OptionScene;
    }

    public  void getVolumeMusic(){
        mEngine.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                float curentVolume=iconSprite_music.getX()/512f;
                audioGame.setVolume(curentVolume);
               try {
                   VolumeEditor.putFloat("KEY_MUSIC",curentVolume);
                   VolumeEditor.commit();
                   Log.v("Dagui","Đã gửi");
               }catch (Exception e){
                   Log.v("chuaguiduoc","Chua gửi được");
               }
            }

            @Override
            public void reset() {

            }
        });
    }
    public  void getVolumeSound(){
        mEngine.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                float curentVolume=iconSprite_sound.getX()/512f;
                try {
                    VolumeEditor.putFloat("KEY_SOUND",curentVolume);
                    VolumeEditor.commit();
                    Log.v("Dagui","Đã gửi");
                }catch (Exception e){
                    Log.v("chuaguiduoc","Chua gửi được");
                }
            }

            @Override
            public void reset() {

            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent myIntent=new Intent(OptionActivity.this,MenuGameActivity.class);
        startActivity(myIntent);
        finish();
        super.onBackPressed();
    }
}
