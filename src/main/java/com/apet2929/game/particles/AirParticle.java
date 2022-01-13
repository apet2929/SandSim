package com.apet2929.game.particles;

import com.apet2929.engine.model.Model;
import com.apet2929.game.World;
import org.joml.Vector2i;

public class AirParticle extends Particle {

    public AirParticle() {
        super(null, ParticleType.AIR);

    }

    @Override
    public void update(World world, Vector2i pos) {

    }
}
