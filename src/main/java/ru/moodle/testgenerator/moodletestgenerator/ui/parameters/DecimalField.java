package ru.moodle.testgenerator.moodletestgenerator.ui.parameters;

import java.util.regex.Pattern;

import javafx.scene.control.TextField;

/**
 * Текстовое поле ввода чисел
 *
 * @author dsyromyatnikov
 * @since 17.10.2025
 */
public class DecimalField extends TextField
{
    private static final Pattern FLOAT_NUMBER_PATTERN = Pattern.compile("-?\\d*(\\.\\d*)?");

    public DecimalField()
    {
        textProperty().addListener((_, oldValue, newValue) ->
        {
            if (!FLOAT_NUMBER_PATTERN.matcher(newValue).matches())
            {
                setText(oldValue);
            }
        });
    }
}
