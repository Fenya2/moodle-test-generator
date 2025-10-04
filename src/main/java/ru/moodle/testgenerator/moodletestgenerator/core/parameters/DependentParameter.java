package ru.moodle.testgenerator.moodletestgenerator.core.parameters;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Параметр, значения которого зависят от других параметров
 *
 * @author dsyromyatnikov
 * @since 03.10.2025
 */
public final class DependentParameter extends Parameter
{
    /**
     * Имена параметров, от которых зависит данный параметр
     */
    private final Set<String> dependentParameterNames;

    public DependentParameter(String name)
    {
        super(name);
        this.dependentParameterNames = new HashSet<>();
    }

    /**
     * Добавляет параметры с перечисленными именами в зависимые параметры
     */
    public void addDependentParameters(String... parameterNames)
    {
        dependentParameterNames.addAll(Arrays.asList(parameterNames));
    }

    /**
     * Удаляет параметры с перечисленными именами из зависимых для текущего
     */
    public void removeDependentParameters(String... parameterNames)
    {
        Arrays.asList(parameterNames).forEach(dependentParameterNames::remove);
    }

    /**
     * @return имена зависимых параметров
     * @implNote возвращается неизменяемая коллекция
     */
    public Set<String> getDependentParameters()
    {
        return Collections.unmodifiableSet(dependentParameterNames);
    }
}
