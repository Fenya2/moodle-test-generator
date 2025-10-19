package ru.moodle.testgenerator.moodletestgenerator.ui;

import java.util.regex.Pattern;

/**
 * Текстовое поле ввода чисел
 *
 * @author dsyromyatnikov
 * @since 17.10.2025
 */
public class DecimalField extends RegexRestrictedTextField {
    public DecimalField() {
        super(Pattern.compile("^-?\\d*(\\.\\d*)?$"));
    }
}
