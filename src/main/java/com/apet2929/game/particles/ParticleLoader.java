package com.apet2929.game.particles;

import com.apet2929.engine.AssetCache;
import com.apet2929.engine.model.Texture;


import java.util.HashMap;

public class ParticleLoader {

    private static final HashMap<ParticleType, Texture> particleTextureMap = new HashMap<>();

    public static Texture getParticleTexture(ParticleType particleType) {
        return particleTextureMap.get(particleType);
    }

    public static void initParticleType(ParticleType particleType, AssetCache assetCache) {
        particleTextureMap.put(particleType, assetCache.loadTexture(particleType.toString()));
    }

}
