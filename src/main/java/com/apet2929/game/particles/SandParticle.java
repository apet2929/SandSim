package com.apet2929.game.particles;

import com.apet2929.engine.model.Model;
import com.apet2929.game.World;
import org.joml.Vector2i;

public class SandParticle extends Particle {
    public SandParticle(Model model) {
        super(model, ParticleType.SAND);
    }

    @Override
    public void update(World world, Vector2i pos) {
        world.moveDown(pos);
    }
}
