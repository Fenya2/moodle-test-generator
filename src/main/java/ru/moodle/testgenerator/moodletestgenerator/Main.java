package ru.moodle.testgenerator.moodletestgenerator;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.moodle.testgenerator.moodletestgenerator.core.form.AddFastNumericTestForm;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.DependentParameter;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.TerminalParameter;
import ru.moodle.testgenerator.moodletestgenerator.guice.AppModule;
import ru.moodle.testgenerator.moodletestgenerator.ui.NavigationServiceImpl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static ru.moodle.testgenerator.moodletestgenerator.ui.controllers.AddTestTaskFormController.ADD_TASK_FORM_VIEW;

public class Main extends Application {
    private static final String ICON_PATH = "/icon.png";
    private static final String WINDOW_TITLE = "Moodle test generator";

    @Override
    public void start(Stage primaryStage) throws Exception {
        loadIcon(primaryStage);
        Injector injector = Guice.createInjector(new AppModule(primaryStage));
        NavigationServiceImpl navigationServiceImpl = injector.getInstance(NavigationServiceImpl.class);
        AddFastNumericTestForm startForm = createStartTestForm();
        navigationServiceImpl.navigateTo(ADD_TASK_FORM_VIEW, startForm);
        primaryStage.setTitle(WINDOW_TITLE);
        primaryStage.show();
    }

    private static void loadIcon(Stage primaryStage) {
        InputStream resourceAsStream = Main.class.getResourceAsStream(ICON_PATH);
        if (resourceAsStream == null) {
            System.out.println("Не удалось открыть иконку");
            return;
        }
        Image icon = new Image(resourceAsStream);
        primaryStage.getIcons().add(icon);
    }

    private static AddFastNumericTestForm createStartTestForm() {
        String answer = """
                Вычислить значение выражения: \\([[a]] + ([[b]])\\).
                Результат округлить до 1 знака после запятой.""";
        String step = "0.01";

        String aName = "a";
        TerminalParameter a = new TerminalParameter(aName);
        a.setMinValue(BigDecimal.ZERO);
        a.setMaxValue(new BigDecimal(3));
        a.setStep(new BigDecimal(step));

        String bName = "b";
        TerminalParameter b = new TerminalParameter(bName);
        b.setMinValue(new BigDecimal(-3));
        b.setMaxValue(BigDecimal.ZERO);
        b.setStep(new BigDecimal(step));

        DependentParameter result = new DependentParameter("result");
        result.setEvaluationScript("return a + b");
        result.setDependentParameters(Set.of(aName, bName));
        String answerError = "0.05";
        return new AddFastNumericTestForm(answer, List.of(a, b, result), "[[result]]", answerError);
    }
}
