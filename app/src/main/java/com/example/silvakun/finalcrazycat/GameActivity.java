package com.example.silvakun.finalcrazycat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXObject;
import org.andengine.extension.tmx.TMXObjectGroup;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;


public class GameActivity extends SimpleBaseGameActivity {
//  Khởi tạo giá trị cho camera
    private static final int CAMERA_WIDTH=800;
    private static final  int CAMERA_HEIGHT=480;

// Khởi tạo Camera và Scene.
    private BoundCamera mCAMERA;
    private Scene mScene;

// Lấy vị trí chính giữa của Camera.
    private final float CenterX=CAMERA_WIDTH*0.5f;
    private final  float CenterY=CAMERA_HEIGHT*0.5f;

    private TMXTiledMap mTMXTiledMap;
    private TMXLayer mVatCanTMXLayer;
    private String path="mapgamelevel1.tmx";

//buttons
    private BitmapTextureAtlas myButtonLeftBitMap,myButtonRightBitMap,myButtonJumpBitMap;
    private TextureRegion myButtonLeftRegion,myButtonRightRegion,myButtonJumpRegion;
    private String pathButton="gfx/control/";
//    mèo
    private BitmapTextureAtlas MeoAniamtionBitmap;
    private TiledTextureRegion MeoTiledAnimation;
    private String pathMeo="gfx/player/";
    private Meo meo;
    private boolean statusCat=false;
//    quản lý môi trường vật lý
    private FixtureDef mFixtureDef = PhysicsFactory.createFixtureDef(1f,-10f, 0.5f);
    private FixtureDef wallfixture = PhysicsFactory.createFixtureDef(0f,0f, 0f);
    private Body bodyMeo;
    public PhysicsWorld mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH),false);

    //    font
    public Font mFont;
    private Text Score;
    private String fontPath="gfx/font/";
    private Text Timer;
    private int myScore=0;
    private	static 	int	mTextXPos = 30;
    private	static 	int	mTextYPos = 5;
//chuot
    private LinkedList<Sprite> chuotArray;
    private BitmapTextureAtlas chuotBitMap;
    private TextureRegion chuotTextureRegion;
    private boolean trangthaichuot;

// dongho
    private LinkedList<Sprite> donghoArray;
    private BitmapTextureAtlas donghoBitMap;
    private TextureRegion donghoRegion;

//    nhac
    private Music amVaCham;
    private Music amDongho;
    private String pathAudio="mfx/";
    private int Score_nextlever;
    private int time=40;
    private SharedPreferences myScoreShared;
    private SharedPreferences.Editor editMyScoreShared;
    private SharedPreferences myNameShared;
    private SharedPreferences.Editor editMyNameShared;
    private SharedPreferences mySheraedSound;
    @Override
    public EngineOptions onCreateEngineOptions() {
        myScoreShared=getSharedPreferences("KEY_SCORE",MODE_PRIVATE);
        editMyScoreShared=myScoreShared.edit();
        myNameShared=getSharedPreferences("KEY_NAME",MODE_PRIVATE);
        editMyNameShared=myNameShared.edit();
        mySheraedSound=getSharedPreferences("KEY_SOUND",MODE_PRIVATE);
        mCAMERA=new BoundCamera(0,0,CAMERA_WIDTH,CAMERA_HEIGHT);
        EngineOptions engineOptions=new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,new FillResolutionPolicy(),mCAMERA);
        engineOptions.getAudioOptions().setNeedsMusic(true);
        engineOptions.getTouchOptions().setNeedsMultiTouch(true);
        if(MultiTouch.isSupported(this)) {
            if(MultiTouch.isSupportedDistinct(this)) {
                Toast.makeText(this, "MultiTouch detected --> Both controls will work properly!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "MultiTouch detected, but your device has problems distinguishing between fingers.\n\nControls are placed at different vertical locations.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Sorry your device does NOT support MultiTouch!\n\n(Falling back to SingleTouch.)\n\nControls are placed at different vertical locations.", Toast.LENGTH_LONG).show();
        }
        return engineOptions;
    }

    @Override
    protected void onCreateResources() {
        onLoadButton();
        onLoadMeo();
        onLoadFont();
        onLoadChuot();
        onLoadDongHo();
        onLoadAudio();
        mTMXTiledMap=loadBanDo.getTMXTiledMap(mScene,mEngine,this,path,this,getVertexBufferObjectManager());
    }
    public void onLoadAudio() {
        MusicFactory.setAssetBasePath(this.pathAudio);
        try {
            amVaCham=MusicFactory.createMusicFromAsset(mEngine.getMusicManager(),this,"onattak.mp3");
            amDongho=MusicFactory.createMusicFromAsset(mEngine.getMusicManager(),this,"clock.mp3");
            amVaCham.setLooping(false);
            amDongho.setLooping(false);
            float myVolume=mySheraedSound.getFloat("KEY_SOUND",0);
            amDongho.setVolume(myVolume);
            amVaCham.setVolume(myVolume);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void onLoadMeo() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(pathMeo);
        MeoAniamtionBitmap=new BitmapTextureAtlas(this.getTextureManager(),512,96,TextureOptions.BILINEAR);
        MeoTiledAnimation=BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(MeoAniamtionBitmap,this,"Meo.png",0,0,3,1);
        MeoAniamtionBitmap.load();

    }
    private void onLoadChuot() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/player/");
        chuotBitMap=new BitmapTextureAtlas(this.getTextureManager(),64,64,TextureOptions.BILINEAR);
        chuotTextureRegion=BitmapTextureAtlasTextureRegionFactory.createFromAsset(chuotBitMap,this,"ChuotMi.png",0,0);
        chuotBitMap.load();

    }
    private void onLoadDongHo() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/item/");
        donghoBitMap=new BitmapTextureAtlas(this.getTextureManager(),32,32,TextureOptions.BILINEAR);
        donghoRegion=BitmapTextureAtlasTextureRegionFactory.createFromAsset(donghoBitMap,this,"clock.png",0,0);
        donghoBitMap.load();

    }
    private void onLoadFont() {
        FontFactory.setAssetBasePath(fontPath);
        this.mFont=FontFactory.createFromAsset(this.getFontManager(),this.getTextureManager(),CAMERA_WIDTH,CAMERA_HEIGHT,this.getAssets(),
                "Daddys.ttf",30,true, Color.BLACK);
        mFont.load();

    }
    private void onLoadButton() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(pathButton);
        myButtonLeftBitMap=new BitmapTextureAtlas(this.getTextureManager(),64,64,TextureOptions.DEFAULT);
        myButtonLeftRegion=BitmapTextureAtlasTextureRegionFactory.createFromAsset(myButtonLeftBitMap,this,"_leftarrow.png",0,0);
        myButtonRightBitMap=new BitmapTextureAtlas(this.getTextureManager(),64,64,TextureOptions.DEFAULT);
        myButtonRightRegion=BitmapTextureAtlasTextureRegionFactory.createFromAsset(myButtonRightBitMap,this,"_rightarrow.png",0,0);
        myButtonJumpBitMap=new BitmapTextureAtlas(this.getTextureManager(),72,72,TextureOptions.DEFAULT);
        myButtonJumpRegion=BitmapTextureAtlasTextureRegionFactory.createFromAsset(myButtonJumpBitMap,this,"Jump_Cat.png",0,0);
        myButtonLeftBitMap.load();
        myButtonRightBitMap.load();
        myButtonJumpBitMap.load();
    }

    @Override
    protected Scene onCreateScene() {
        mEngine.registerUpdateHandler(new FPSLogger());
        mScene=new Scene();

        Loadlever();
        onCreateCat();
        onCreateControll();
//        onCreateTime();

        final Rectangle ground = new Rectangle(0,CAMERA_HEIGHT,CAMERA_WIDTH+200,2,getVertexBufferObjectManager());
        PhysicsFactory.createBoxBody(this.mPhysicsWorld,ground, BodyDef.BodyType.StaticBody, wallfixture);
        final Rectangle left = new Rectangle(1,1,2,CAMERA_HEIGHT+50,getVertexBufferObjectManager());
        PhysicsFactory.createBoxBody(this.mPhysicsWorld,left, BodyDef.BodyType.StaticBody, wallfixture);
        final Rectangle right = new Rectangle(CAMERA_WIDTH-2,0,2,CAMERA_HEIGHT+50,getVertexBufferObjectManager());
        PhysicsFactory.createBoxBody(this.mPhysicsWorld,right, BodyDef.BodyType.StaticBody, wallfixture);
        final Rectangle top = new Rectangle(0,0,CAMERA_WIDTH+200,2,getVertexBufferObjectManager());
        PhysicsFactory.createBoxBody(this.mPhysicsWorld,top, BodyDef.BodyType.StaticBody, wallfixture);
        this.mScene.registerUpdateHandler(this.mPhysicsWorld);
        return mScene;
    }
    public  void  onCreateCat(){
        meo =new Meo(0,CAMERA_HEIGHT*0.6f,MeoTiledAnimation,getVertexBufferObjectManager());
        meo.setScale(0.5f);
        mScene.attachChild(meo);
        meo.animate(100);
        bodyMeo = PhysicsFactory.createBoxBody(mPhysicsWorld, meo, BodyDef.BodyType.DynamicBody, mFixtureDef);
        bodyMeo.setFixedRotation(true);
        mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(meo,bodyMeo,true,true));
    }


    private void onCreateControll() {
        Sprite leftArrow=new Sprite(0,0,myButtonLeftRegion,getVertexBufferObjectManager()){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionDown()){
                    if(statusCat==false){
                        this.setAlpha(0.5f);
                        meo.MeoMoveLeft(meo,bodyMeo);
                        statusCat=true;
                        Log.v("dichuyentrai","Đang di chuyển trái");
                    }
                    else if(statusCat==true){
                        meo.MeoDungYen(meo,bodyMeo);
                        statusCat=false;
                    }
                }else if(pSceneTouchEvent.isActionUp()){
                    meo.MeoDungYen(meo,bodyMeo);
                    this.setAlpha(1f);
                }

                return true;
            }
        };
        Sprite rightArrow=new Sprite(0,0,myButtonRightRegion,getVertexBufferObjectManager()){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionDown()){
                    if(statusCat==false){
                        this.setAlpha(0.5f);
                        meo.MeoMoveRight(meo,bodyMeo);
                        statusCat=true;
                        Log.v("dichuyenphai","Đang di chuyển phải");
                    }
                    else if(statusCat==true){
                        meo.MeoDungYen(meo,bodyMeo);
                        statusCat=false;
                    }
                }else if(pSceneTouchEvent.isActionUp()){
                    meo.MeoDungYen(meo,bodyMeo);
                    this.setAlpha(1f);
                }

                return true;
            }
        };
        Sprite jump=new Sprite(0,0,myButtonJumpRegion,getVertexBufferObjectManager()){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionDown()){
                    this.setAlpha(1f);
                    meo.Jump(bodyMeo);
                }
                if(pSceneTouchEvent.isActionUp()){
                    this.setAlpha(1.5f);
                }
                return true;
            }
        };
        leftArrow.setPosition(0,CAMERA_HEIGHT-leftArrow.getHeight());
        rightArrow.setPosition(leftArrow.getWidth()+50,CAMERA_HEIGHT-rightArrow.getHeight());
        jump.setPosition(CAMERA_WIDTH-jump.getWidth(),CAMERA_HEIGHT-jump.getHeight());
        jump.setAlpha(1.5f);
        leftArrow.setAlpha(1f);
        rightArrow.setAlpha(1f);
        mScene.attachChild(leftArrow);
        mScene.attachChild(rightArrow);
        mScene.attachChild(jump);
        mScene.registerTouchArea(jump);
        mScene.registerTouchArea(leftArrow);
        mScene.registerTouchArea(rightArrow);
    }

    public void Loadlever(){
        lever1();
        Kiemtravacham();
        onCreateTime();

}

    private void lever1() {
        ArrayList<TMXLayer> mapLayers = mTMXTiledMap.getTMXLayers();
        for(TMXLayer mLayer :mapLayers){
            if(!mLayer.getName().equals("dongho")){
                mVatCanTMXLayer=mLayer;
                mScene.attachChild(mLayer);
            }
        }
        chuotArray=new LinkedList();
        for(final TMXObjectGroup group:this.mTMXTiledMap.getTMXObjectGroups()){
            for (final  TMXObject object:group.getTMXObjects()){
                if (group.getName().equals("muctieuchuot")){
                    Sprite chuotSprite=new Sprite(object.getX(),
                            object.getY(),object.getWidth(),
                            object.getHeight(),
                            chuotTextureRegion,
                            getVertexBufferObjectManager());
                    chuotArray.add(chuotSprite);
                }
            }
        }
        for(final TMXObjectGroup group:this.mTMXTiledMap.getTMXObjectGroups()){
            for (final  TMXObject object:group.getTMXObjects()){
                if (group.getName().equals("vatcan")){
                    Rectangle rect=new Rectangle(object.getX()+10f,object.getY(),object.getWidth()-15f,object.getHeight(),getVertexBufferObjectManager());
                    PhysicsFactory.createBoxBody(mPhysicsWorld,rect, BodyDef.BodyType.StaticBody,wallfixture);

                }
            }
        }
        donghoArray=new LinkedList<>();
        for(final TMXObjectGroup group:this.mTMXTiledMap.getTMXObjectGroups()){
            for (final  TMXObject object:group.getTMXObjects()){
                if (group.getName().equals("muctieudongho")){
                    Sprite donghoSprite=new Sprite(object.getX(),
                            object.getY(),object.getWidth(),
                            object.getHeight(),
                            donghoRegion,
                            getVertexBufferObjectManager());
                    donghoArray.add(donghoSprite);
                }
            }
        }
//        dem so luong chuot.
        for(Sprite s:chuotArray) {
            Score_nextlever++;
            mScene.attachChild(s);
        }
        for(int i=0;i<donghoArray.size();i++) {
            mScene.attachChild(donghoArray.get(i));
        }
//        final TMXLayer tmxLayer = this.mTMXTiledMap.getTMXLayers().get(0);
//        this.mCAMERA.setBounds(0, tmxLayer.getWidth(), 0, tmxLayer.getHeight());
//        this.mCAMERA.setBoundsEnabled(true);
    }
public  void Kiemtravacham() {
    mEngine.registerUpdateHandler(new IUpdateHandler() {
        @Override
        public void onUpdate(float pSecondsElapsed) {
            checkCollision();
                Score.setText("Điểm: "+String.valueOf(myScore)+" / "+Score_nextlever);

        }

        @Override
        public void reset() {
        }
    });
    Score=new Text(CAMERA_WIDTH*0.2f,mTextYPos,this.mFont,"Điểm "+myScore,50,getVertexBufferObjectManager());
    Score.setPosition(CAMERA_WIDTH*0.4f,mTextYPos);
    mScene.attachChild(Score);
    }
    public void checkCollision() {
        for (Sprite s : chuotArray) {
            if (meo.collidesWith(s)) {
                mScene.detachChild(s);
                s.setX(-1000);
                s.setY(-1000);
                amVaCham.play();
                myScore++;
                Log.v("davacham", "Đã va chạm");
            }
        }
        for(Sprite s:donghoArray){
            if (meo.collidesWith(s)){
                mScene.detachChild(s);
                s.setX(-1000);
                s.setY(-1000);
                amDongho.play();
                time=time+5;
            }
        }
    }

    public void onCreateTime(){
        Timer=new Text(0,0,this.mFont,"",50,getVertexBufferObjectManager());
        mScene.registerUpdateHandler(new TimerHandler(1f, true, new ITimerCallback() {
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                time--;
                Timer.setText("Thời gian : "+String.valueOf(time));
                Timer.setPosition(CAMERA_WIDTH-Timer.getWidth()*1.1f,mTextYPos);
                if(time==0 && myScore<Score_nextlever){
                    luudiem();
                    mScene.unregisterUpdateHandler(pTimerHandler);
//                    Intent intent =new Intent(GameActivity.this,MenuGameActivity.class);
//                    startActivity(intent);
//                    finish();
                }else if(time>0 && myScore==Score_nextlever){
                    Intent intent =new Intent(GameActivity.this,Level2_Activity.class);
                    startActivity(intent);
                    finish();
                }
                pTimerHandler.reset();
            }
        }));
        mScene.attachChild(Timer);
    }
    @Override
    public void onBackPressed() {
        Intent intent =new Intent(GameActivity.this,MenuGameActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
    public  void luudiem(){
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder  builder= new AlertDialog.Builder(GameActivity.this);
                    final EditText myEditText=new EditText(GameActivity.this);
                    builder.setView(myEditText);
                    builder.setMessage("Hết Thời Gian ! "+" Tên bạn là gì ?").setCancelable(true).setPositiveButton("Lưu",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try {
                                        String yourname=myEditText.getText().toString();
                                        editMyNameShared.putString("KEY_NAME",yourname+" (Level 1)");
                                        editMyScoreShared.putInt("KEY_SCORE",myScore);
                                        editMyScoreShared.commit();
                                        editMyNameShared.commit();
                                        Log.v("Gui","Đã gửi được" +yourname);
                                        Intent intent =new Intent(GameActivity.this,HighScoreActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }catch (Exception e){
                                        Log.v("Khongguiduoc","Không gửi được" );
                                    }


                                }
                            });
                    AlertDialog alertDialog =builder.create();
                    alertDialog.show();

                }
            });

        }catch (Exception e){
            Log.v("ErrorLuuDiem","Không thể tạo Dialog !");
        }
    }
}
