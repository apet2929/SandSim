package com.apet2929.engine.utils;

public class Consts {

    public static final String TITLE = "ENGINE";
    public static final float CAMERA_STEP = 0.05f;
    public static final float MOUSE_SENSITIVITY = 0.2f;
    public static final float Z_NEAR = 0.01f;
    public static final float Z_FAR = 1000f;
    public static final float FOV = (float) Math.toRadians(60);
    public static final float FRAMERATE = 120;
    public static final int PARTICLE_FPS = 20;


    public static final float GRID_WIDTH = 2f;
    public static final float GRID_HEIGHT = 2f;
    public static final int NUM_COLS_GRID = 20;
    public static final int NUM_ROWS_GRID = 20;
    public static final float ROW_WIDTH = GRID_HEIGHT / NUM_ROWS_GRID;
    public static final float COL_WIDTH = GRID_WIDTH / NUM_COLS_GRID;
    public static float GRID_Z = -2.0f;

}
