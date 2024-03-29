package com.apet2929.engine;

import com.apet2929.engine.utils.Consts;
import com.apet2929.game.Launcher;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import static com.apet2929.engine.utils.Consts.FRAMERATE;

public class EngineManager {
    public static final long NANOSECOND = 1000000000L;
    private static int fps;
    private static final float frametime = 1.0f / FRAMERATE;
    private static float lastFrameTime;

    private boolean isRunning;

    private WindowManager window;
    private GLFWErrorCallback errorCallback;
    private ILogic gameLogic;
    private MouseInput mouseInput;

    private void init() throws Exception {
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        window = Launcher.getWindow();
        gameLogic = Launcher.getGame();
        mouseInput = new MouseInput();
        window.init();
        gameLogic.init();
        mouseInput.init();
        fps = (int) FRAMERATE;
    }

    public void start() throws Exception {
        init();

        if(isRunning)
            return;
        run();
    }

    public void run() {
        this.isRunning = true;

        int frames = 0;
        long frameCounter = 0;

        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

//        Rendering loop
        while(isRunning){
            boolean render = false;

            long startTime = System.nanoTime();
            lastFrameTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += lastFrameTime / (double) NANOSECOND;
            frameCounter += lastFrameTime;

            while(unprocessedTime > frametime){
                render = true;
                unprocessedTime -= frametime;

                if(window.windowShouldClose())
                    stop();

                if(frameCounter >= NANOSECOND){
                    setFps(frames);
                    window.setTitle(Consts.TITLE + " FPS: " + getFps());
                    frames = 0;
                    frameCounter = 0;
                }
            }

            if(render) {
                input();
                update();
                render();
                frames++;
            }
        }
        cleanup();
    }

    private void stop(){
        if(!isRunning)
            return;
        isRunning = false;
    }

    private void input(){
        mouseInput.input();
        gameLogic.input(mouseInput);
    }

    private void render(){
        gameLogic.render();
        window.update();
    }

    private void update() {
        gameLogic.update();
    }

    private void cleanup(){
        window.cleanup();
        gameLogic.cleanup();
        errorCallback.free();
        GLFW.glfwTerminate();
    }

    public static int getFps() {
        return fps;
    }

    public static float getDeltaTime() {
        return lastFrameTime / (float) NANOSECOND;

    }

    public static void setFps(int fps) {
        EngineManager.fps = fps;
    }
}
