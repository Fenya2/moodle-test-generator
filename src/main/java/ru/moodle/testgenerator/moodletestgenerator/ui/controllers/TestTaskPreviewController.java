package ru.moodle.testgenerator.moodletestgenerator.ui.controllers;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import ru.moodle.testgenerator.moodletestgenerator.core.NumericTestTask;
import ru.moodle.testgenerator.moodletestgenerator.core.TestTaskGenerator;
import ru.moodle.testgenerator.moodletestgenerator.core.ParameterRandomizer;
import ru.moodle.testgenerator.moodletestgenerator.core.TestTaskGenerationResult;
import ru.moodle.testgenerator.moodletestgenerator.core.form.AddFastNumericTestForm;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.DependentParameter;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.Parameter;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.TerminalParameter;
import ru.moodle.testgenerator.moodletestgenerator.ui.NavigationService;
import ru.moodle.testgenerator.moodletestgenerator.ui.previewform.DependentParameterPreviewView;
import ru.moodle.testgenerator.moodletestgenerator.ui.previewform.QuestionAreaPreviewView;
import ru.moodle.testgenerator.moodletestgenerator.ui.previewform.TerminalParameterPreviewView;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import static ru.moodle.testgenerator.moodletestgenerator.ui.controllers.AddTestTaskFormController.ADD_TASK_FORM_VIEW;
import static ru.moodle.testgenerator.moodletestgenerator.ui.controllers.ExportTaskFormController.EXPORT_TASK_VIEW_FORM_VIEW;

/**
 * Контроллер формы предпросмотра составленного вопроса. Подразумевается, что пользователь уже описал вопрос и по
 * вопросу сгенерировался генератор {@link TestTaskGenerator}. То есть контроллер начинает работу с контекстом
 *
 * @author dsyromyatnikov
 * @since 11.10.2025
 */
public class TestTaskPreviewController extends AbstractControllerWithContext<TestTaskGenerator> implements Initializable {
    /**
     * Представление, которое обрабатывает контроллер
     */
    public static final String ADD_TASK_PREVIEW_FORM_VIEW = "/fxml/addTaskPreviewFormView.fxml";

    private final ParameterRandomizer parameterRandomizer;

    /**
     * Представление с записанным вопросом задания
     */
    @FXML
    public QuestionAreaPreviewView questionView;
    @FXML
    public VBox terminalParamsContainer;
    @FXML
    public VBox dependentParamsContainer;
    /**
     * Представление с записанным ответом на задание
     */
    @FXML
    public TextArea answerTextArea;
    private TestTaskGenerator testTaskGenerator;

    @Inject
    public TestTaskPreviewController(NavigationService navigationService, ParameterRandomizer parameterRandomizer) {
        super(navigationService);
        this.parameterRandomizer = parameterRandomizer;
    }

    @Override
    public void setContext(TestTaskGenerator context) {
        this.testTaskGenerator = context;
    }

    /**
     * Обрабатывает событие успешной загрузки сцены. Заполняет форму указанными параметрами с представлениями для
     * предпросмотра
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AddFastNumericTestForm form = getQuestionForm();
        List<Node> terminalParameters = terminalParamsContainer.getChildren();
        List<Node> dependentParameters = dependentParamsContainer.getChildren();
        for (Parameter parameter : form.parameters()) {
            switch (parameter) {
                case TerminalParameter terminalParameter ->
                        terminalParameters.add(new TerminalParameterPreviewView(terminalParameter));
                case DependentParameter dependentParameter ->
                        dependentParameters.add(new DependentParameterPreviewView(dependentParameter));
            }
        }
    }

    /**
     * Обрабатывает событие нажатия на кнопку случайного заполнения терминальных параметров
     */
    @FXML
    public void onFillRandomClick() {
        List<TerminalParameter> terminalParameters = getTerminalParametersOnForm();
        Map<String, BigDecimal> randomParametersValues = parameterRandomizer.randomizeTerminalParameters(
                terminalParameters);
        getTerminalParameterPreviewViewStream().forEach(view ->
        {
            String parameterName = view.getParameterName();
            view.setValue(randomParametersValues.get(parameterName).toPlainString());
        });
        hideError();
    }

    /**
     * Обрабатывает событие нажатия на кнопку "Рассчитать вопрос".
     * <li>Собирает с формы значения терминальных параметров. Если какой-то из параметров не указан, генерирует
     * случайное значение</li>
     * <li>Вычисляет на основе терминальных параметров зависимые параметры, вопрос и ответ</li>
     * <li>Выводит вычисленные параметры, вопрос и ответ</li>
     */
    @FXML
    public void onCalculateClick() {
        Map<String, BigDecimal> terminalParamsValues = collectTerminalParamsValuesOnForm();
        TestTaskGenerationResult result;
        try {
            result = testTaskGenerator.generateTestTask(terminalParamsValues);
        } catch (Exception e) {
            printError(e.getMessage());
            return;
        }
        NumericTestTask task = result.getTestTask();
        questionView.setText(task.getQuestion());
        answerTextArea.setText(task.getAnswer() + "±" + getQuestionForm().answerError());
        Map<String, BigDecimal> calculatedParameters = result.getParameters();
        getTerminalParameterPreviewViewStream().forEach(view ->
                view.setValue(calculatedParameters.get(view.getParameterName()).toPlainString()));
        getDependentParameterPreviewViewStream().forEach(
                view -> view.setParameterValue(calculatedParameters.get(view.getParameterName()).toPlainString()));
        hideError();
    }

    /**
     * Сначала вычисляет случайные значения всех терминальных параметров, затем часть заменяет значениями, выведенными
     * в представлениях терминальных параметров
     */
    private Map<String, BigDecimal> collectTerminalParamsValuesOnForm() {
        Map<String, BigDecimal> randomizedValues = parameterRandomizer.randomizeTerminalParameters(
                getTerminalParametersOnForm());
        getTerminalParameterPreviewViewStream().forEach(view ->
                {
                    String parameterValue = view.getParameterValue();
                    try {
                        if (!parameterValue.isEmpty()) {
                            randomizedValues.put(view.getParameterName(), new BigDecimal(parameterValue));
                        }
                    } catch (Exception e) {
                        printError(e.getMessage());
                    }
                }
        );
        return randomizedValues;
    }

    /**
     * @return поток открытых представлений для предпросмотра терминальных параметров
     */
    private Stream<TerminalParameterPreviewView> getTerminalParameterPreviewViewStream() {
        return terminalParamsContainer.getChildren().stream()
                .map(TerminalParameterPreviewView.class::cast);
    }

    private Stream<DependentParameterPreviewView> getDependentParameterPreviewViewStream() {
        return dependentParamsContainer.getChildren().stream()
                .map(DependentParameterPreviewView.class::cast);
    }

    private List<TerminalParameter> getTerminalParametersOnForm() {
        return getQuestionForm().parameters().stream()
                .filter(TerminalParameter.class::isInstance)
                .map(TerminalParameter.class::cast).toList();
    }

    /**
     * Обрабатывает событие нажатия на кнопку "Продолжить"
     */
    @FXML
    private void onContinueClick() {
        navigateTo(EXPORT_TASK_VIEW_FORM_VIEW, testTaskGenerator);
    }

    /**
     * Обрабатывает событие нажатия на кнопку "Вернуться"
     */
    @FXML
    private void onBackClick() {
        navigateTo(ADD_TASK_FORM_VIEW, testTaskGenerator.getForm());
    }

    private AddFastNumericTestForm getQuestionForm() {
        return testTaskGenerator.getForm();
    }
}