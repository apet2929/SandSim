package com.apet2929.game.particles.liquid;

import com.apet2929.game.particles.ParticleType;

public class Fire extends Liquid{
    public Fire(int x, int y) {
        super(x, y, ParticleType.FIRE);
        this.dispersion = 1.0f;
    }

}