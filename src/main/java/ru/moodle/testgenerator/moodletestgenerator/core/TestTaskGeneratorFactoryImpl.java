package ru.moodle.testgenerator.moodletestgenerator.core;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ru.moodle.testgenerator.moodletestgenerator.core.form.AddFastTestForm;
import ru.moodle.testgenerator.moodletestgenerator.core.interpreter.ScriptCalculator;
import ru.moodle.testgenerator.moodletestgenerator.template.TemplateEngine;

/**
 * Фабрика генераторов вопросов
 *
 * @author dsyromyatnikov
 * @since 17.10.2025
 */
@Singleton
public class TestTaskGeneratorFactoryImpl implements TestTaskGeneratorFactory {
    private final ScriptCalculator scriptCalculator;
    private final TemplateEngine templateEngine;

    @Inject
    public TestTaskGeneratorFactoryImpl(ScriptCalculator scriptCalculator, TemplateEngine templateEngine) {
        this.scriptCalculator = scriptCalculator;
        this.templateEngine = templateEngine;
    }

    @Override
    public TestTaskGenerator create(AddFastTestForm form) {
        return new TestTaskGenerator(form, scriptCalculator, templateEngine);
    }
}
