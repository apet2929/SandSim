package com.apet2929.game.particles.gas;

import com.apet2929.game.World;
import com.apet2929.game.particles.Particle;
import com.apet2929.game.particles.ParticleType;

public abstract class Gas extends Particle {
    public Gas(int x, int y, ParticleType type) {
        super(x, y, type);
    }

    @Override
    public void update(World world) {
        super.update(world);
        if(canSwap(world.getAt(getGridX(), getGridY()+1))) {
            world.swapParticles(this, getGridX(), getGridY()+1);
//            world.swapParticlesInc(this, 0, -1);
        }
        else if(canSwap(world.getAt(getGridX()-1, getGridY()+1))) {
            world.swapParticles(this, getGridX()-1, getGridY()+1);
//            world.swapParticlesInc(this, -1, -1);
        }
        else if(canSwap(world.getAt(getGridX()+1, getGridY()+1))) {
            world.swapParticles(this, getGridX()+1, getGridY()+1);
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
    public boolean canSwap(ParticleType type) {
        return type.matterType == ParticleType.MatterType.EMPTY;
    }
}
