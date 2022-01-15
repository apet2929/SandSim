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
        if(world.particleIsType(pos.x, pos.y-1, ParticleType.AIR)) {
            world.moveDown(pos);
        } else if(world.particleIsType(pos.x-1, pos.y-1, ParticleType.AIR)) {
            world.swapParticles(pos, pos.x-1, pos.y-1);
        }  else if(world.particleIsType(pos.x+1, pos.y-1, ParticleType.AIR)) {
            world.swapParticles(pos, pos.x+1, pos.y-1);
        }
    }
}
