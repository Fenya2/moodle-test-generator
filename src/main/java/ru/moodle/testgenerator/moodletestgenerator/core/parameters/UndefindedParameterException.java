package ru.moodle.testgenerator.moodletestgenerator.core.parameters;

/**
 * Исключение, возникающее, когда на форме присутствует упоминание неописанного параметра
 */
public class UndefindedParameterException extends ParameterValidationException {
    public UndefindedParameterException(String message) {
        super(message);
    }
}
