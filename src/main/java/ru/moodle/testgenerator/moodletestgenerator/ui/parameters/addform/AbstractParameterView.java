package ru.moodle.testgenerator.moodletestgenerator.ui.parameters.addform;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Описание представления параметра, присущая любому типу параметра
 *
 * @author dsyromyatnikov
 * @since 03.10.2025
 */
public abstract sealed class AbstractParameterView extends VBox permits TerminalParameterView, DependentParameterView {
    private final TextField nameField;

    protected AbstractParameterView() {
        HBox nameRow = new HBox();
        Label nameLabel = new Label("Имя:");
        this.nameField = new ParameterNameField();
        this.nameField.setPromptText("param1");
        nameRow.getChildren().addAll(nameLabel, nameField);
        getChildren().add(nameRow);
    }

    /**
     * @return имя параметра
     */
    public String getName() {
        return this.nameField.getText();
    }

    /**
     * Устанавливает имя параметра
     */
    public void setName(String parameterName) {
        this.nameField.setText(parameterName);
    }
}
