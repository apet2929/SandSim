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
        int dir = world.getDirectionBias();
        if(canSwap(world.getAt(getGridX(), getGridY()-1))) {
            world.swapParticles(this, getGridX(), getGridY()-1);
        }
        else if(canSwap(world.getAt(getGridX()-dir, getGridY()-1))) {
            world.swapParticles(this, getGridX()-dir, getGridY()-1);
        }
        else if(canSwap(world.getAt(getGridX()+dir, getGridY()-1))) {
            world.swapParticles(this, getGridX()+dir, getGridY()-1);
        }
        else if(canSwap(world.getAt(getGridX()-dir, getGridY()))) {
            world.swapParticles(this, getGridX()-dir, getGridY());
        }
        else if(canSwap(world.getAt(getGridX()+dir, getGridY()))) {
            world.swapParticles(this, getGridX()+dir, getGridY());
        }
    }


    @Override
    public boolean canSwap(ParticleType.MatterType t) {
        return (t == ParticleType.MatterType.EMPTY || t == ParticleType.MatterType.GAS);
    }
}
