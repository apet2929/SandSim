package com.apet2929.game.particles;

import com.apet2929.engine.model.Model;

public abstract class Particle {

    private Model model;
    protected ParticleType type;

    public Particle(Model model) {
        this.model = model;
    }

    public abstract void update(Particle[][] particles);

    public ParticleType getType() {
        return type;
    }
}
