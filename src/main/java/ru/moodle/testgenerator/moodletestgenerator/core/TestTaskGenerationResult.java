package ru.moodle.testgenerator.moodletestgenerator.core;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

/**
 * Результат генерации {@link NumericTestTask},
 * выдаваемый {@link TestTaskGenerator#generateTestTask(Map)}
 *
 * @author dsyromyatnikov
 * @since 18.10.2025
 */
public class TestTaskGenerationResult {
    /**
     * Тестовое задание
     */
    private final NumericTestTask numericTestTask;
    /**
     * Параметры, по которым было построено задание.
     *
     * @apiNote являются неизменяемой картой
     */
    private final Map<String, BigDecimal> parameters;

    public TestTaskGenerationResult(NumericTestTask numericTestTask, Map<String, BigDecimal> parameters) {
        this.numericTestTask = numericTestTask;
        this.parameters = Collections.unmodifiableMap(parameters);
    }

    public NumericTestTask getTestTask() {
        return numericTestTask;
    }

    public Map<String, BigDecimal> getParameters() {
        return parameters;
    }
}
