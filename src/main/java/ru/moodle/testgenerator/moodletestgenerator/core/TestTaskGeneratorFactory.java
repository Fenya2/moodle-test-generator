package ru.moodle.testgenerator.moodletestgenerator.core;

import ru.moodle.testgenerator.moodletestgenerator.core.form.AddFastTestForm;

/**
 * Фабрика {@link TestTaskGenerator}
 *
 * @author dsyromyatnikov
 * @since 17.10.2025
 */
public interface TestTaskGeneratorFactory {
    TestTaskGenerator create(AddFastTestForm form);
}
