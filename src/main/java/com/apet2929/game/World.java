package com.apet2929.game;

import com.apet2929.engine.RenderManager;
import com.apet2929.engine.model.Grid;
import com.apet2929.engine.model.Model;
import com.apet2929.engine.model.ObjectLoader;
import com.apet2929.engine.utils.Consts;
import com.apet2929.game.particles.EmptyParticle;
import com.apet2929.game.particles.Particle;
import com.apet2929.game.particles.ParticleType;
import org.joml.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class World {
    private int width, height;
    private Particle[][] particles;
    private Grid grid;

    private boolean directionBias = false;
    int directionThreshold = Consts.NUM_ROWS_GRID / 16;

    public World(Grid grid) {
        this.grid = grid;

        this.width = grid.getNumCols();
        this.height = grid.getNumRows();
        particles = initWorld(width, height);
    }

    public enum ExpandDirection {
        UP, DOWN, LEFT, RIGHT
    }

    public Grid expand(ObjectLoader loader, ExpandDirection direction, int amount) {
        // adds `amount` new rows/columns in the specified direction
        loader.cleanupGrid(grid);
        int oldWidth = this.width;
        int oldHeight = this.height;
        int newWidth = this.width;
        int newHeight = this.height;
        if(direction == ExpandDirection.UP || direction == ExpandDirection.DOWN) newHeight += amount;
        else newWidth += amount;
        Grid newGrid = loader.loadGrid(newWidth, newHeight);
        Particle[][] newParticles = initWorld(newWidth, newHeight);
        Vector2i offset = new Vector2i(0,0);
        switch(direction) {
            case UP -> {
                offset.y = 0;
            }
            case DOWN -> {
                offset.y = amount;
            }
            case LEFT -> {
                offset.x = amount;
            }
            case RIGHT -> {
                offset.x = 0;
            }
        }
        for (int x = 0; x < oldWidth; x++) {
            for (int y = 0; y < oldHeight; y++) {
                newParticles[y + offset.y][x + offset.x] = getAt(x,y);
            }
        }

        this.grid = newGrid;
        this.width = newWidth;
        this.height = newHeight;
        this.particles = newParticles;
        return grid;
    }

    public void update() {
        int i = 0;
        for (Particle particle : getParticlesToBeUpdated()) {

            if(i > directionThreshold) {
                i = 0;
                directionBias = !directionBias;
            }
            particle.update(this);
            i++;

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

    public int getDirectionBias() {
        return directionBias ? 1 : -1;
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

    public void swapParticles(Particle particle, int x, int y) {
        swapParticles(particle.getGridX(), particle.getGridY(), x, y);
    }

    public void swapParticlesInc(Particle particle, int xInc, int yInc) {
        swapParticles(particle.getGridX(), particle.getGridY(), particle.getGridX() + xInc, particle.getGridY() + yInc);
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

    public boolean posInGridRange(Vector2i pos) {
         return (pos.x < height && pos.x >= 0 && pos.y < height && pos.y >= 0);
    }

    public boolean posInGridRange(int posX, int posY) {
        return (posX < width && posX >= 0 && posY < height && posY >= 0);
    }

    public void fillRandomly() {
        Vector2i gridSize = new Vector2i(grid.getNumCols(), grid.getNumRows());
        java.util.Random random = new Random();
        ParticleType[] types = ParticleType.values();
        for(int x = 0; x < gridSize.x; x++) {
            for(int y = 0; y < gridSize.y; y++) {
                int randomTypeId = random.nextInt(types.length);
                ParticleType randomType = types[randomTypeId];
                spawnParticle(randomType, x, y);
            }
        }
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
