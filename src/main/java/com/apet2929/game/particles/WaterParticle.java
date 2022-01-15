package com.apet2929.game.particles;

import com.apet2929.engine.model.Model;
import com.apet2929.game.World;
import org.joml.Vector2i;

public class WaterParticle extends Particle{


    public WaterParticle(Model model) {
        super(model, ParticleType.WATER);
    }

    @Override
    public void update(World world, Vector2i pos) {
        if(world.particleIsType(pos.x, pos.y-1, ParticleType.AIR)) {
            world.moveDown(pos);
        } else if(world.particleIsType(pos.x-1, pos.y-1, ParticleType.AIR)) {
            world.swapParticles(pos, pos.x-1, pos.y-1);
        }  else if(world.particleIsType(pos.x+1, pos.y-1, ParticleType.AIR)) {
            world.swapParticles(pos, pos.x+1, pos.y-1);
        }  else if(world.particleIsType(pos.x+1, pos.y, ParticleType.AIR)) {
            world.swapParticles(pos, pos.x+1, pos.y);
        }  else if(world.particleIsType(pos.x-1, pos.y, ParticleType.AIR)) {
            world.swapParticles(pos, pos.x-1, pos.y);
        }
    }
}
