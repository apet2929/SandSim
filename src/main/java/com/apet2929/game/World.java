package com.apet2929.game;

import com.apet2929.engine.RenderManager;
import com.apet2929.engine.model.Grid;
import com.apet2929.engine.model.Model;
import com.apet2929.game.particles.EmptyParticle;
import com.apet2929.game.particles.Particle;
import com.apet2929.game.particles.ParticleType;
import org.joml.*;

import java.util.ArrayList;
import java.util.Arrays;

public class World {
    private final int width, height;
    private Particle[][] particles;
    private final Grid grid;

    /*
    TODO:
    ParticleType and MatterState replaced by instanceof
    Particles now store their own position, but can only be modified through the world class to avoid array desync.
    Particles updated by making an array of particles to be updated and then using a foreach loop
            to avoid the same particle being updated more than once


    ... I've got a lot of work ahead of me...
    Maybe delete all of these classes and start from scratch?

     */

    public World(Grid grid) {
        this.grid = grid;

        this.width = grid.getNumCols();
        this.height = grid.getNumRows();
        particles = initWorld(width, height);


    }

    public void update() {
        for (Particle particle : getParticlesToBeUpdated()) {
            particle.update(this);
        }
    }

    public void render(RenderManager renderer, Model particleModel) {

        Vector2i particlePos = new Vector2i(0, 0);
        for (int i = 0; i < particles.length; i++) {
            particlePos.y = i;
            for (int j = 0; j < particles[i].length; j++) {
                particlePos.x = j;
                boolean b = !(particles[i][j] instanceof EmptyParticle);
                if(b) {
                    particleModel.setTexture(particles[i][j].getTexture());
                    renderer.renderParticle(particleModel, grid.calculateGridPosition(particlePos.x, particlePos.y));
                }
            }
        }
    }

    public void spawnParticle(ParticleType type, int x, int y) {
        Particle particle = type.createParticleByMatrix(x, y);
        setAt(x, y, particle);
    }

    public void moveWithSwap(Vector2i pos, int changeX, int changeY) {
        swapParticles(pos, pos.x + changeX, pos.y + changeY);
    }

    public void moveDown(Vector2i pos) {
        if(pos.y != 0)
            swapParticles(pos, new Vector2i(pos.x, pos.y-1));
    }

    public void swapParticles(Vector2i pos1, Vector2i pos2) {
        Particle temp = getAt(pos1);
        setAt(pos1, getAt(pos2));
        setAt(pos2, temp);
    }

    public void swapParticles(Vector2i pos1, int x2, int y2) {
        Particle temp = getAt(pos1);
        setAt(pos1, getAt(x2, y2));
        setAt(x2, y2, temp);
    }

    public void swapParticles(int x1, int y1, int x2, int y2) {
        Particle temp = getAt(x1, y1);
        setAt(x1, y1, getAt(x2, y2));
        setAt(x2, y2, temp);
    }

    public void setAt(int x, int y, Particle particle) {
        if(posInGridRange(x, y)) {
            particles[y][x] = particle;
            particle.setPositionByWorld(x, y);
        }
    }

    public void setAt(Vector2i pos, Particle particle) {
        if(posInGridRange(pos)) {
            particles[pos.y][pos.x] = particle;
            particle.setPositionByWorld(pos.x, pos.y);
        }
    }

    public Particle getAt(int x, int y) {
        if(posInGridRange(x, y))
            return particles[y][x];
        return null;
    }

    public Particle getAt(Vector2i pos) {
        return particles[pos.y][pos.x];
    }

    private static Particle[][] initWorld(int width, int height) {
        Particle[][] particles = new Particle[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                particles[y][x] = new EmptyParticle(x, y);
            }
        }
        return particles;
    }

    public Vector2i mouseToGridCoords(Vector3f normalizedMousePos) {

        return grid.worldToGridCoordinates(normalizedMousePos);
    }

    public boolean posInGridRange(Vector2i pos) {
         return (pos.x < height && pos.x >= 0 && pos.y < height && pos.y >= 0);
    }

    public boolean posInGridRange(int posX, int posY) {
        return (posX < width && posX >= 0 && posY < height && posY >= 0);
    }

    private ArrayList<Particle> getParticlesToBeUpdated() {
        ArrayList<Particle> particlesToBeUpdated = new ArrayList<>();
        for (Particle[] row: particles) {
            for(Particle particle : row) {
                if(particle.getType() != ParticleType.EMPTYPARTICLE) {
                    particlesToBeUpdated.add(particle);
                }
            }
        }
        return particlesToBeUpdated;
    }

}
