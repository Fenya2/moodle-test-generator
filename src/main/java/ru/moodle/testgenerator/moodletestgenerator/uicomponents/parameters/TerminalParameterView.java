package ru.moodle.testgenerator.moodletestgenerator.uicomponents.parameters;

import ru.moodle.testgenerator.moodletestgenerator.core.parameters.ParameterType;

/**
 * Параметр, значение которого не зависит от других параметров
 *
 * @author dsyromyatnikov
 * @since 03.10.2025
 */
public final class TerminalParameterView extends AbstractParameterView
{
    @Override
    public ParameterType getType()
    {
        return ParameterType.TERMINAL;
    }
}
