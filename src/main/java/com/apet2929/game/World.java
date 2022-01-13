package com.apet2929.game;

import com.apet2929.engine.RenderManager;
import com.apet2929.engine.model.Grid;
import com.apet2929.game.particles.AirParticle;
import com.apet2929.game.particles.Particle;
import com.apet2929.game.particles.ParticleType;
import org.joml.Vector2i;

public class World {
    private int width, height;
    public Particle[][] particles;
    private Grid grid;

    public World(Grid grid) {
        this.grid = grid;

        this.width = grid.getNumCols();
        this.height = grid.getNumRows();
        particles = initWorld(width, height);
    }

    public void update() {
        Vector2i particlePos = new Vector2i(0, 0);
        for (int i = 0; i < particles.length; i++) {
            particlePos.y = i;
            for (int j = 0; j < particles[i].length; j++) {
                particlePos.x = j;
                particles[i][j].update(this, particlePos);
            }
        }
    }

    public void render(RenderManager renderer) {
        Vector2i particlePos = new Vector2i(0, 0);
        for (int i = 0; i < particles.length; i++) {
            particlePos.y = i;
            for (int j = 0; j < particles[i].length; j++) {
                particlePos.x = j;
                if (particles[i][j].getType() != ParticleType.AIR) {
                    renderer.renderParticle(particles[i][j], grid.calculateGridPosition(particlePos.x, particlePos.y));
                }
            }
        }
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

    public void setAt(int x, int y, Particle particle) {
        particles[y][x] = particle;
    }

    public void setAt(Vector2i pos, Particle particle) {
        particles[pos.y][pos.x] = particle;
    }

    public Particle getAt(int x, int y) {
        return particles[y][x];
    }

    public Particle getAt(Vector2i pos) {
        return particles[pos.y][pos.x];
    }



    private static Particle[][] initWorld(int width, int height) {
        Particle[][] particles = new Particle[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                particles[i][j] = new AirParticle();
            }
        }
        return particles;
    }





}
