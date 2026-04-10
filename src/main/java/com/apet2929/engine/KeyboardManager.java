package com.apet2929.engine;

import org.lwjgl.glfw.GLFW;

import java.util.HashMap;

public class KeyboardManager {
    public static final int[] KEYCODES = { GLFW.GLFW_KEY_SPACE,  GLFW.GLFW_KEY_APOSTROPHE,  GLFW.GLFW_KEY_COMMA,  GLFW.GLFW_KEY_MINUS,  GLFW.GLFW_KEY_PERIOD,  GLFW.GLFW_KEY_SLASH,  GLFW.GLFW_KEY_0,  GLFW.GLFW_KEY_1,  GLFW.GLFW_KEY_2,  GLFW.GLFW_KEY_3,  GLFW.GLFW_KEY_4,  GLFW.GLFW_KEY_5,  GLFW.GLFW_KEY_6,  GLFW.GLFW_KEY_7,  GLFW.GLFW_KEY_8,  GLFW.GLFW_KEY_9,  GLFW.GLFW_KEY_SEMICOLON,  GLFW.GLFW_KEY_EQUAL,  GLFW.GLFW_KEY_A,  GLFW.GLFW_KEY_B,  GLFW.GLFW_KEY_C,  GLFW.GLFW_KEY_D,  GLFW.GLFW_KEY_E,  GLFW.GLFW_KEY_F,  GLFW.GLFW_KEY_G,  GLFW.GLFW_KEY_H,  GLFW.GLFW_KEY_I,  GLFW.GLFW_KEY_J,  GLFW.GLFW_KEY_K,  GLFW.GLFW_KEY_L,  GLFW.GLFW_KEY_M,  GLFW.GLFW_KEY_N,  GLFW.GLFW_KEY_O,  GLFW.GLFW_KEY_P,  GLFW.GLFW_KEY_Q,  GLFW.GLFW_KEY_R,  GLFW.GLFW_KEY_S,  GLFW.GLFW_KEY_T,  GLFW.GLFW_KEY_U,  GLFW.GLFW_KEY_V,  GLFW.GLFW_KEY_W,  GLFW.GLFW_KEY_X,  GLFW.GLFW_KEY_Y,  GLFW.GLFW_KEY_Z,  GLFW.GLFW_KEY_LEFT_BRACKET,  GLFW.GLFW_KEY_BACKSLASH,  GLFW.GLFW_KEY_RIGHT_BRACKET,  GLFW.GLFW_KEY_GRAVE_ACCENT,  GLFW.GLFW_KEY_WORLD_1,  GLFW.GLFW_KEY_WORLD_2,  GLFW.GLFW_KEY_ESCAPE,  GLFW.GLFW_KEY_ENTER,  GLFW.GLFW_KEY_TAB,  GLFW.GLFW_KEY_BACKSPACE,  GLFW.GLFW_KEY_INSERT,  GLFW.GLFW_KEY_DELETE,  GLFW.GLFW_KEY_RIGHT,  GLFW.GLFW_KEY_LEFT,  GLFW.GLFW_KEY_DOWN,  GLFW.GLFW_KEY_UP};

    public static class KeyInfo {
        // state can be either RELEASE=0, PRESS=1, or REPEAT=2
        public int currentState;
        public int stateLastFrame;

        public KeyInfo() {
            this.currentState = GLFW.GLFW_RELEASE;
            this.stateLastFrame = GLFW.GLFW_RELEASE;
        }

        public void update(int currentState) {
            this.stateLastFrame = currentState;
            this.currentState = currentState;
        }
    }
    public static HashMap<Integer, KeyInfo> initKeyboard(){
        HashMap<Integer,KeyInfo> keyboard = new HashMap<>();
        for(int keycode : KEYCODES) {
            keyboard.put(keycode, new KeyInfo());
        }
        return keyboard;
    }

    private HashMap<Integer, KeyInfo> keyboard;

    public KeyboardManager() {
        this.keyboard = initKeyboard();
    }

    public void update(long window) {
        // Should be called before handling current frame's input
        for(int keycode : KEYCODES) {
            int status = GLFW.glfwGetKey(window, keycode);
            keyboard.get(keycode).update(status);
        }
    }

    public boolean isKeyPressed(int keycode) {
        KeyInfo key = keyboard.get(keycode);
        return key.currentState == GLFW.GLFW_PRESS || key.currentState == GLFW.GLFW_REPEAT;
    }

    public boolean isKeyReleased(int keycode) {
        return keyboard.get(keycode).currentState == GLFW.GLFW_RELEASE;
    }

    public boolean isKeyJustPressed(int keycode) {
        KeyInfo key = keyboard.get(keycode);
        return key.currentState == GLFW.GLFW_PRESS && key.stateLastFrame == GLFW.GLFW_RELEASE;
    }

    public boolean isKeyJustReleased(int keycode) {
        KeyInfo key = keyboard.get(keycode);
        return key.currentState == GLFW.GLFW_RELEASE &&
                (key.stateLastFrame == GLFW.GLFW_PRESS || key.stateLastFrame == GLFW.GLFW_REPEAT);
    }

    public boolean[] getNumbersPressed() {
        boolean[] numKeys = new boolean[10];
        int key;
        for (int i = 0; i < 10; i++) {
            key = 48 + i;
            numKeys[i] = isKeyPressed(key);
        }
        return numKeys;
    }


}
