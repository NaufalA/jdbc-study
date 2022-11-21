package com.enigmacamp.shared.interfaces;

import java.util.function.Consumer;

public interface IPresenter {
    void mainMenu();

    void createMenu();

    void getAll(Consumer<Integer> nextAction);

    void getDetailMenu(Integer id);

    void removeMenu(Integer id);
}
