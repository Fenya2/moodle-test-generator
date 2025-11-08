package ru.moodle.testgenerator.moodletestgenerator.core.form;

import ru.moodle.testgenerator.moodletestgenerator.core.parameters.Parameter;

import java.util.Collections;
import java.util.List;

/**
 * Форма добавления вопроса с числовым вариантом ответа
 *
 * @param question    параметризованный текст вопроса
 * @param parameters  параметры на форме
 * @param answer      параметризованный ответ (число)
 * @param answerError погрешность ответа
 * @author dsyromyatnikov
 * @since 03.10.2025
 */
public record AddFastNumericTestForm(String question, List<Parameter> parameters, String answer, String answerError) {
    @Override
    public List<Parameter> parameters() {
        return Collections.unmodifiableList(parameters);
    }
}
