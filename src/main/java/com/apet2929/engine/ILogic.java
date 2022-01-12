package com.apet2929.engine;

public interface ILogic {

    void init() throws Exception;

    void input();

    void update(MouseInput mouseInput);

    void render();

    void cleanup();
}
