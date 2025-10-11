package ru.moodle.testgenerator.moodletestgenerator.core.interpreter;

import java.math.BigDecimal;
import java.util.Map;

import jakarta.annotation.Nullable;

/**
 * Сервис вычисления скриптов
 *
 * @author dsyromyatnikov
 * @since 11.10.2025
 */
public interface ScriptCalculator
{
    /**
     * Вычисляет функцию нескольких переменных, заданную в виде скрипта на python
     *
     * @param calculationScript скрипт
     * @param dependenciesValues карта имя-значение переменных
     */
    @Nullable
    BigDecimal calculateScript(String calculationScript, Map<String, BigDecimal> dependenciesValues);
}
