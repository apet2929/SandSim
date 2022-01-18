package com.apet2929.game.particles;

import com.apet2929.engine.model.Model;
import com.apet2929.engine.model.Texture;
import com.apet2929.game.World;
import org.joml.Vector2i;


public abstract class Particle {

    private Texture texture;
    private ParticleType type;
    private int gridX, gridY;

    public Particle(int x, int y) {
        gridX = x;
        gridY = y;
        this.type = getEnumType();
        this.texture = ParticleLoader.getParticleTexture(type);
    }
    public abstract void update(World world);
    public abstract boolean canSwap(ParticleType.MatterType type);

    public boolean canSwap(Particle other) {
        if(other == null) return false;
        return canSwap(other.getType().matterType);
    }

    public ParticleType getType() {
        return type;
    }

    public Texture getTexture() {
        return texture;
    }


    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public void setPositionByWorld(Vector2i position) {
        this.gridX = position.x;
        this.gridY = position.y;
    }

    public void setPositionByWorld(int x, int y) {
        this.gridX = x;
        this.gridY = y;
    }

    private ParticleType getEnumType() {
        return ParticleType.valueOf(this.getClass().getSimpleName().toUpperCase());
    }

    @Override
    public String toString() {
        return "Particle{" +
                "type=" + type +
                ", position=(" + gridX + ", " + gridY + ")" +
                '}';
    }
}
