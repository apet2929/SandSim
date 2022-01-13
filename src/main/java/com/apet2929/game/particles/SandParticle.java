package com.apet2929.game.particles;

import com.apet2929.engine.model.Model;

public class SandParticle extends Particle {
    public SandParticle(Model model) {
        super(model);
        this.type = ParticleType.SAND;
    }

    @Override
    public void update(Particle[][] particles) {

    }
}
