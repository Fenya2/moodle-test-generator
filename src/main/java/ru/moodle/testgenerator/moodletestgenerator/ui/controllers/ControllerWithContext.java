package ru.moodle.testgenerator.moodletestgenerator.ui.controllers;

/**
 * FXML контроллер, которому может потребоваться контекст для работы
 */
public interface ControllerWithContext {
    /**
     * Устанавливает контекст для работы контроллера
     */
    void setContext(Object context);
}
