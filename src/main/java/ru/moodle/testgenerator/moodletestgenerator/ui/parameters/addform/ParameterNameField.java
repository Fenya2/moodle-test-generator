package ru.moodle.testgenerator.moodletestgenerator.ui.parameters.addform;

import java.util.regex.Pattern;

import javafx.scene.control.TextField;

/**
 * Поле ввода имени параметра. Ограничивает допустимые значения при вводе через слушатель
 *
 * @author dsyromyatnikov
 * @since 18.10.2025
 */
public class ParameterNameField extends TextField
{
    private static final Pattern VARIABLE_NAME_PATTERN = Pattern.compile("^[a-zA-Z_]\\w*$");

    public ParameterNameField()
    {
        textProperty().addListener((_, oldValue, newValue) ->
        {
            // Пустая строка также допускается (иначе нельзя изменить имя переменной с длиной 1)
            if (newValue.isEmpty())
            {
                return;
            }
            if (!VARIABLE_NAME_PATTERN.matcher(newValue).matches())
            {
                setText(oldValue);
            }
        });
    }
}
