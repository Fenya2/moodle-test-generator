package ru.moodle.testgenerator.moodletestgenerator.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ru.moodle.testgenerator.moodletestgenerator.ui.NavigationService;

/**
 * Контроллер, содержащий общую логику для остальных контроллеров
 *
 * @author dsyromyatnikov
 * @since 26.10.2025
 */
public abstract class AbstractControllerWithContext<T> implements ControllerWithContext<T> {

    private final NavigationService navigationService;

    @FXML
    private Label errorLabel;

    protected AbstractControllerWithContext(NavigationService navigationService) {
        this.navigationService = navigationService;
    }

    protected void navigateTo(String viewLocation, Object context) {
        navigationService.navigateTo(viewLocation, context);
    }

    protected void printError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    protected void hideError() {
        errorLabel.setText("");
        errorLabel.setVisible(false);
    }
}
