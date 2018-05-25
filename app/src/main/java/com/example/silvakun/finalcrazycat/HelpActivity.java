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
import org.andengine.opengl.vbo.DrawType;
import org.andengine.ui.activity.SimpleBaseGameActivity;

public class HelpActivity extends SimpleBaseGameActivity  {
    public static int CAMERA_WIDTH=800;
    public static int CAMERA_HEIGHT=480;

    //   vị trí giữa màn hình
    private final int CenterX=CAMERA_WIDTH/2;
    private  final int CenterY=CAMERA_HEIGHT/2;
    // Khai báo màn hình Scene,Sprite cho  background và đường dẫn cho logo background.
    private Scene mScene;
    private Sprite HelpScreenSprite;
    private String pathImagebackground="gfx/background/";
    //    Khai báo biến Bitmap để quản lý nguồn hình ảnh.
    private BitmapTextureAtlas BackgroungbitmapTextureAtlas;
    private TextureRegion BackgroundtextureRegion;

    //    Khai báo font sử dụng
    public Font mFont;
    private Text helpText;
    private Font newFont;
    private String fontPath="gfx/font/";


//    load button
    private BitmapTextureAtlas leftArrow,rightArrow,jump;
    private TextureRegion mTextureLeftArrow,mTextutrRightArrow,mTextureJump;
    private  String path="gfx/control/";

    // Khởi tạo AndeEngine với kích thước màn hình 800 x 480 và không có âm thanh.
    @Override
    public EngineOptions onCreateEngineOptions() {
        final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        EngineOptions engineOptions=new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
                new FillResolutionPolicy(), camera);
        engineOptions.getAudioOptions().setNeedsMusic(true);
        return engineOptions;
    }
    // Hàm load nguồn hình ảnh ,âm thanh,font,...
    @Override
    protected void onCreateResources() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(this.pathImagebackground);
        BackgroungbitmapTextureAtlas=new BitmapTextureAtlas(this.getTextureManager(),800,604, TextureOptions.DEFAULT);
        BackgroundtextureRegion=BitmapTextureAtlasTextureRegionFactory.createFromAsset(BackgroungbitmapTextureAtlas,this,"backgroundOption.png",0,0);
        BackgroungbitmapTextureAtlas.load();
        onLoadFont();
        onLoadNewFont();
        onLoadButton();


    }

    private void onLoadButton() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(path);
        leftArrow=new BitmapTextureAtlas(this.getTextureManager(),64,64,TextureOptions.BILINEAR);
        rightArrow=new BitmapTextureAtlas(this.getTextureManager(),64,64,TextureOptions.BILINEAR);
        jump=new BitmapTextureAtlas(this.getTextureManager(),72,72,TextureOptions.BILINEAR);
        mTextureLeftArrow=BitmapTextureAtlasTextureRegionFactory.createFromAsset(leftArrow,this,"_leftarrow.png",0,0);
        mTextutrRightArrow=BitmapTextureAtlasTextureRegionFactory.createFromAsset(rightArrow,this,"_rightarrow.png",0,0);
        mTextureJump=BitmapTextureAtlasTextureRegionFactory.createFromAsset(jump,this,"Jump_Cat.png",0,0);
        leftArrow.load();
        rightArrow.load();
        jump.load();
        }

    private void onLoadFont() {
        FontFactory.setAssetBasePath(fontPath);
        this.mFont=FontFactory.createFromAsset(this.getFontManager(),this.getTextureManager(),CAMERA_WIDTH,CAMERA_HEIGHT,this.getAssets(),
                "Daddys.ttf",80,true, Color.BLACK);
        mFont.load();

    }
    private void onLoadNewFont() {
        FontFactory.setAssetBasePath(fontPath);
        this.newFont=FontFactory.createFromAsset(this.getFontManager(),this.getTextureManager(),CAMERA_WIDTH,CAMERA_HEIGHT,this.getAssets(),
                "Daddys.ttf",40,true, Color.BLACK);
        newFont.load();

    }
    @Override
    protected Scene onCreateScene() {
        mEngine.registerUpdateHandler(new FPSLogger());
        this.mScene=new Scene();
        HelpScreenSprite=new Sprite(0,0,BackgroundtextureRegion,getVertexBufferObjectManager());
        this.helpText=new Text(5,5,mFont,"Trợ giúp: \n",50,getVertexBufferObjectManager());
        helpText.setPosition(CenterX-helpText.getWidth()*0.5f,5);
        mScene.attachChild(HelpScreenSprite);
        Sprite leftArrowSprite=new Sprite(0,0,mTextureLeftArrow,getVertexBufferObjectManager());
        Sprite rightArrowSprite=new Sprite(0,0,mTextutrRightArrow,getVertexBufferObjectManager());
        Sprite jumpSprite=new Sprite(0,0,mTextureJump,getVertexBufferObjectManager());
        leftArrowSprite.setPosition(10,helpText.getY()+100);
        Text txtLeft=new Text(leftArrowSprite.getX()+100,leftArrowSprite.getY()+20,newFont,"Nút di chuyển trái",getVertexBufferObjectManager());
        rightArrowSprite.setPosition(10,leftArrowSprite.getY()+100);
        Text txtRight=new Text(rightArrowSprite.getX()+100,rightArrowSprite.getY()+20,newFont,"Nút di chuyển phải",getVertexBufferObjectManager());
        jumpSprite.setPosition(10,rightArrowSprite.getY()+100);
        Text txtJump=new Text(jumpSprite.getX()+100,jumpSprite.getY()+20,newFont,"Nút Nhảy",getVertexBufferObjectManager());
        mScene.attachChild(leftArrowSprite);
        mScene.attachChild(rightArrowSprite);
        mScene.attachChild(jumpSprite);
        mScene.attachChild(helpText);
        mScene.attachChild(txtLeft);
        mScene.attachChild(txtRight);
        mScene.attachChild(txtJump);
        Text BackSprite=new Text(0,0,newFont,"Nhấn nút trở về để thoát !",getVertexBufferObjectManager());
        BackSprite.setPosition(CenterX-BackSprite.getWidth()*0.5f,CAMERA_HEIGHT-BackSprite.getHeight()-5);
        mScene.attachChild(BackSprite);
        return mScene;
    }
    @Override
    public void onBackPressed() {
        Intent myIntent=new Intent(HelpActivity.this,MenuGameActivity.class);
        startActivity(myIntent);
        finish();
        super.onBackPressed();
    }
}
