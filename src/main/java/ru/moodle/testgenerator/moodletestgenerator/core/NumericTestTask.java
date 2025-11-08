package ru.moodle.testgenerator.moodletestgenerator.core;

import jakarta.annotation.Nullable;

/**
 * Тестовое упражнение с числовым вариантом ответа
 *
 * @author dsyromyatnikov
 * @since 16.10.2025
 */
public class NumericTestTask {
    /**
     * Имя задания
     */
    @Nullable
    private String name;

    /**
     * Вопрос
     */
    private final String question;

    /**
     * Ответ
     */
    private final String answer;

    /**
     * Погрешность ответа
     */
    private final String answerError;

    public NumericTestTask(String question, String answer, String answerError) {
        this.question = question;
        this.answer = answer;
        this.answerError = answerError;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public String getAnswerError() {
        return answerError;
    }
}
