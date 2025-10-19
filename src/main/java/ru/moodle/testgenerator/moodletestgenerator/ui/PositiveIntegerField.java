package ru.moodle.testgenerator.moodletestgenerator.ui;

import java.util.regex.Pattern;

/**
 * Поле ввода целого положительного числа
 *
 * @author dsyromyatnikov
 * @since 19.10.2025
 */
public class PositiveIntegerField extends RegexRestrictedTextField {
    public PositiveIntegerField() {
        super(Pattern.compile("^\\d*$"));
    }
}
