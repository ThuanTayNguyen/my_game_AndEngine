package com.example.silvakun.finalcrazycat;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Silva Kun on 31-Mar-18.
 */

public class Meo extends AnimatedSprite {

    public Meo(float pX, float pY, ITiledTextureRegion pTiledTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }
    public void Jump(Body body){
        body.setLinearVelocity(new Vector2(body.getLinearVelocity().x,-6));
    }
    public  void MeoMoveLeft(AnimatedSprite meo,Body body){
        meo.animate(500);
        meo.setFlippedHorizontal(true);
        body.setLinearVelocity(new Vector2(-4,body.getLinearVelocity().y));
    }
    public  void MeoMoveRight(AnimatedSprite meo,Body body){
        meo.animate(500);
        meo.setFlippedHorizontal(false);
        body.setLinearVelocity(new Vector2(+4,body.getLinearVelocity().y));
    }
    public  void MeoDungYen( AnimatedSprite meo,Body body){
        meo.animate(1000);
        body.setLinearVelocity(new Vector2(0,0));
    }
    @Override
    public void stopAnimation() {
        super.stopAnimation();
    }

    @Override
    public boolean isAnimationRunning() {
        return super.isAnimationRunning();
    }



}
