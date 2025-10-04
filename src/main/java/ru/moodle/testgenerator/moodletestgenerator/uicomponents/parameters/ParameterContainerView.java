package ru.moodle.testgenerator.moodletestgenerator;

import java.util.List;

import jakarta.annotation.Nullable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.ParameterType;
import ru.moodle.testgenerator.moodletestgenerator.uicomponents.parameters.AbstractParameterView;
import ru.moodle.testgenerator.moodletestgenerator.uicomponents.parameters.DependentParameterView;
import ru.moodle.testgenerator.moodletestgenerator.uicomponents.parameters.ParameterRemovedEvent;
import ru.moodle.testgenerator.moodletestgenerator.uicomponents.parameters.TerminalParameterView;

/**
 * Описание представления параметра, общее для всех видов параметров
 *
 * @author dsyromyatnikov
 * @since 03.10.2025
 */
public class ParameterContainerView extends VBox
{
    private static final int PARAMETER_SPACING = 5;
    private final ComboBox<ParameterType> parameterTypes;
    /**
     * Контейнер, куда помещается значение параметра
     */
    private final VBox parameterPlace;

    public ParameterContainerView()
    {
        super(PARAMETER_SPACING);
        Button removeButton = new Button("–");
        removeButton.setOnAction(_ -> this.fireEvent(new ParameterRemovedEvent(this)));

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

        this.parameterPlace = new VBox();
        this.getChildren().addAll(removeButton, secondRow, parameterPlace);
    }

    /**
     * Обработчик события выбора типа параметра
     *
     * @param selectedType выбранный тип параметра
     */
    private void onParameterTypeSelected(ParameterType selectedType)
    {
        List<Node> place = parameterPlace.getChildren();
        if (!place.isEmpty())
        {
            place.clear();
        }
        switch (selectedType)
        {
            case TERMINAL -> place.add(new TerminalParameterView());
            case DEPENDENT -> place.add(new DependentParameterView());
        }
    }

    /**
     * @return заполненный параметр. {@code null}, если параметр не заполнялся
     */
    @Nullable
    public AbstractParameterView getFilledParameter()
    {
        return parameterPlace.getChildren().isEmpty()
                ? null
                : (AbstractParameterView)parameterPlace.getChildren().getFirst();
    }
}