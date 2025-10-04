package ru.moodle.testgenerator.moodletestgenerator.uicomponents.parameters;

import ru.moodle.testgenerator.moodletestgenerator.core.parameters.ParameterType;

/**
 * Параметр, значение которого зависит от других параметров
 *
 * @author dsyromyatnikov
 * @since 03.10.2025
 */
public final class DependentParameterView extends AbstractParameterView
{
    @Override
    public ParameterType getType()
    {
        return ParameterType.DEPENDENT;
    }
}
