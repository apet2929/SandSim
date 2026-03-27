package com.apet2929.game.particles.liquid;

import com.apet2929.game.particles.ParticleType;

public class Water extends Liquid{
    public Water(int x, int y) {
        super(x, y, ParticleType.WATER);
        this.dispersion = 5.0f;
    }

}
