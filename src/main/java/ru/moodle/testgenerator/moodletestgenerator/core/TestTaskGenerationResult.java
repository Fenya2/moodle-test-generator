package ru.moodle.testgenerator.moodletestgenerator.core;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

/**
 * Результат генерации {@link ru.moodle.testgenerator.moodletestgenerator.core.TestTask},
 * выдаваемый {@link ru.moodle.testgenerator.moodletestgenerator.core.TestTaskGenerator#generateTestTask(Map)}
 *
 * @author dsyromyatnikov
 * @since 18.10.2025
 */
public class TestTaskGenerationResult {
    /**
     * Тестовое задание
     */
    private final TestTask testTask;
    /**
     * Параметры, по которым было построено задание.
     *
     * @apiNote являются неизменяемой картой
     */
    private final Map<String, BigDecimal> parameters;

    public TestTaskGenerationResult(TestTask testTask, Map<String, BigDecimal> parameters) {
        this.testTask = testTask;
        this.parameters = Collections.unmodifiableMap(parameters);
    }

    public TestTask getTestTask() {
        return testTask;
    }

    public Map<String, BigDecimal> getParameters() {
        return parameters;
    }
}
