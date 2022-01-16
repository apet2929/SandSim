package com.apet2929.game.particles;

import com.apet2929.game.World;
import org.joml.Vector2i;

public class EmptyParticle extends Particle {

    public EmptyParticle() {
        super(null, ParticleType.NONE);
    }

    @Override
    public void update(World world, Vector2i pos) {
    }
}
