package ru.moodle.testgenerator.moodletestgenerator.core.parameters;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Параметр, значения которого зависят от других параметров
 *
 * @author dsyromyatnikov
 * @since 03.10.2025
 */
public final class DependentParameter extends Parameter {
    /**
     * Имена параметров, от которых зависит данный параметр
     */
    private final Set<String> dependentParameterNames;

    /**
     * Скрипт, вычисляющий значение параметра
     */
    private String evaluationScript;

    public DependentParameter(String name) {
        super(name);
        this.dependentParameterNames = new HashSet<>();
    }

    /**
     * Добавляет параметры с перечисленными именами в зависимые параметры
     */
    public void setDependentParameters(Set<String> parameterNames) {
        dependentParameterNames.addAll(parameterNames);
    }

    /**
     * @return имена зависимых параметров
     * @implNote возвращается неизменяемая коллекция
     */
    public Set<String> getDependentParameterNames() {
        return Collections.unmodifiableSet(dependentParameterNames);
    }

    /**
     * @return скрипт, вычисляющий параметр
     */
    public String getCalculationScript() {
        return evaluationScript;
    }

    public void setEvaluationScript(String evaluationScript) {
        this.evaluationScript = evaluationScript;
    }
}
