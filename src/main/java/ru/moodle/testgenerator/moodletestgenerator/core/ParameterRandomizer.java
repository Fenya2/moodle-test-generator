package ru.moodle.testgenerator.moodletestgenerator.core;

import ru.moodle.testgenerator.moodletestgenerator.core.parameters.TerminalParameter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Сервис для получения случайных значений параметров
 *
 * @author dseromyatnikov
 * @since 17.10.2025
 */
public interface ParameterRandomizer {

    /**
     * Возвращает {@code count} наборов случайно сгенерированных значений параметров {@code parameters}
     */
    default List<Map<String, BigDecimal>> randomizeTerminalParameters(List<TerminalParameter> parameters, int count) {
        if (count < 0) {
            throw new InvalidTestCountException("Число вариантов для генерации не может быть отрицательным");
        }
        List<Map<String, BigDecimal>> randomizedParameters = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            randomizedParameters.add(randomizeTerminalParameters(parameters));
        }
        return randomizedParameters;
    }

    /**
     * Генерирует карту случайных значений терминальных параметров на основе заданной у этих параметров области значений
     *
     * @param parameters список терминальных параметров
     * @return карта имя-значение терминального параметра
     */
    Map<String, BigDecimal> randomizeTerminalParameters(List<TerminalParameter> parameters);

    /**
     * @param terminalParameter терминальный параметр
     * @return случайное значение переданного параметра из его области значений
     */
    BigDecimal randomizeTerminalParameter(TerminalParameter terminalParameter);
}
