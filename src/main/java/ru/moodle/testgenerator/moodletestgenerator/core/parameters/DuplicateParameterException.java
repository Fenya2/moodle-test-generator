package ru.moodle.testgenerator.moodletestgenerator.core.parameters;

/**
 * Исключение, связанное с вводом параметров с одинаковым именем
 */
public class DuplicateParameterException extends ParameterValidationException
{
    public DuplicateParameterException(String message)
    {
        super(message);
    }

    public DuplicateParameterException(String message, Exception e)
    {
        super(message, e);
    }
}
