package ru.moodle.testgenerator.moodletestgenerator.ui.addform;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import ru.moodle.testgenerator.moodletestgenerator.ui.DecimalField;

/**
 * Параметр, значение которого не зависит от других параметров
 *
 * @author dsyromyatnikov
 * @since 03.10.2025
 */
public final class TerminalParameterView extends AbstractParameterView {
    private final TextField minValueField;
    private final TextField maxValueField;
    private final TextField stepField;

    public TerminalParameterView() {
        HBox minRow = new HBox(5);
        Label minLabel = new Label("Минимальное значение:");
        minValueField = new DecimalField();
        minValueField.setPromptText("0.0");
        minRow.getChildren().addAll(minLabel, minValueField);

        // Вторая строка: максимальное значение
        HBox maxRow = new HBox(5);
        Label maxLabel = new Label("Максимальное значение:");
        maxValueField = new DecimalField();
        maxValueField.setPromptText("1.0");
        maxRow.getChildren().addAll(maxLabel, maxValueField);

        // Третья строка: шаг
        HBox stepRow = new HBox(5);
        Label stepLabel = new Label("Шаг:");
        stepField = new DecimalField();
        stepField.setPromptText("0.1");
        stepRow.getChildren().addAll(stepLabel, stepField);

        // Устанавливаем контейнер как корневой элемент
        this.getChildren().addAll(minRow, maxRow, stepRow);
    }

    /**
     * Возвращает максимальное значение параметра
     */
    public String getMaxValue() {
        return maxValueField.getText();
    }

    /**
     * Устанавливает максимальное значение параметра
     */
    public void setMaxValue(String maxValue) {
        maxValueField.setText(maxValue);
    }

    /**
     * Возвращает минимальное значение параметра
     */
    public String getMinValue() {
        return minValueField.getText();
    }

    /**
     * Устанавливает минимальное значение параметра
     */
    public void setMinValue(String minValue) {
        minValueField.setText(minValue);
    }

    /**
     * Возвращает шаг параметра
     */
    public String getStep() {
        return stepField.getText();
    }

    /**
     * Устанавливает шаг параметра
     */
    public void setStep(String step) {
        stepField.setText(step);
    }
}