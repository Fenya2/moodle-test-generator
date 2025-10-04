package ru.moodle.testgenerator.moodletestgenerator.core.parameters;

/**
 * Тип параметра (по зависимости значения от других параметров)
 *
 * @author dsyromyatnikov
 * @since 03.10.2025
 */
public enum ParameterType
{
    /**
     * Значение параметра не зависит от значений других параметров
     */
    TERMINAL,
    /**
     * Значение параметра зависит от значений других параметров
     */
    DEPENDENT
}
