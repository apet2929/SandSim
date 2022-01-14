package com.apet2929.engine;

import com.apet2929.game.Launcher;
import org.joml.*;
import org.lwjgl.glfw.GLFW;

public class MouseInput {
    private final Vector2d previousPos, currentPos;
    private final Vector2f displVec;
    private boolean inWindow = false, leftButtonPressed = false, rightButtonPressed = false;

    public MouseInput() {
        previousPos = new Vector2d(-1,-1);
        currentPos = new Vector2d(0,0);
        displVec = new Vector2f();
    }

    public void init() {
        GLFW.glfwSetCursorPosCallback(Launcher.getWindow().getWindow(), ((window, xpos, ypos) -> {
            currentPos.x = xpos;
            currentPos.y = ypos;

        }));
        GLFW.glfwSetCursorEnterCallback(Launcher.getWindow().getWindow(), ((window, entered) -> {
            inWindow = entered;
        }));
        GLFW.glfwSetMouseButtonCallback(Launcher.getWindow().getWindow(), ((window, button, action, mods) -> {
            leftButtonPressed = button == GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_PRESS;
            rightButtonPressed = button == GLFW.GLFW_MOUSE_BUTTON_2 && action == GLFW.GLFW_PRESS;
        }));

    }

    public void input() {
        displVec.x = 0;
        displVec.y = 0;
        if(previousPos.x > 0 && previousPos.y > 0 && inWindow) {
            double x = currentPos.x - previousPos.x;
            double y = currentPos.y - previousPos.y;

            boolean rotX = x != 0;
            boolean rotY = y != 0;
            if(rotX)
                displVec.y = (float) x;
            if(rotY)
                displVec.x = (float) y;
        }
        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;

    }

    public Vector2f getDisplVec() {
        return displVec;
    }

    public Vector2d getPos() {
        return new Vector2d(currentPos);
    }

    /*
    public void selectGameItem(GameItem[] gameItems, Window window, Vector2d mousePos, Camera camera) {
        // Transform mouse coordinates into normalized spaze [-1, 1]
        int wdwWitdh = window.getWidth();
        int wdwHeight = window.getHeight();

        float x = (float)(2 * mousePos.x) / (float)wdwWitdh - 1.0f;
        float y = 1.0f - (float)(2 * mousePos.y) / (float)wdwHeight;
        float z = -1.0f;

        invProjectionMatrix.set(window.getProjectionMatrix());
        invProjectionMatrix.invert();

        tmpVec.set(x, y, z, 1.0f);
        tmpVec.mul(invProjectionMatrix);
        tmpVec.z = -1.0f;
        tmpVec.w = 0.0f;

        Matrix4f viewMatrix = camera.getViewMatrix();
        invViewMatrix.set(viewMatrix);
        invViewMatrix.invert();
        tmpVec.mul(invViewMatrix);

        mouseDir.set(tmpVec.x, tmpVec.y, tmpVec.z);

        selectGameItem(gameItems, camera.getPosition(), mouseDir);
    }
     */
    public Vector4f getUnprojectedMousePos(WindowManager window) {
        Vector4f tmpVec = new Vector4f();
        Matrix4f invProjectionMatrix = new Matrix4f(window.updateProjectionMatrix());
        invProjectionMatrix.invert();

        Vector3f normalized = getNormalizedMousePos(window.getWidth(), window.getHeight());

        tmpVec.set(normalized.x, normalized.y, normalized.z, 1.0f);
        tmpVec.mul(invProjectionMatrix);
        tmpVec.z = -1.0f;
        tmpVec.w = 0.0f;

        return tmpVec;
    }

    public Vector3f getNormalizedMousePos(int windowWidth, int windowHeight) {
        float x = (float)(2 * currentPos.x) / (float) windowWidth - 1.0f;
        float y = 1.0f - (float)(2 * currentPos.y) / (float) windowHeight;
        float z = 1.0f;
        return new Vector3f(x, y, z);
    }

    public boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }
}
