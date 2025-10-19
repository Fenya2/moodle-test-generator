package ru.moodle.testgenerator.moodletestgenerator.core.form;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.moodle.testgenerator.moodletestgenerator.core.TestTaskGenerator;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для {@link TestTaskGenerator}
 */
class TestTaskGeneratorTest {
    private static AddFastTestForm createQuestionForm(List<Parameter> parameters) {
        return new AddFastTestForm("Вопрос?", parameters, "Ответ");
    }

    private static TerminalParameter createTerminalParam(String name) {
        TerminalParameter param = new TerminalParameter(name);
        param.setMinValue(BigDecimal.valueOf(1));
        param.setMaxValue(BigDecimal.valueOf(10));
        param.setStep(BigDecimal.valueOf(1));
        return param;
    }

    private static TerminalParameter createTerminalParam(String name, int minValue, int maxValue, int step) {
        TerminalParameter param = new TerminalParameter(name);
        param.setMinValue(BigDecimal.valueOf(minValue));
        param.setMaxValue(BigDecimal.valueOf(maxValue));
        param.setStep(BigDecimal.valueOf(step));
        return param;
    }

    private static DependentParameter createDependentParam(String name, Set<String> dependencies) {
        DependentParameter param = new DependentParameter(name);
        param.setDependentParameters(dependencies);
        param.setEvaluationScript("");
        return param;
    }

    @Test
    @DisplayName("Успешное создание QuestionGenerator с корректными параметрами")
    void shouldCreateQuestionGenerator_WhenValidParameters() {
        List<Parameter> parameters = List.of(
                createTerminalParam("a"),
                createTerminalParam("b"),
                createDependentParam("c", Set.of("a", "b")),
                createDependentParam("d", Set.of("c")));
        AddFastTestForm form = createQuestionForm(parameters);
        assertDoesNotThrow(() -> new TestTaskGenerator(form, null, null));
    }

    @Test
    @DisplayName("Успешное создание с несвязным графом")
    void shouldCreateQuestionGenerator_WhenDisconnectedGraph() {
        List<Parameter> parameters = List.of(
                createTerminalParam("x"),
                createDependentParam("y", Set.of("x")),
                createTerminalParam("a"),
                createDependentParam("b", Set.of("a")),
                createDependentParam("c", Set.of("b")));
        AddFastTestForm form = createQuestionForm(parameters);
        assertDoesNotThrow(() -> new TestTaskGenerator(form, null, null));
    }

    @Test
    @DisplayName("Успешное создание только с терминальными параметрами")
    void shouldCreateQuestionGenerator_WhenOnlyTerminalParameters() {
        List<Parameter> parameters = List.of(
                createTerminalParam("param1"),
                createTerminalParam("param2"),
                createTerminalParam("param3"));

        AddFastTestForm form = createQuestionForm(parameters);
        assertDoesNotThrow(() -> new TestTaskGenerator(form, null, null));
    }

    @Test
    @DisplayName("Успешное создание только с зависимыми параметрами (без циклов)")
    void shouldThrowParameterValidationException_WhenOnlyDependentParameters() {
        List<Parameter> parameters = List.of(
                createDependentParam("first", Set.of()),
                createDependentParam("second", Set.of("first")),
                createDependentParam("third", Set.of("second")));
        AddFastTestForm form = createQuestionForm(parameters);
        assertThrows(UncomputableDependentParameterException.class, () -> new TestTaskGenerator(form, null, null));
    }

    @Test
    @DisplayName("Выбрасывает исключение при дубликатах имен параметров")
    void shouldThrowDuplicateParameterException_WhenDuplicateNames() {
        List<Parameter> parameters = List.of(
                createTerminalParam("duplicate"),
                createDependentParam("duplicate", Set.of()));
        AddFastTestForm form = createQuestionForm(parameters);
        DuplicateParameterException exception =
                assertThrows(DuplicateParameterException.class, () -> new TestTaskGenerator(form, null, null));
        assertEquals("На форме присутствуют параметры с одинаковым именем", exception.getMessage());
    }

    @Test
    @DisplayName("Выбрасывает исключение при нескольких дубликатах")
    void shouldThrowDuplicateParameterException_WhenMultipleDuplicates() {
        List<Parameter> parameters = List.of(
                createTerminalParam("a"),
                createTerminalParam("a"),
                createDependentParam("b", Set.of()),
                createDependentParam("b", Set.of("a")));
        AddFastTestForm form = createQuestionForm(parameters);
        assertThrows(DuplicateParameterException.class, () -> new TestTaskGenerator(form, null, null));
    }

    @Test
    @DisplayName("Выбрасывает исключение при простом цикле из 3 вершин")
    void shouldThrowParameterValidationException_WhenSimpleCycle() {
        List<Parameter> parameters = List.of(
                createDependentParam("a", Set.of("c")),
                createDependentParam("b", Set.of("a")),
                createDependentParam("c", Set.of("b")));
        AddFastTestForm form = createQuestionForm(parameters);
        ParameterValidationException exception = assertThrows(
                ParameterValidationException.class, () -> new TestTaskGenerator(form, null, null));
        assertEquals("Обнаружена циклическая зависимость между параметрами: a -> c -> b -> a", exception.getMessage());
    }

    @Test
    @DisplayName("Выбрасывает исключение при цикле из 2 вершин")
    void shouldThrowParameterValidationException_WhenTwoNodeCycle() {
        List<Parameter> parameters = List.of(
                createDependentParam("x", Set.of("y")),
                createDependentParam("y", Set.of("x")));

        AddFastTestForm form = createQuestionForm(parameters);
        ParameterValidationException exception =
                assertThrows(ParameterValidationException.class, () -> new TestTaskGenerator(form, null, null));
        assertEquals("Обнаружена циклическая зависимость между параметрами: x -> y -> x", exception.getMessage());
    }

    @Test
    @DisplayName("Выбрасывает исключение при взаимной зависимости")
    void shouldThrowParameterValidationException_WhenSelfCycle() {
        List<Parameter> parameters = List.of(createDependentParam("self", Set.of("self")));
        AddFastTestForm form = createQuestionForm(parameters);
        ParameterValidationException exception = assertThrows(
                ParameterValidationException.class, () -> new TestTaskGenerator(form, null, null));
        assertEquals("Обнаружена циклическая зависимость между параметрами: self -> self", exception.getMessage());
    }

    @Test
    @DisplayName("Выбрасывает исключение, если область определения терминального параметра пуста")
    void shouldThrowParameterValidationException_whenTerminalParameterHaveEmptyDefinitionArea() {
        List<Parameter> parameters = List.of(createTerminalParam("term", 1, 0, 1));
        AddFastTestForm form = createQuestionForm(parameters);
        ParameterValidationException exception = assertThrows(
                ParameterValidationException.class, () -> new TestTaskGenerator(form, null, null));
        assertEquals("Минимальное значение параметра term больше максимального", exception.getMessage());
    }

    @Test
    @DisplayName("Выбрасывает исключение, если шаг терминального параметра отрицательный")
    void shouldThrowParameterValidationException_whenTerminalParameterStepIsNegative() {
        List<Parameter> parameters = List.of(createTerminalParam("term", 0, 1, -1));
        AddFastTestForm form = createQuestionForm(parameters);
        NonPositiveTerminalParameterStepException exception = assertThrows(
                NonPositiveTerminalParameterStepException.class, () -> new TestTaskGenerator(form, null, null));
        assertEquals("Шаг параметра term не положительный", exception.getMessage());
    }

    @Test
    @DisplayName("Выбрасывает исключение, если шаг терминального параметра не положительный")
    void shouldThrowParameterValidationException_whenTerminalParameterStepIsZero() {
        List<Parameter> parameters = List.of(createTerminalParam("term", 0, 1, 0));
        AddFastTestForm form = createQuestionForm(parameters);
        NonPositiveTerminalParameterStepException exception = assertThrows(
                NonPositiveTerminalParameterStepException.class, () -> new TestTaskGenerator(form, null, null));
        assertEquals("Шаг параметра term не положительный", exception.getMessage());
    }


    @Test
    @DisplayName("Выбрасывает исключение, если имя параметра - пустая строка")
    void shouldThrowParameterInvalidNameException_whenParameterNameIsEmpty() {
        List<Parameter> parameters = List.of(createTerminalParam("", 0, 1, -1));
        AddFastTestForm form = createQuestionForm(parameters);
        ParameterInvalidNameException exception = assertThrows(
                ParameterInvalidNameException.class, () -> new TestTaskGenerator(form, null, null));
        assertEquals("Задано недопустимое имя параметру ''", exception.getMessage());
    }

    @Test
    @DisplayName("Выбрасывает исключение при сложном цикле в несвязном графе")
    void shouldThrowParameterValidationException_WhenCycleInDisconnectedGraph() {
        List<Parameter> parameters = List.of(
                createTerminalParam("valid1"),
                createDependentParam("valid2", Set.of("valid1")),
                createDependentParam("cycle1", Set.of("cycle3")),
                createDependentParam("cycle2", Set.of("cycle1")),
                createDependentParam("cycle3", Set.of("cycle2")));

        AddFastTestForm form = createQuestionForm(parameters);
        ParameterValidationException exception = assertThrows(
                ParameterValidationException.class, () -> new TestTaskGenerator(form, null, null));
        assertEquals("Обнаружена циклическая зависимость между параметрами: cycle3 -> cycle2 -> cycle1 -> cycle3",
                exception.getMessage());
    }

    @Test
    @DisplayName("Выбрасывает исключение при большом цикле")
    void shouldThrowParameterValidationException_WhenLargeCycle() {
        List<Parameter> parameters = List.of(
                createDependentParam("node1", Set.of("node5")),
                createDependentParam("node2", Set.of("node1")),
                createDependentParam("node3", Set.of("node2")),
                createDependentParam("node4", Set.of("node3")),
                createDependentParam("node5", Set.of("node4")));

        AddFastTestForm form = createQuestionForm(parameters);
        ParameterCyclicDependencyException exception = assertThrows(
                ParameterCyclicDependencyException.class,
                () -> new TestTaskGenerator(form, null, null));
        assertEquals("Обнаружена циклическая зависимость между параметрами: node4 -> node3 -> node2 -> node1 -> "
                + "node5 -> node4", exception.getMessage());
    }

    @Test
    @DisplayName("Выбрасывает исключение при смешанном графе с циклом")
    void shouldThrowParameterValidationException_WhenMixedGraphWithCycle() {
        List<Parameter> parameters = List.of(
                createTerminalParam("base"),
                createDependentParam("level1", Set.of("base")),
                createDependentParam("level2", Set.of("level1", "level3")),
                createDependentParam("level3", Set.of("level2")),
                createTerminalParam("independent"));
        AddFastTestForm form = createQuestionForm(parameters);
        ParameterValidationException exception =
                assertThrows(ParameterValidationException.class, () -> new TestTaskGenerator(form, null, null));
        assertEquals("Обнаружена циклическая зависимость между параметрами: level3 -> level2 -> level3",
                exception.getMessage());
    }

    @Test
    @DisplayName("Успешное создание с пустым списком параметров")
    void shouldCreateQuestionGenerator_WhenEmptyParameters() {
        List<Parameter> parameters = List.of();
        AddFastTestForm form = createQuestionForm(parameters);
        assertDoesNotThrow(() -> new TestTaskGenerator(form, null, null));
    }

    @Test
    @DisplayName("Успешное создание с одним параметром")
    void shouldCreateQuestionGenerator_WhenSingleParameter() {
        List<Parameter> parameters = List.of(createTerminalParam("single"));
        AddFastTestForm form = createQuestionForm(parameters);
        assertDoesNotThrow(() -> new TestTaskGenerator(form, null, null));
    }

    @Test
    @DisplayName("Бросает исключение, если в зависимостях присутствует неописанный параметр")
    void shouldThrowParameterValidationException_WhenDependencyOnNonExistentParameter() {
        List<Parameter> parameters = List.of(createDependentParam("valid", Set.of("undefined")));
        AddFastTestForm form = createQuestionForm(parameters);
        UndefindedParameterException exception =
                assertThrows(UndefindedParameterException.class, () -> new TestTaskGenerator(form, null, null));
        assertEquals("Параметр с именем undefined не задан", exception.getMessage());
    }

    @Test
    @DisplayName("Бросает исключение при попытке сгенерировать вопрос, не задав все терминальные параметры")
    void shouldThrowNotAllTerminalParametersDefinedException_WhenDependencyOnNonExistentParameter() {
        List<Parameter> parameters = List.of(createTerminalParam("a"));
        TestTaskGenerator testTaskGenerator = new TestTaskGenerator(createQuestionForm(parameters), null, null);
        Map<String, BigDecimal> parametersValues = Map.of();
        NotAllTerminalParametersDefinedException exception = assertThrows(
                NotAllTerminalParametersDefinedException.class,
                () -> testTaskGenerator.generateTestTask(parametersValues));
        assertEquals("Для генерации вопроса заданы не все терминальные параметры", exception.getMessage());
    }

    @Test
    @DisplayName("Бросает исключение при попытке задать некорректное значение терминального параметра")
    void shouldThrowInvalidParameterValueException_WhenInvalidParameterEntered() {
        List<Parameter> parameters = List.of(createTerminalParam("a", 0, 2, 1));
        TestTaskGenerator testTaskGenerator = new TestTaskGenerator(createQuestionForm(parameters), null, null);
        Map<String, BigDecimal> parametersValues = Map.of("a", new BigDecimal("0.5"));
        InvalidParameterValueException exception = assertThrows(
                InvalidParameterValueException.class,
                () -> testTaskGenerator.generateTestTask(parametersValues));
        assertEquals("Для параметра 'a' задано неверное значение", exception.getMessage());
    }
}