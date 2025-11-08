package ru.moodle.testgenerator.moodletestgenerator.core;

import ru.moodle.testgenerator.moodletestgenerator.core.form.AddFastNumericTestForm;

/**
 * Фабрика {@link NumericTestTaskGenerator}
 *
 * @author dsyromyatnikov
 * @since 17.10.2025
 */
public interface TestTaskGeneratorFactory {
    NumericTestTaskGenerator create(AddFastNumericTestForm form);
}
