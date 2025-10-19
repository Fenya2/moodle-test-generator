package ru.moodle.testgenerator.moodletestgenerator.ui.previewform;

import javafx.scene.control.TextField;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.TerminalParameter;
import ru.moodle.testgenerator.moodletestgenerator.ui.DecimalField;

/**
 * Представление для предпросмотра и задания значений терминальным параметрам.
 * Содержит текстовое поле для имени параметра (унаследовано) и поле для ввода значения пользователем.
 *
 * @author dsyromyatnikov
 * @since 17.10.2025
 */
public final class TerminalParameterPreviewView extends AbstractParameterPreviewView {

    private final TextField valueField;

    public TerminalParameterPreviewView(TerminalParameter parameter) {
        super(parameter.getName());
        this.valueField = new DecimalField();
        String promptText = "%s <= %s <= %s (step: %s)".formatted(
                parameter.getMinValue(), parameter.getName(), parameter.getMaxValue(), parameter.getStep());
        this.valueField.setPromptText(promptText);
        this.getChildren().add(valueField);
    }

    /**
     * Получить текущее значение параметра в представлении
     *
     * @return строка с введённым значением
     */
    public String getParameterValue() {
        return valueField.getText();
    }

    /**
     * Устанавливает значение терминального параметра в его представлении
     *
     * @param value новое значение
     */
    public void setValue(String value) {
        valueField.setText(value);
    }
}