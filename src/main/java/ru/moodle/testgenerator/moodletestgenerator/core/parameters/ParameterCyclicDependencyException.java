package ru.moodle.testgenerator.moodletestgenerator.core.parameters;

/**
 * Исключение, возникающее при наличии циклической связи между параметрами
 */
public class ParameterCyclicDependencyException extends ParameterValidationException
{
    public ParameterCyclicDependencyException(String message)
    {
        super(message);
    }
}
