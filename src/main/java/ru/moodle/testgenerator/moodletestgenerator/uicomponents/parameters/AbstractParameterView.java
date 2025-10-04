package ru.moodle.testgenerator.moodletestgenerator.uicomponents.parameters;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Описание представления общая часть, присущая любому типу параметра
 *
 * @author dsyromyatnikov
 * @since 03.10.2025
 */
public abstract sealed class AbstractParameterView extends VBox permits TerminalParameterView, DependentParameterView
{
    private final TextField nameField;

    protected AbstractParameterView()
    {
        HBox nameRow = new HBox();
        Label nameLabel = new Label("Имя:");
        this.nameField = new TextField();
        this.nameField.setPromptText("param1");
        nameRow.getChildren().addAll(nameLabel, nameField);
        getChildren().add(nameRow);
    }

    public String getName()
    {
        return nameField.getText();
    }
}
