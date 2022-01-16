package com.apet2929.game.particles;

import com.apet2929.engine.model.Model;
import com.apet2929.game.World;
import org.joml.Vector2i;

public class GasParticle extends Particle{
    public GasParticle(Model model, ParticleType type) {
        super(model, type);
    }

    @Override
    public void update(World world, Vector2i pos) {
        if (this.canPass(world.getAt(pos.x, pos.y + 1))) {
            world.moveWithSwap(pos, 0, 1);
        } else if (this.canPass(world.getAt(pos.x - 1, pos.y + 1))) {
            world.moveWithSwap(pos, -1, 1);
        } else if (this.canPass(world.getAt(pos.x + 1, pos.y + 1))) {
            world.moveWithSwap(pos, 1, 1);
        } else if (this.canPass(world.getAt(pos.x + 1, pos.y))) {
            world.moveWithSwap(pos, 1, 0);
        } else if(this.canPass(world.getAt(pos.x - 1, pos.y))) {
            world.moveWithSwap(pos, -1, 0);
        }
    }
}
