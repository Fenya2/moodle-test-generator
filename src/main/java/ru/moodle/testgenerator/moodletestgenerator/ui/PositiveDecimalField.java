package ru.moodle.testgenerator.moodletestgenerator.ui;

import java.util.regex.Pattern;

/**
 * Поле ввода положительных чисел
 *
 * @author dsyromyatnikov
 * @since 08.11.2025
 */
public class PositiveDecimalField extends RegexRestrictedTextField {
    public PositiveDecimalField() {
        super(Pattern.compile("^\\d*(\\.\\d*)?$"));
    }
}
