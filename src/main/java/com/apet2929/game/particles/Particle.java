package com.apet2929.game.particles;

import com.apet2929.engine.model.Model;
import com.apet2929.game.World;
import org.joml.Vector2i;

public abstract class Particle {

    private Model model;
    private ParticleType type;

    public Particle(Model model, ParticleType type) {
        this.model = model;
        this.type = type;
    }

    public abstract void update(World world, Vector2i pos);

    public ParticleType getType() {
        return type;
    }

    public Model getModel() {
        return model;
    }

}
