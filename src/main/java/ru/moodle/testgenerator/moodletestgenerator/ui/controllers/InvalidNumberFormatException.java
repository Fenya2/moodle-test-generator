package ru.moodle.testgenerator.moodletestgenerator.ui.controllers;

import ru.moodle.testgenerator.moodletestgenerator.core.parameters.ParameterValidationException;

/**
 * Исключение, возникающее при неправильном формате заполнения значений параметров
 *
 * @author dsyromyatnikov
 * @since 11.10.2025
 */
public class InvalidNumberFormatException extends ParameterValidationException {
    public InvalidNumberFormatException(String messge) {
        super(messge);
    }
}
