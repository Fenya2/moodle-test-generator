package ru.moodle.testgenerator.moodletestgenerator.ui;

import javafx.scene.control.TextField;

import java.util.regex.Pattern;

/**
 * Поле ввода, значение которого должно соответствовать маске, заданной с помощью регулярного выражения
 *
 * @author dsyromyatnikov
 * @since 19.10.2025
 */
public abstract class RegexRestrictedTextField extends TextField {
    protected RegexRestrictedTextField(Pattern pattern) {
        textProperty().addListener((_, oldValue, newValue) ->
        {
            if (!pattern.matcher(newValue).matches()) {
                setText(oldValue);
            }
        });
    }
}
