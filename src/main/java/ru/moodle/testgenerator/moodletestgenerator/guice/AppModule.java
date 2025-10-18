package ru.moodle.testgenerator.moodletestgenerator.guice;

import com.google.inject.AbstractModule;
import javafx.stage.Stage;
import ru.moodle.testgenerator.moodletestgenerator.ParameterRandomizerImpl;
import ru.moodle.testgenerator.moodletestgenerator.core.ParameterRandomizer;
import ru.moodle.testgenerator.moodletestgenerator.core.TestTaskGeneratorFactory;
import ru.moodle.testgenerator.moodletestgenerator.core.TestTaskGeneratorFactoryImpl;
import ru.moodle.testgenerator.moodletestgenerator.core.interpreter.JythonScriptCalculator;
import ru.moodle.testgenerator.moodletestgenerator.core.interpreter.ScriptCalculator;
import ru.moodle.testgenerator.moodletestgenerator.ui.NavigationService;
import ru.moodle.testgenerator.moodletestgenerator.ui.NavigationServiceImpl;

/**
 * Конфигурация guice
 *
 * @author dsyromyatnikov
 * @since 11.10.2025
 */
public class AppModule extends AbstractModule {
    /**
     * Корневой контейнер javaFX
     */
    private final Stage primaryStage;

    public AppModule(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    protected void configure() {
        bind(Stage.class).toInstance(primaryStage);
        bind(NavigationService.class).to(NavigationServiceImpl.class);
        bind(TestTaskGeneratorFactory.class).to(TestTaskGeneratorFactoryImpl.class);
        bind(ScriptCalculator.class).to(JythonScriptCalculator.class);
        bind(ParameterRandomizer.class).to(ParameterRandomizerImpl.class);
    }
}
