package ru.moodle.testgenerator.moodletestgenerator.ui.parameters.previewform;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Представление для предпросмотра параметра, общее для всех типов параметров
 * Содержит текстовое поле для имени параметра
 *
 * @author dsyromyatnikov
 * @since 17.10.2025
 */
public abstract sealed class AbstractParameterPreviewView extends HBox
        permits TerminalParameterPreviewView, DependentParameterPreviewView
{

    private final Label parameterName;

    protected AbstractParameterPreviewView(String parameterName)
    {
        super(10);
        this.parameterName = new Label(parameterName);
        this.getChildren().addAll(this.parameterName, new Label(":"));
    }

    /**
     * @return имя параметра
     */
    public String getParameterName()
    {
        return parameterName.getText();
    }
}