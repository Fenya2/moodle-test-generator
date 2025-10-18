package ru.moodle.testgenerator.moodletestgenerator.core;

import java.io.Serial;

/**
 * Исключение, связанное с неправильным вводом пользователя
 *
 * @author dsyromyatnikov
 * @since 05.10.2025
 */
public class InputException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -1922040075838527352L;

    public InputException(String message) {
        super(message);
    }

    public InputException(String message, Exception e) {
        super(message, e);
    }
}
