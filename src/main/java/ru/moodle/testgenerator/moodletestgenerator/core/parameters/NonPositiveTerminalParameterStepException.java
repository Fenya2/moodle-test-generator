package ru.moodle.testgenerator.moodletestgenerator.core.parameters;

/**
 * Исключение, возникающее при отрицательно значении шага {@link TerminalParameter терминального параметра}
 */
public class NonPositiveTerminalParameterStepException extends ParameterValidationException
{
    public NonPositiveTerminalParameterStepException(String message)
    {
        super(message);
    }
}
