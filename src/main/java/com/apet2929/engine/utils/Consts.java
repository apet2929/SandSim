package com.apet2929.engine.utils;

public class Consts {

    public static final String TITLE = "ENGINE";
    public static final float CAMERA_STEP = 0.05f;
    public static final float MOUSE_SENSITIVITY = 0.2f;
    public static final float Z_NEAR = 0.01f;
    public static final float Z_FAR = 1000f;
    public static final float FOV = (float) Math.toRadians(60);
    public static final float FRAMERATE = 120;


    public static final int NUM_COLS_GRID = 150;
    public static final int NUM_ROWS_GRID = 100;

    public static final int PARTICLE_TPS = Math.min(NUM_COLS_GRID, 120);
    public static final float PARTICLE_DELTA = 1.0f / PARTICLE_TPS;
    public static final float GRAVITY = -1 * (NUM_ROWS_GRID / 4.0f);

    public static float GRID_Z = -2.0f;

}
