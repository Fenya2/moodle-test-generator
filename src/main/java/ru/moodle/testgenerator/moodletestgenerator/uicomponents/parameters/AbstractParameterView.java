package ru.moodle.testgenerator.moodletestgenerator.uicomponents.parameters;

import javafx.scene.layout.VBox;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.ParameterType;

/**
 * Описание представления параметров типа
 * {@link ru.moodle.testgenerator.moodletestgenerator.core.parameters.ParameterType}
 *
 * @author dsyromyatnikov
 * @since 03.10.2025
 */
public abstract sealed class AbstractParameterView extends VBox permits TerminalParameterView, DependentParameterView
{
    public abstract ParameterType getType();
}
