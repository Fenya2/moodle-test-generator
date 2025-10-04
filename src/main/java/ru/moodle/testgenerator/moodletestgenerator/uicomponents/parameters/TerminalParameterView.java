package ru.moodle.testgenerator.moodletestgenerator.uicomponents.parameters;

import java.util.regex.Pattern;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Параметр, значение которого не зависит от других параметров
 *
 * @author dsyromyatnikov
 * @since 03.10.2025
 */
public final class TerminalParameterView extends AbstractParameterView
{
    private static final Pattern FLOAT_NUMBER_PATTERN = Pattern.compile("-?\\d*(\\.\\d*)?");
    private final TextField minValueField;
    private final TextField maxValueField;
    private final TextField stepField;

    public TerminalParameterView()
    {
        HBox minRow = new HBox(5);
        Label minLabel = new Label("Минимальное значение:");
        minValueField = new TextField();
        minValueField.setPromptText("0.0");
        setupNumericField(minValueField);
        minRow.getChildren().addAll(minLabel, minValueField);

        // Вторая строка: максимальное значение
        HBox maxRow = new HBox(5);
        Label maxLabel = new Label("Максимальное значение:");
        maxValueField = new TextField();
        maxValueField.setPromptText("1.0");
        setupNumericField(maxValueField);
        maxRow.getChildren().addAll(maxLabel, maxValueField);

        // Третья строка: шаг
        HBox stepRow = new HBox(5);
        Label stepLabel = new Label("Шаг:");
        stepField = new TextField();
        stepField.setPromptText("0.1");
        setupNumericField(stepField);
        stepRow.getChildren().addAll(stepLabel, stepField);

        // Устанавливаем контейнер как корневой элемент
        this.getChildren().addAll(minRow, maxRow, stepRow);
    }

    /**
     * Настраивает текстовое поле для ввода только числовых значений
     *
     * @param field текстовое поле для настройки
     */
    private static void setupNumericField(TextField field)
    {
        field.textProperty().addListener((_, oldValue, newValue) ->
        {
            if (!FLOAT_NUMBER_PATTERN.matcher(newValue).matches())
            {
                field.setText(oldValue);
            }
        });
    }

    /**
     * Возвращает максимальное значение параметра
     */
    public String getMaxValue()
    {
        return maxValueField.getText();
    }

    /**
     * Возвращает минимальное значение параметра
     */
    public String getMinValue()
    {
        return minValueField.getText();
    }

    /**
     * Возвращает шаг параметра
     */
    public String getStep()
    {
        return stepField.getText();
    }
}