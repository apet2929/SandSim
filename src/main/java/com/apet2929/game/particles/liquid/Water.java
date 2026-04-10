package com.apet2929.game.particles.liquid;

import com.apet2929.game.particles.ParticleType;

public class Water extends Liquid{
    int mass;

    public Water(int x, int y) {
        super(x, y, ParticleType.WATER);
        mass = 1;
        this.dispersion = 5.0f;
    }

    @Override
    public boolean canSwap(ParticleType type) {
        return super.canSwap(type) || type == ParticleType.OIL;
    }
}
