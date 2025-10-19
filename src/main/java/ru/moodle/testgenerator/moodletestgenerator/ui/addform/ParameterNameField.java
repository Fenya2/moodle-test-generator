package ru.moodle.testgenerator.moodletestgenerator.ui.addform;

import ru.moodle.testgenerator.moodletestgenerator.ui.RegexRestrictedTextField;

import java.util.regex.Pattern;

/**
 * Поле ввода имени параметра. Ограничивает допустимые значения при вводе через слушатель
 *
 * @author dsyromyatnikov
 * @since 18.10.2025
 */
public class ParameterNameField extends RegexRestrictedTextField {
    public ParameterNameField() {
        super(Pattern.compile("^(?:[a-zA-Z_]\\w*|)$"));
    }
}
