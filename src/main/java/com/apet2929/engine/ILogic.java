package com.apet2929.engine;

public interface ILogic {

    void init() throws Exception;

    void input(MouseInput mouseInput);

    void update();

    void render();

    void cleanup();
}
