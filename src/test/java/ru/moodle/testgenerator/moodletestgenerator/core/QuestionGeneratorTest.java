package ru.moodle.testgenerator.moodletestgenerator.core.form;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ru.moodle.testgenerator.moodletestgenerator.core.QuestionGenerator;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.DependentParameter;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.DuplicateParameterException;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.Parameter;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.ParameterCyclicDependencyException;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.ParameterValidationException;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.TerminalParameter;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.UncomputableDependentParameterException;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.UndefindedParameterException;

/**
 * Тесты для {@link QuestionGenerator}
 */
class QuestionGeneratorTest
{
    @Test
    @DisplayName("Успешное создание QuestionGenerator с корректными параметрами")
    void shouldCreateQuestionGenerator_WhenValidParameters()
    {
        List<Parameter> parameters = List.of(
                createTerminalParam("a"),
                createTerminalParam("b"),
                createDependentParam("c", Set.of("a", "b")),
                createDependentParam("d", Set.of("c")));
        AddQuestionForm form = new AddQuestionForm("Вопрос?", parameters, "Ответ");
        assertDoesNotThrow(() -> new QuestionGenerator(form));
    }

    @Test
    @DisplayName("Успешное создание с несвязным графом")
    void shouldCreateQuestionGenerator_WhenDisconnectedGraph()
    {
        List<Parameter> parameters = List.of(
                createTerminalParam("x"),
                createDependentParam("y", Set.of("x")),
                createTerminalParam("a"),
                createDependentParam("b", Set.of("a")),
                createDependentParam("c", Set.of("b")));
        AddQuestionForm form = new AddQuestionForm("Вопрос?", parameters, "Ответ");
        assertDoesNotThrow(() -> new QuestionGenerator(form));
    }

    @Test
    @DisplayName("Успешное создание только с терминальными параметрами")
    void shouldCreateQuestionGenerator_WhenOnlyTerminalParameters()
    {
        List<Parameter> parameters = List.of(
                createTerminalParam("param1"),
                createTerminalParam("param2"),
                createTerminalParam("param3"));

        AddQuestionForm form = new AddQuestionForm("Вопрос?", parameters, "Ответ");
        assertDoesNotThrow(() -> new QuestionGenerator(form));
    }

    @Test
    @DisplayName("Успешное создание только с зависимыми параметрами (без циклов)")
    void shouldThrowParameterValidationException_WhenOnlyDependentParameters()
    {
        List<Parameter> parameters = List.of(
                createDependentParam("first", Set.of()),
                createDependentParam("second", Set.of("first")),
                createDependentParam("third", Set.of("second")));
        AddQuestionForm form = new AddQuestionForm("Вопрос?", parameters, "Ответ");
        assertThrows(UncomputableDependentParameterException.class, () -> new QuestionGenerator(form));
    }

    @Test
    @DisplayName("Выбрасывает исключение при дубликатах имен параметров")
    void shouldThrowDuplicateParameterException_WhenDuplicateNames()
    {
        List<Parameter> parameters = List.of(
                createTerminalParam("duplicate"),
                createDependentParam("duplicate", Set.of()));
        AddQuestionForm form = new AddQuestionForm("Вопрос?", parameters, "Ответ");
        DuplicateParameterException exception =
                assertThrows(DuplicateParameterException.class, () -> new QuestionGenerator(form));
        assertEquals("На форме присутствуют параметры с одинаковым именем", exception.getMessage());
    }

    @Test
    @DisplayName("Выбрасывает исключение при нескольких дубликатах")
    void shouldThrowDuplicateParameterException_WhenMultipleDuplicates()
    {
        List<Parameter> parameters = List.of(
                createTerminalParam("a"),
                createTerminalParam("a"),
                createDependentParam("b", Set.of()),
                createDependentParam("b", Set.of("a")));
        AddQuestionForm form = new AddQuestionForm("Вопрос?", parameters, "Ответ");
        assertThrows(DuplicateParameterException.class, () -> new QuestionGenerator(form));
    }

    @Test
    @DisplayName("Выбрасывает исключение при простом цикле из 3 вершин")
    void shouldThrowParameterValidationException_WhenSimpleCycle()
    {
        List<Parameter> parameters = List.of(
                createDependentParam("a", Set.of("c")),
                createDependentParam("b", Set.of("a")),
                createDependentParam("c", Set.of("b")));
        AddQuestionForm form = new AddQuestionForm("Вопрос?", parameters, "Ответ");
        ParameterValidationException exception = assertThrows(
                ParameterValidationException.class,
                () -> new QuestionGenerator(form));
        assertEquals("Обнаружена циклическая зависимость между параметрами: a -> c -> b -> a", exception.getMessage());
    }

    @Test
    @DisplayName("Выбрасывает исключение при цикле из 2 вершин")
    void shouldThrowParameterValidationException_WhenTwoNodeCycle()
    {
        List<Parameter> parameters = List.of(
                createDependentParam("x", Set.of("y")),
                createDependentParam("y", Set.of("x")));

        AddQuestionForm form = new AddQuestionForm("Вопрос?", parameters, "Ответ");
        ParameterValidationException exception =
                assertThrows(ParameterValidationException.class, () -> new QuestionGenerator(form));
        assertEquals("Обнаружена циклическая зависимость между параметрами: x -> y -> x", exception.getMessage());
    }

    @Test
    @DisplayName("Выбрасывает исключение при самозависимости")
    void shouldThrowParameterValidationException_WhenSelfCycle()
    {
        List<Parameter> parameters = List.of(createDependentParam("self", Set.of("self")));
        AddQuestionForm form = new AddQuestionForm("Вопрос?", parameters, "Ответ");
        ParameterValidationException exception = assertThrows(
                ParameterValidationException.class,
                () -> new QuestionGenerator(form));
        assertTrue(exception.getMessage().contains("Обнаружена циклическая зависимость"));
        assertEquals("Обнаружена циклическая зависимость между параметрами: self -> self", exception.getMessage());
    }

    @Test
    @DisplayName("Выбрасывает исключение при сложном цикле в несвязном графе")
    void shouldThrowParameterValidationException_WhenCycleInDisconnectedGraph()
    {
        List<Parameter> parameters = List.of(
                createTerminalParam("valid1"),
                createDependentParam("valid2", Set.of("valid1")),
                createDependentParam("cycle1", Set.of("cycle3")),
                createDependentParam("cycle2", Set.of("cycle1")),
                createDependentParam("cycle3", Set.of("cycle2")));

        AddQuestionForm form = new AddQuestionForm("Вопрос?", parameters, "Ответ");
        ParameterValidationException exception = assertThrows(
                ParameterValidationException.class,
                () -> new QuestionGenerator(form));
        assertEquals("Обнаружена циклическая зависимость между параметрами: cycle3 -> cycle2 -> cycle1 -> cycle3",
                exception.getMessage());
    }

    @Test
    @DisplayName("Выбрасывает исключение при большом цикле")
    void shouldThrowParameterValidationException_WhenLargeCycle()
    {
        List<Parameter> parameters = List.of(
                createDependentParam("node1", Set.of("node5")),
                createDependentParam("node2", Set.of("node1")),
                createDependentParam("node3", Set.of("node2")),
                createDependentParam("node4", Set.of("node3")),
                createDependentParam("node5", Set.of("node4")));

        AddQuestionForm form = new AddQuestionForm("Вопрос?", parameters, "Ответ");
        ParameterCyclicDependencyException exception = assertThrows(
                ParameterCyclicDependencyException.class,
                () -> new QuestionGenerator(form));
        assertEquals("Обнаружена циклическая зависимость между параметрами: node4 -> node3 -> node2 -> node1 -> "
                     + "node5 -> node4", exception.getMessage());
    }

    @Test
    @DisplayName("Выбрасывает исключение при смешанном графе с циклом")
    void shouldThrowParameterValidationException_WhenMixedGraphWithCycle()
    {
        List<Parameter> parameters = List.of(
                createTerminalParam("base"),
                createDependentParam("level1", Set.of("base")),
                createDependentParam("level2", Set.of("level1", "level3")),
                createDependentParam("level3", Set.of("level2")),
                createTerminalParam("independent"));
        AddQuestionForm form = new AddQuestionForm("Вопрос?", parameters, "Ответ");
        ParameterValidationException exception =
                assertThrows(ParameterValidationException.class, () -> new QuestionGenerator(form));
        assertEquals("Обнаружена циклическая зависимость между параметрами: level3 -> level2 -> level3",
                exception.getMessage());
    }

    // ===== ТЕСТ 4: ГРАНИЧНЫЕ СЛУЧАИ =====

    @Test
    @DisplayName("Успешное создание с пустым списком параметров")
    void shouldCreateQuestionGenerator_WhenEmptyParameters()
    {
        List<Parameter> parameters = List.of();
        AddQuestionForm form = new AddQuestionForm("Вопрос?", parameters, "Ответ");
        assertDoesNotThrow(() -> new QuestionGenerator(form));
    }

    @Test
    @DisplayName("Успешное создание с одним параметром")
    void shouldCreateQuestionGenerator_WhenSingleParameter()
    {
        List<Parameter> parameters = List.of(createTerminalParam("single"));
        AddQuestionForm form = new AddQuestionForm("Вопрос?", parameters, "Ответ");
        assertDoesNotThrow(() -> new QuestionGenerator(form));
    }

    @Test
    @DisplayName("Успешное создание с зависимостью от несуществующего параметра")
    void shouldThrowParameterValidationException_WhenDependencyOnNonExistentParameter()
    {
        List<Parameter> parameters = List.of(createDependentParam("valid", Set.of("undefined")));
        AddQuestionForm form = new AddQuestionForm("Вопрос?", parameters, "Ответ");
        UndefindedParameterException exception =
                assertThrows(UndefindedParameterException.class, () -> new QuestionGenerator(form));
        assertEquals("Параметр с именем undefined не задан", exception.getMessage());
    }

    private static TerminalParameter createTerminalParam(String name)
    {
        TerminalParameter param = new TerminalParameter(name);
        param.setMinValue(BigDecimal.valueOf(1));
        param.setMaxValue(BigDecimal.valueOf(10));
        param.setStep(BigDecimal.valueOf(1));
        return param;
    }

    private static DependentParameter createDependentParam(String name, Set<String> dependencies)
    {
        DependentParameter param = new DependentParameter(name);
        param.setDependentParameters(dependencies);
        param.setEvaluationScript("");
        return param;
    }
}