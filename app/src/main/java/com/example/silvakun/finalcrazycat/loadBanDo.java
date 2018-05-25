package com.example.silvakun.finalcrazycat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Silva Kun on 05-May-18.
 */

public class loadBanDo {

    public static TMXTiledMap getTMXTiledMap(Scene mScene, Engine mEngine,  Context mContext, String maps_name,final Activity activity,final VertexBufferObjectManager vbom){
        TMXTiledMap mTMXTiledMap;
        try{
            final TMXLoader tmxLoader = new TMXLoader(mContext, mEngine.getTextureManager(), TextureOptions.BILINEAR_PREMULTIPLYALPHA,vbom,new TMXLoader.ITMXTilePropertiesListener(){
                public void onTMXTileWithPropertiesCreated(TMXTiledMap pTMXTiledMap, TMXLayer pTMXLayer, TMXTile pTMXTile,
                                                           TMXProperties pTMXTileProperties){
                }
            });
            mTMXTiledMap = tmxLoader.loadFromAsset( "tmx/"+maps_name);
            return mTMXTiledMap;
        }catch (final TMXLoadException tmxle) {
            // Nếu không tải được sẽ bắn ra Dialog cảnh báo. Chọn OK đ�?dừng ứng dụng.
            String error = tmxle.toString();
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Error 20:");
            builder.setMessage("Load map false ! - " + error);
            builder.setCancelable(false);
            builder.setNegativeButton("OK", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    activity.finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        return null;
    }
}
