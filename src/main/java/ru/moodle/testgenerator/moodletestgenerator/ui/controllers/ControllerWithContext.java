package ru.moodle.testgenerator.moodletestgenerator.ui.controllers;

/**
 * FXML контроллер, которому может потребоваться контекст для работы
 */
public interface ControllerWithContext<T> {
    /**
     * Устанавливает контекст для работы контроллера
     */
    void setContext(T context);
}
