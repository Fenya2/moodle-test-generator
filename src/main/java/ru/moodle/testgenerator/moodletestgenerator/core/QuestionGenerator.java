package ru.moodle.testgenerator.moodletestgenerator.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.annotation.Nullable;
import ru.moodle.testgenerator.moodletestgenerator.core.form.AddQuestionForm;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.DependentParameter;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.DuplicateParameterException;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.Parameter;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.ParameterCyclicDependencyException;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.TerminalParameter;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.UncomputableDependentParameterException;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.UndefindedParameterException;

/**
 * Генератор вариантов, на основе заполненой {@link AddQuestionForm формы}
 *
 * @author dsyromyatnikov
 * @since 05.10.2025
 */
public class QuestionGenerator
{
    /**
     * Отображение из имени параметра в его полное описание
     */
    private Map<String, Parameter> parameterDefinitions;

    /**
     * Граф зависимостей параметров
     */
    private Map<String, Set<String>> parametersDependencyGraph;

    public QuestionGenerator(AddQuestionForm form)
    {
        parametersDependencyGraph = new HashMap<>();
        validateAndPrepare(form);
    }

    /**
     * Проверяет форму на корректность введенных значений с точки зрения возможности вычисления всех параметров.
     * Для этого строит ориентированный граф зависимостей параметров и проверяет его на ацикличность. Вершины такого
     * графа соответствуют параметрам, введенным на форме. Дуга соединяет первую вершину со второй, если
     * соответсвующий первой вершине параметр зависит от параметра, соответствующего второй вершине.
     * Также подготавливает структуры для будущих вычислений параметров
     *
     * @implNote в таком графе вершины со степенью исхода 0 соответствуют {@link TerminalParameter терминальным
     * параметрам}. Такой граф не обязательно связный
     */
    private void validateAndPrepare(AddQuestionForm form)
    {
        List<Parameter> parameters = form.getParameters();
        try
        {
            parameterDefinitions = parameters.stream()
                    .collect(Collectors.toMap(Parameter::getName, Function.identity()));
        }
        catch (IllegalStateException e)
        {
            if (e.getMessage().startsWith("Duplicate"))
            {
                throw new DuplicateParameterException("На форме присутствуют параметры с одинаковым именем", e);
            }
            throw e;
        }

        parameterDefinitions.forEach((paramName, _) -> parametersDependencyGraph.put(paramName, new HashSet<>()));
        parameterDefinitions.forEach((paramName, parameter) ->
                parametersDependencyGraph.computeIfPresent(paramName, (_, oldValue) ->
                {
                    switch (parameter)
                    {
                        case TerminalParameter _ ->
                        {
                            return oldValue;
                        }
                        case DependentParameter dependentParameter ->
                        {
                            oldValue.addAll(dependentParameter.getDependentParameterNames());
                            return oldValue;
                        }
                    }
                }));

        checkGraphHaveNoUndefinedVertices();
        checkGraphHaveNoCycles();
        checkGraphLeavesIsTerminalParameters();
    }

    /**
     * Проверяет, что в построенном графе параметров каждой вершине соответствует описание параметра
     */
    private void checkGraphHaveNoUndefinedVertices()
    {
        parametersDependencyGraph.values().stream()
                .flatMap(Collection::stream)
                .filter(paramName -> !parameterDefinitions.containsKey(paramName))
                .findAny().ifPresent(paramName ->
                {
                    throw new UndefindedParameterException("Параметр с именем %s не задан".formatted(paramName));
                });
    }

    /**
     * Проверяет, что в {@link #parametersDependencyGraph графе зависимостей параметров} нет циклов
     */
    private void checkGraphHaveNoCycles()
    {
        Set<String> visited = new HashSet<>();

        for (String node : parametersDependencyGraph.keySet())
        {
            if (visited.contains(node))
            {
                continue;
            }
            List<String> cycle = findCycle(node, visited, new LinkedHashSet<>());
            if (cycle != null)
            {
                throw new ParameterCyclicDependencyException("Обнаружена циклическая зависимость между параметрами: " +
                                                             String.join(" -> ", cycle));
            }
        }
    }

    /**
     * Рекурсивно ищет цикл в графе {@link #parametersDependencyGraph} с использованием DFS
     *
     * @param currentNode текущий узел графа
     * @param visited     множество посещенных вершин
     * @param path        текущий путь из вершин в графе
     * @return найденный цикл. Иначе {@code null}
     */
    @Nullable
    private List<String> findCycle(String currentNode, Set<String> visited, LinkedHashSet<String> path)
    {
        if (path.contains(currentNode))
        {
            return Stream.concat(path.stream(), Stream.of(currentNode)).toList();
        }

        visited.add(currentNode);
        path.add(currentNode);

        for (String neighbor : parametersDependencyGraph.get(currentNode))
        {
            List<String> cycle = findCycle(neighbor, visited, path);
            if (cycle != null)
            {
                return cycle;
            }
        }
        path.removeLast();
        return null;
    }

    /**
     * Проверяет, что всем вершинам графа {@link #parametersDependencyGraph} со степенью исхода 0 соответствуют
     * {@link TerminalParameter} терминальные параметры
     */
    private void checkGraphLeavesIsTerminalParameters()
    {
        boolean haveOnlyTerminalsOnTail = parametersDependencyGraph.entrySet().stream()
                .filter(entry -> entry.getValue().isEmpty())
                .allMatch(entry -> parameterDefinitions.get(entry.getKey()) instanceof TerminalParameter);
        if (!haveOnlyTerminalsOnTail)
        {
            throw new UncomputableDependentParameterException(
                    "На форме есть связанный параметр, который невозможно вычислить, так как цепочка зависимостей "
                    + "этого параметра не ведет к терминальному параметру");
        }
    }
}
