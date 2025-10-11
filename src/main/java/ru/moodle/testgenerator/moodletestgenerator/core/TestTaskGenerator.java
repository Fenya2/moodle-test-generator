package ru.moodle.testgenerator.moodletestgenerator.core;

import java.math.BigDecimal;
import java.math.MathContext;
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

import com.google.inject.Inject;

import jakarta.annotation.Nullable;
import ru.moodle.testgenerator.moodletestgenerator.core.form.AddQuestionForm;
import ru.moodle.testgenerator.moodletestgenerator.core.interpreter.ScriptCalculator;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.DependentParameter;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.DuplicateParameterException;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.EmptyParameterDefinitionAreaException;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.InvalidParameterValueException;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.NonPositiveTerminalParameterStepException;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.NotAllTerminalParametersDefinedException;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.Parameter;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.ParameterCyclicDependencyException;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.ParameterInvalidNameException;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.TerminalParameter;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.UncomputableDependentParameterException;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.UndefindedParameterException;

/**
 * Генератор вариантов, на основе заполненой {@link AddQuestionForm формы}
 *
 * @author dsyromyatnikov
 * @since 05.10.2025
 */
public class TestTaskGenerator
{
    /**
     * Форма, по которой построен генератор
     */
    private final AddQuestionForm form;
    /**
     * Отображение из имени параметра в его полное описание
     */
    private Map<String, Parameter> parameterDefinitions;

    /**
     * Граф зависимостей параметров
     */
    private final Map<String, Set<String>> parametersDependencyGraph;

    private final ScriptCalculator scriptCalculator;

    @Inject
    public TestTaskGenerator(AddQuestionForm form, ScriptCalculator scriptCalculator)
    {
        this.form = form;
        this.scriptCalculator = scriptCalculator;
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
        checkAllParametersHaveName();

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
        checkTerminalParametersDefinitionAreas();
        checkTerminalParametersStepsIsPositive();
    }

    private void checkAllParametersHaveName()
    {
        parameterDefinitions.values().stream()
                .filter(parameter -> parameter.getName().isEmpty())
                .findAny().ifPresent(parameter ->
                {
                    throw new ParameterInvalidNameException(
                            "Задано недопустимое имя параметру '%s'".formatted(parameter.getName()));
                });
    }

    /**
     * Проверяет, что область определения всех терминальных параметров не пуста
     */
    private void checkTerminalParametersDefinitionAreas()
    {
        getTerminalParametersStream()
                .filter(param -> param.getMinValue().compareTo(param.getMaxValue()) > 0)
                .findAny().ifPresent(emptyDefAreaParam ->
                {
                    throw new EmptyParameterDefinitionAreaException(
                            "Минимальное значение параметра %s больше максимального"
                                    .formatted(emptyDefAreaParam.getName()));
                });
    }

    /**
     * Проверяет, что значения шага всех {@link TerminalParameter терминальных} параметров больше 0
     */
    private void checkTerminalParametersStepsIsPositive()
    {
        getTerminalParametersStream()
                .filter(param -> param.getStep().compareTo(BigDecimal.ZERO) < 0)
                .findAny().ifPresent(emptyDefAreaParam ->
                {
                    throw new NonPositiveTerminalParameterStepException(
                            "Шаг параметра %s не положительный"
                                    .formatted(emptyDefAreaParam.getName()));
                });
    }

    /**
     * @return поток по {@link TerminalParameter терминальным параметрам}
     */
    private Stream<TerminalParameter> getTerminalParametersStream()
    {
        return parameterDefinitions.values().stream()
                .filter(TerminalParameter.class::isInstance)
                .map(TerminalParameter.class::cast);
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
     * Рекурсивно ищет цикл в графе {@link #parametersDependencyGraph} с использованием алгоритма обхода графа в глубину
     *
     * @param currentNode текущий узел графа
     * @param visited множество посещенных вершин
     * @param path текущий путь из вершин в графе
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

    public AddQuestionForm getForm()
    {
        return form;
    }

    /**
     * Генерирует вопрос по заданным значениям терминальных параметров, предварительно проверив, что они лежат в
     * допустимой области значений заданных пользователем терминальных параметров. Для этого рекурсивно вычисляет все
     * параметры
     */
    public Map<String, BigDecimal> generateTestTask(Map<String, BigDecimal> terminalParameterValues)
    {
        checkAllTerminalParametersDefinedCorrectly(terminalParameterValues);
        return calculateParameters(terminalParameterValues);
    }

    /**
     * Вычисляет значения всех параметров по заданным значениям терминальных параметров
     * @param terminalParameterValues значения всех терминальных параметров
     * @return карта вычисленных значений параметров
     */
    private Map<String, BigDecimal> calculateParameters(Map<String, BigDecimal> terminalParameterValues)
    {
        Map<String, BigDecimal> calculatedParameters = HashMap.newHashMap(parameterDefinitions.size());
        for (String parameterName : parameterDefinitions.keySet())
        {
            calculateParameter(parameterName, calculatedParameters, terminalParameterValues);
        }
        return calculatedParameters;
    }

    /**
     * Рекурсивно вычисляет значение параметра и всех параметров, от которых он зависит, если они не вычислялись ранее
     *
     * @param parameterName имя параметра
     * @param calculatedParameters карта вычисленных параметров
     * @param terminalParameterValues карта значений терминальных параметров
     */
    private void calculateParameter(String parameterName, Map<String, BigDecimal> calculatedParameters,
            Map<String, BigDecimal> terminalParameterValues)
    {
        if (calculatedParameters.containsKey(parameterName))
        {
            return;
        }

        Parameter parameter = parameterDefinitions.get(parameterName);

        if (parameter instanceof TerminalParameter)
        {
            calculatedParameters.put(parameterName, terminalParameterValues.get(parameterName));
            return;
        }

        Set<String> parameterDependencies = parametersDependencyGraph.get(parameterName);
        for (String dependency : parameterDependencies)
        {
            calculateParameter(dependency, calculatedParameters, terminalParameterValues);
        }

        Map<String, BigDecimal> dependenciesValues = parameterDependencies.stream()
                .collect(Collectors.toMap(Function.identity(), calculatedParameters::get));

        BigDecimal calculatedValue = scriptCalculator.calculateScript(
                ((DependentParameter)parameter).getCalculationScript(), dependenciesValues);
        calculatedParameters.put(parameterName, calculatedValue);
    }

    /**
     * Проверяет, что:
     * <li>Задано значения для каждого терминального параметра</li>
     * <li>Значеие каждого терминального параметра лежит в области возможных значений этого параметра</li>
     *
     * @param terminalParameterValues карта имя-значение терминальных параметров
     */
    private void checkAllTerminalParametersDefinedCorrectly(Map<String, BigDecimal> terminalParameterValues)
    {
        Set<String> terminalParameterNames = getTerminalParametersStream()
                .map(TerminalParameter::getName).collect(Collectors.toSet());
        if (!terminalParameterValues.keySet().equals(terminalParameterNames))
        {
            throw new NotAllTerminalParametersDefinedException(
                    "Для генерации вопроса заданы не все терминальные параметры");
        }

        terminalParameterValues.entrySet().stream()
                .filter(entry -> !parameterValueBelongsDefinitionScope(
                        (TerminalParameter)parameterDefinitions.get(entry.getKey()), entry.getValue()))
                .findAny().ifPresent(entry ->
                {
                    throw new InvalidParameterValueException(
                            "Для параметра '%s' задано неверное значение".formatted(entry.getKey()));
                });
    }

    /**
     * Определяет, принадлежит ли заданное значение терминального параметра его области определения
     * @param parameter параметр
     * @param value проверяемое значение
     * @return {@code true}, если значение принадлежит области определения. Иначе {@code false}
     */
    private static boolean parameterValueBelongsDefinitionScope(TerminalParameter parameter, BigDecimal value)
    {
        BigDecimal minValue = parameter.getMinValue();
        BigDecimal maxValue = parameter.getMaxValue();

        if (minValue.compareTo(value) > 0 || value.compareTo(maxValue) > 0)
        {
            return false;
        }

        BigDecimal step = parameter.getStep();

        // minValue + step * n = value
        // n = (value - minValue) / step
        // n должно оказаться целым
        BigDecimal delta = value.subtract(minValue);
        BigDecimal quotient;
        try
        {
            quotient = delta.divide(step, MathContext.DECIMAL128);
        }
        catch (ArithmeticException _)
        {
            return false;
        }

        return isInteger(quotient);
    }

    private static boolean isInteger(BigDecimal number)
    {
        return number.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0;
    }
}
