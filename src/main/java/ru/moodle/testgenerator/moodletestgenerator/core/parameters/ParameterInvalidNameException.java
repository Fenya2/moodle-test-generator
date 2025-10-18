package ru.moodle.testgenerator.moodletestgenerator.core.parameters;

/**
 * Исключение, возникающее при попытке задать параметр с недопустимым именем
 *
 * @author dsyromyatnikov
 * @since 16.10.2025
 */
public class ParameterInvalidNameException extends ParameterValidationException {
    public ParameterInvalidNameException(String message) {
        super(message);
    }
}
