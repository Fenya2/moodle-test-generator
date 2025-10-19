package ru.moodle.testgenerator.moodletestgenerator.core;

/**
 * Событие возникающее при вводе некорректного числа тестов для экспорта
 */
public class InvalidTestCountException extends InputException {
    public InvalidTestCountException(String message) {
        super(message);
    }
}
