package com.apet2929.game.particles;

import com.apet2929.engine.model.Model;
import com.apet2929.engine.model.ObjectLoader;
import com.apet2929.engine.model.Texture;
import org.lwjgl.system.CallbackI;

public class ParticleLoader {

    private static final float[] vertices = {
            -0.5f, 0.5f, 0f,    //  Top left vertex
            -0.5f, -0.5f, 0f,   //  Bottom left vertex
            0.5f, -0.5f, 0f,    //  Bottom right vertex
            0.5f, 0.5f, 0f,     //  Top right
//                -0.5f, 0.5f, 0f     //  Top left
//                0.5f, -0.5f, 0f,    //  Bottom right vertex
    };



    private static final int[] indices = {
            0,1,3,
            3,1,2
    };

    private static final float[] textureCoords = {
            0,0,
            0,1,
            1,1,
            1,0
    };
//    public static SandParticle loadSandParticle(ObjectLoader loader) {
//        Texture sandTexture;
//        TODO : Make asset manager class
//        Model sandModel = loader.loadModel(vertices, indices, textureCoords, sandTexture);
//        return new SandParticle(sandModel);
//    }

}
