package ru.moodle.testgenerator.moodletestgenerator.core.parameters;

import ru.moodle.testgenerator.moodletestgenerator.core.InputException;

/**
 * Исключение, возникающее, когда описан терминальный параметр с пустой областью определения.
 * То есть указанное минимальное значение параметра больше указанного максимального значения параметра
 *
 * @author dsyromyatnikov
 * @since 05.10.2025
 */
public class ParameterValidationException extends InputException {
    public ParameterValidationException(String message) {
        super(message);
    }

    public ParameterValidationException(String message, Exception e) {
        super(message, e);
    }
}
