package com.apet2929.game.particles.solid;

import com.apet2929.engine.utils.Consts;
import com.apet2929.game.World;
import com.apet2929.game.particles.Particle;
import com.apet2929.game.particles.ParticleType;
import com.apet2929.game.particles.liquid.Liquid;

public class NonMoveableSolid extends Particle {
    public NonMoveableSolid(int x, int y, ParticleType type) {
        super(x, y, type);
    }

    @Override
    public boolean canSwap(ParticleType type) {
        return false;
    }

    @Override
    public void update(World world) {
    }
}
