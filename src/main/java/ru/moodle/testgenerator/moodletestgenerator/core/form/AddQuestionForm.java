package ru.moodle.testgenerator.moodletestgenerator.core.form;

import ru.moodle.testgenerator.moodletestgenerator.core.parameters.Parameter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Форма добавления вопроса
 *
 * @author dsyromyatnikov
 * @since 03.10.2025
 */
public class AddQuestionForm {
    /**
     * Поле вопроса
     */
    private final String question;
    /**
     * Параметры на форме.
     */
    private final List<Parameter> parameters;
    /**
     * Поле ответа
     */
    private final String answer;

    public AddQuestionForm(String question, List<Parameter> parameters, String answer) {
        this.question = question;
        this.parameters = new ArrayList<>(parameters);
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public List<Parameter> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

    public String getAnswer() {
        return answer;
    }
}
