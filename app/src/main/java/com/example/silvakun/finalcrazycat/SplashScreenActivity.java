package com.example.silvakun.finalcrazycat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
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

public class SplashScreenActivity extends SimpleBaseGameActivity implements IOnSceneTouchListener {

    public static int CAMERA_WIDTH=800;
    public static int CAMERA_HEIGHT=480;

    private final int CenterX=CAMERA_WIDTH/2;
    private  final int CenterY=CAMERA_HEIGHT/2;
// Khai báo màn hình Scene,Sprite cho logo background và đường dẫn cho logo background.
    private Scene mScene;
    private Sprite msplasScreenSprite;
    private String pathImagebackground="gfx/";
//    Khai báo biến Bitmap để quản lý nguồn hình ảnh.
    private BitmapTextureAtlas BackgroungbitmapTextureAtlas;
    private TextureRegion BackgroundtextureRegion;

// Khởi tạo AndeEngine với kích thước màn hình 800 x 480 và không có âm thanh.
    @Override
    public EngineOptions onCreateEngineOptions() {
        final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
                new FillResolutionPolicy(), camera);
    }
// Hàm load nguồn hình ảnh ,âm thanh,font,...
    @Override
    protected void onCreateResources() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(this.pathImagebackground);
        BackgroungbitmapTextureAtlas=new BitmapTextureAtlas(this.getTextureManager(),512,512, TextureOptions.DEFAULT);
        BackgroundtextureRegion=BitmapTextureAtlasTextureRegionFactory.createFromAsset(BackgroungbitmapTextureAtlas,this,"AndEngine.png",0,0);
        BackgroungbitmapTextureAtlas.load();
    }
    @Override
    protected Scene onCreateScene() {
        mEngine.registerUpdateHandler(new FPSLogger());
        this.mScene=new Scene();
        mScene.setBackground(new Background(50,50,50));
        msplasScreenSprite=new Sprite(0,0,BackgroundtextureRegion,getVertexBufferObjectManager());
        msplasScreenSprite.setPosition((CAMERA_WIDTH - msplasScreenSprite.getWidth())*0.5f,(CAMERA_HEIGHT - msplasScreenSprite.getHeight())*0.5f);
        msplasScreenSprite.registerEntityModifier(new SequenceEntityModifier(new ScaleModifier(3,0.5f,1.0f)));
        mScene.attachChild(msplasScreenSprite);

        mScene.setOnSceneTouchListener(this);
        return mScene;
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        if(pSceneTouchEvent.isActionDown()){
            msplasScreenSprite.registerEntityModifier(new SequenceEntityModifier(new AlphaModifier(5,0,1.5f)));
        }
        if(pSceneTouchEvent.isActionUp()){

            Intent myIntent=new Intent(SplashScreenActivity.this,MenuGameActivity.class);
            startActivity(myIntent);
            finish();
        }
        return true;
    }
}
