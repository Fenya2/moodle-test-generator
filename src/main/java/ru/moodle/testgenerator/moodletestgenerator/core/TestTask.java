package ru.moodle.testgenerator.moodletestgenerator.core;

/**
 * Тестовое упражнение с единственным текстовым вариантом ответа
 *
 * @author dsyromyatnikov
 * @since 16.10.2025
 */
public class TestTask {
    /**
     * Вопрос
     */
    private final String question;

    /**
     * Ответ
     */
    private final String answer;

    public TestTask(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
