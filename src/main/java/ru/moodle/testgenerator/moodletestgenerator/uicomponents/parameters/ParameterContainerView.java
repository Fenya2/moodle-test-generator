package ru.moodle.testgenerator.moodletestgenerator;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.ParameterType;
import ru.moodle.testgenerator.moodletestgenerator.uicomponents.parameters.ParameterRemovedEvent;

/**
 * Описание представления параметра, общее для всех видов параметров
 *
 * @author dsyromyatnikov
 * @since 03.10.2025
 */
public class ParameterContainerView extends VBox
{
    private static final int PARAMETER_SPACING = 5;
    private final TextField nameField;
    private final ComboBox<ParameterType> parameterTypes;

    public ParameterContainerView()
    {
        super(PARAMETER_SPACING);

        HBox firstRow = new HBox(PARAMETER_SPACING);
        Label nameLabel = new Label("Имя параметра");
        nameField = new TextField();
        nameField.setPromptText("param1");
        Button removeButton = new Button("–");
        removeButton.setOnAction(_ -> this.fireEvent(new ParameterRemovedEvent(this)));

        firstRow.getChildren().addAll(nameLabel, nameField, removeButton);

        HBox secondRow = new HBox(PARAMETER_SPACING);
        Label typeLabel = new Label("Тип параметра");
        parameterTypes = new ComboBox<>();
        parameterTypes.getItems().addAll(ParameterType.values());
        parameterTypes.setOnAction(_ ->
        {
            ParameterType selectedType = parameterTypes.getValue();
            onParameterTypeSelected(selectedType);
        });

        secondRow.getChildren().addAll(typeLabel, parameterTypes);

        this.getChildren().addAll(firstRow, secondRow);
    }

    public String getName()
    {
        return nameField.getText();
    }

    public ParameterType getSelectedType()
    {
        return parameterTypes.getValue();
    }

    public void setSelectedType(ParameterType type)
    {
        parameterTypes.setValue(type);
    }

    /**
     * Обработчик события выбора типа параметра
     *
     * @param selectedType выбранный тип параметра
     */
    private void onParameterTypeSelected(ParameterType selectedType)
    {
        switch (selectedType)
        {
            case TERMINAL -> System.out.println("terminal");
            case DEPENDENT -> System.out.println("dependent");
        }
    }
}