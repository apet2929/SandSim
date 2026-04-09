package com.apet2929.game.particles.liquid;

import com.apet2929.game.World;
import com.apet2929.game.particles.Particle;
import com.apet2929.game.particles.ParticleType;

import java.util.List;

public class Oil extends Liquid{
    public Oil(int x, int y) {
        super(x, y, ParticleType.OIL);
        this.dispersion = 5.0f;
    }
}
