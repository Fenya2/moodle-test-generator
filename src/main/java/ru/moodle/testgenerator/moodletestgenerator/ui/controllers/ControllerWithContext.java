package ru.moodle.testgenerator.moodletestgenerator.ui.controllers;

import jakarta.annotation.Nullable;

/**
 * FXML контроллер, которому может потребоваться контекст для работы
 */
public interface ControllerWithContext<T> {
    /**
     * Устанавливает контекст для работы контроллера
     */
    void setContext(@Nullable T context);
}
