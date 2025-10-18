package ru.moodle.testgenerator.moodletestgenerator.core.parameters;

/**
 * Исключение, возникающее в случае неверно заданных терминальных параметров при попытке сгенерировать вопрос
 *
 * @author dsyromyatnikov
 * @since 17.10.2025
 */
public class InvalidParameterValueException extends ParameterValidationException {
    public InvalidParameterValueException(String message) {
        super(message);
    }
}
