package ru.moodle.testgenerator.moodletestgenerator.core.parameters;

import ru.moodle.testgenerator.moodletestgenerator.core.InputException;

/**
 * Исключение, связанное с неправильным описанием параметров
 *
 * @author dsyromyatnikov
 * @since 05.10.2025
 */
public class ParameterValidationException extends InputException
{
    public ParameterValidationException(String message)
    {
        super(message);
    }

    public ParameterValidationException(String message, Exception e)
    {
        super(message, e);
    }
}
