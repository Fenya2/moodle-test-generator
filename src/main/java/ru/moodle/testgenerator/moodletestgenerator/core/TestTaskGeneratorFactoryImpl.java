package ru.moodle.testgenerator.moodletestgenerator.core;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ru.moodle.testgenerator.moodletestgenerator.core.form.AddQuestionForm;
import ru.moodle.testgenerator.moodletestgenerator.core.interpreter.ScriptCalculator;

/**
 * Фабрика генераторов вопросов
 *
 * @author dsyromyatnikov
 * @since 17.10.2025
 */
@Singleton
public class TestTaskGeneratorFactoryImpl implements TestTaskGeneratorFactory {
    private final ScriptCalculator scriptCalculator;

    @Inject
    public TestTaskGeneratorFactoryImpl(ScriptCalculator scriptCalculator) {
        this.scriptCalculator = scriptCalculator;
    }

    @Override
    public TestTaskGenerator create(AddQuestionForm form) {
        return new TestTaskGenerator(form, scriptCalculator);
    }
}
