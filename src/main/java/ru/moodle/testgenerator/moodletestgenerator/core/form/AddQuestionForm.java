package ru.moodle.testgenerator.moodletestgenerator.core.form;

import java.util.ArrayList;
import java.util.List;

import ru.moodle.testgenerator.moodletestgenerator.core.parameters.Parameter;

/**
 * Форма добавления вопроса
 *
 * @author dsyromyatnikov
 * @since 03.10.2025
 */
public class AddQuestionForm
{
    /**
     * Поле вопроса
     */
    private String question;
    /**
     * Параметры на форме.
     * @apiNote нуметруются с 0
     */
    private final List<Parameter> parameters;
    /**
     * Поле ответа
     */
    private String answer;

    public AddQuestionForm()
    {
        this.parameters = new ArrayList<>();
    }

    /**
     * Добавляет место для параметра на форму в конец списка
     */
    public void addParameterPlace()
    {
        parameters.add(null);
    }

    /**
     * @param parameterIndex номер параметра на форме
     * @return параметр под номером {@code intdex} на форме
     */
    public Parameter getParameterAt(int parameterIndex)
    {
        return parameters.get(parameterIndex);
    }

    /**
     * Обновляет параметр под номером
     */
    public void updateParameterAt(int parameterIndex, Parameter parameter)
    {
        parameters.set(parameterIndex, parameter);
    }

    /**
     * Удаляет парметр
     * @param parameterIndex
     */
    public void deleteParameterAt(int parameterIndex)
    {

    }
}
