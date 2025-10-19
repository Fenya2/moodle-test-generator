package ru.moodle.testgenerator.moodletestgenerator.core.export;

public class ExportingException extends RuntimeException {
    public ExportingException(String message, Throwable e) {
        super(message, e);
    }
}
