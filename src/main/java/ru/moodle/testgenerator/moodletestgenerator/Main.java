package ru.moodle.testgenerator.moodletestgenerator;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;
import ru.moodle.testgenerator.moodletestgenerator.guice.AppModule;
import ru.moodle.testgenerator.moodletestgenerator.ui.NavigationServiceImpl;

import static ru.moodle.testgenerator.moodletestgenerator.ui.controllers.AddQuestionController.ADD_QUESTION_FORM_VIEW;

public class Main extends Application {
    private static final String WINDOW_TITLE = "Moodle test generator";

    @Override
    public void start(Stage primaryStage) throws Exception {
        Injector injector = Guice.createInjector(new AppModule(primaryStage));
        NavigationServiceImpl navigationServiceImpl = injector.getInstance(NavigationServiceImpl.class);
        navigationServiceImpl.navigateTo(ADD_QUESTION_FORM_VIEW);
        primaryStage.setTitle(WINDOW_TITLE);
        primaryStage.show();
    }
}
