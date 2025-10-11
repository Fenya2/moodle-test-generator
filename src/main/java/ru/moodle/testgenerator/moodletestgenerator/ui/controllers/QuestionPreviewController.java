package ru.moodle.testgenerator.moodletestgenerator.ui.controllers;

import static ru.moodle.testgenerator.moodletestgenerator.ui.controllers.AddQuestionController.ADD_QUESTION_FORM_VIEW;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import com.google.inject.Inject;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import ru.moodle.testgenerator.moodletestgenerator.core.ParameterRandomizer;
import ru.moodle.testgenerator.moodletestgenerator.core.TestTaskGenerator;
import ru.moodle.testgenerator.moodletestgenerator.core.form.AddQuestionForm;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.DependentParameter;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.Parameter;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.TerminalParameter;
import ru.moodle.testgenerator.moodletestgenerator.ui.NavigationService;
import ru.moodle.testgenerator.moodletestgenerator.ui.parameters.previewform.DependentParameterPreviewView;
import ru.moodle.testgenerator.moodletestgenerator.ui.parameters.previewform.TerminalParameterPreviewView;

/**
 * Контроллер формы предпросмотра составленного вопроса. Подразумевается, что пользователь уже описал вопрос и по
 * вопросу сгенерировался генератор {@link TestTaskGenerator}. То есть контроллер начинает работу с контекстом
 *
 * @author dsyromyatnikov
 * @since 11.10.2025
 */
public class QuestionPreviewController implements ControllerWithContext, Initializable
{
    /**
     * Представление, которое обрабатывает контроллер
     */
    public static final String QUESTION_PREVIEW_VIEW = "/question-preview-view.fxml";

    private final NavigationService navigationService;
    private final ParameterRandomizer parameterRandomizer;

    private TestTaskGenerator testTaskGenerator;

    @FXML
    public TextArea questionTextArea;
    @FXML
    public VBox terminalParamsContainer;
    @FXML
    public VBox dependentParamsContainer;
    @FXML
    public TextArea answerTextArea;

    @Inject
    public QuestionPreviewController(NavigationService navigationService, ParameterRandomizer parameterRandomizer)
    {
        this.navigationService = navigationService;
        this.parameterRandomizer = parameterRandomizer;
    }

    @Override
    public void setContext(Object context)
    {
        this.testTaskGenerator = (TestTaskGenerator)context;
    }

    /**
     * Обрабатывает событие успешной загрузки сцены. Заполняет форму указанными параметрами с представлениями для
     * предпросмотра
     */
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        AddQuestionForm form = getQuestionForm();
        questionTextArea.setText(form.getQuestion());

        List<Node> terminalParameters = terminalParamsContainer.getChildren();
        List<Node> dependentParameters = dependentParamsContainer.getChildren();

        for (Parameter parameter : form.getParameters())
        {
            switch (parameter)
            {
                case TerminalParameter terminalParameter ->
                        terminalParameters.add(new TerminalParameterPreviewView(terminalParameter));
                case DependentParameter dependentParameter ->
                        dependentParameters.add(new DependentParameterPreviewView(dependentParameter));
            }
        }

        answerTextArea.setText(form.getAnswer());
    }

    /**
     * Обрабатывает событие нажатия на кнопку случайного заполнения терминальных параметров
     */
    @FXML
    public void onFillRandomClick()
    {
        List<TerminalParameter> terminalParameters = getTerminalParametersOnForm();
        Map<String, BigDecimal> randomParametersValues = parameterRandomizer.randomizeTerminalParameters(
                terminalParameters);

        getTerminalParameterPreviewViewStream().forEach(view ->
        {
            String parameterName = view.getParameterName();
            view.setValue(randomParametersValues.get(parameterName).toPlainString());
        });

    }

    /**
     * Обрабатывает событие нажатия на кнопку "Рассчитать вопрос".
     * <li>Собирает с формы значения терминальных параметров. Если какой-то из параметров не указан, генерирует
     * случайное значение</li>
     * <li>Вычисляет на основе терминальных параметров зависимые параметры, вопрос и ответ</li>
     * <li>Выводит вычисленные параметры, вопрос и ответ</li>
     */
    @FXML
    public void onCalculateClick()
    {
        Map<String, BigDecimal> terminalParamsValues = collectTerminalParamsValuesOnForm();
        Map<String, BigDecimal> paramsValues = testTaskGenerator.generateTestTask(terminalParamsValues);
        getTerminalParameterPreviewViewStream().forEach(view ->
                view.setValue(paramsValues.get(view.getParameterName()).toPlainString()));
        getDependentParameterPreviewViewStream().forEach(
                view -> view.setParameterValue(paramsValues.get(view.getParameterName()).toPlainString()));
    }

    /**
     * Сначала вычисляет случайные значения всех терминальных параметров, затем часть заменяет значениями, выведенными
     * в представлениях терминальных параметров
     */
    private Map<String, BigDecimal> collectTerminalParamsValuesOnForm()
    {
        Map<String, BigDecimal> randomizedValues = parameterRandomizer.randomizeTerminalParameters(
                getTerminalParametersOnForm());
        getTerminalParameterPreviewViewStream().forEach(view ->
                {
                    String parameterValue = view.getParameterValue();
                    try
                    {
                        if (!parameterValue.isEmpty())
                        {
                            randomizedValues.put(view.getParameterName(), new BigDecimal(parameterValue));
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
        );
        return randomizedValues;
    }

    /**
     * @return поток открытых представлений для предпросмотра терминальных параметров
     */
    private Stream<TerminalParameterPreviewView> getTerminalParameterPreviewViewStream()
    {
        return terminalParamsContainer.getChildren().stream()
                .map(TerminalParameterPreviewView.class::cast);
    }

    private Stream<DependentParameterPreviewView> getDependentParameterPreviewViewStream()
    {
        return dependentParamsContainer.getChildren().stream()
                .map(DependentParameterPreviewView.class::cast);
    }

    private List<TerminalParameter> getTerminalParametersOnForm()
    {
        return getQuestionForm().getParameters().stream()
                .filter(TerminalParameter.class::isInstance)
                .map(TerminalParameter.class::cast).toList();
    }

    /**
     * Обрабатывает событие нажатия на кнопку "Продолжить"
     */
    @FXML
    public void onContinueClick()
    {
        System.out.println("Кликнули на продолжить");
    }

    /**
     * Обрабатывает событие нажатия на кнопку "Вернуться"
     */
    public void onBackClick()
    {
        navigationService.navigateTo(ADD_QUESTION_FORM_VIEW, testTaskGenerator.getForm());
    }

    private AddQuestionForm getQuestionForm()
    {
        return testTaskGenerator.getForm();
    }
}