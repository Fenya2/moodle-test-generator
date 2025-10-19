package ru.moodle.testgenerator.moodletestgenerator.ui.previewform;

import javafx.scene.control.Label;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.DependentParameter;

/**
 * Представление для предпросмотра зависимого параметра
 *
 * @author dsyromyatnikov
 * @since 17.10.2025
 */
public final class DependentParameterPreviewView extends AbstractParameterPreviewView {
    /**
     * Значение зависимого параметра
     */
    private final Label value;

    public DependentParameterPreviewView(DependentParameter parameter) {
        super(parameter.getName());
        this.value = new Label("<>");
        this.getChildren().add(value);
    }

    /**
     * Устанавливает значение параметра в его представлении
     */
    public void setParameterValue(String value) {
        this.value.setText(value);
    }
}
