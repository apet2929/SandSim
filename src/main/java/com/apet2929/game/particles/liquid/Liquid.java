package com.apet2929.game.particles.liquid;

import com.apet2929.game.World;
import com.apet2929.game.particles.Particle;
import com.apet2929.game.particles.ParticleType;

public abstract class Liquid extends Particle {
    public Liquid(int x, int y) {
        super(x, y);
    }

    @Override
    public void update(World world) {
        if(canSwap(world.getAt(getGridX(), getGridY()-1))) {
            world.swapParticles(this, getGridX(), getGridY()-1);
//            world.swapParticlesInc(this, 0, -1);
        }
        else if(canSwap(world.getAt(getGridX()-1, getGridY()-1))) {
            world.swapParticles(this, getGridX()-1, getGridY()-1);
//            world.swapParticlesInc(this, -1, -1);
        }
        else if(canSwap(world.getAt(getGridX()+1, getGridY()-1))) {
            world.swapParticles(this, getGridX()+1, getGridY()-1);
//            world.swapParticlesInc(this, 1, -1);
        }
        else if(canSwap(world.getAt(getGridX()-1, getGridY()))) {
            world.swapParticles(this, getGridX()-1, getGridY());
//            world.swapParticlesInc(this, 1, 0);
        }
        else if(canSwap(world.getAt(getGridX()+1, getGridY()))) {
            world.swapParticles(this, getGridX()+1, getGridY());
//            world.swapParticlesInc(this, -1, 0);
        }
    }


    @Override
    public boolean canSwap(ParticleType.MatterType t) {
        return (t == ParticleType.MatterType.EMPTY || t == ParticleType.MatterType.GAS);
    }
}
