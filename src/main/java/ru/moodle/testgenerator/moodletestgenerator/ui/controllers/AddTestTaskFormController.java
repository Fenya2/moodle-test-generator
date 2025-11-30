package ru.moodle.testgenerator.moodletestgenerator.ui.controllers;

import com.google.inject.Inject;
import jakarta.annotation.Nullable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ru.moodle.testgenerator.moodletestgenerator.core.TestTaskGeneratorFactory;
import ru.moodle.testgenerator.moodletestgenerator.core.form.AddFastNumericTestForm;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.DependentParameter;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.Parameter;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.TerminalParameter;
import ru.moodle.testgenerator.moodletestgenerator.ui.NavigationService;
import ru.moodle.testgenerator.moodletestgenerator.ui.addform.DependentParameterView;
import ru.moodle.testgenerator.moodletestgenerator.ui.addform.ParameterContainerView;
import ru.moodle.testgenerator.moodletestgenerator.ui.addform.ParameterRemovedEvent;
import ru.moodle.testgenerator.moodletestgenerator.ui.addform.TerminalParameterView;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static ru.moodle.testgenerator.moodletestgenerator.core.parameters.ParameterType.DEPENDENT;
import static ru.moodle.testgenerator.moodletestgenerator.core.parameters.ParameterType.TERMINAL;
import static ru.moodle.testgenerator.moodletestgenerator.ui.controllers.TestTaskPreviewController.ADD_TASK_PREVIEW_FORM_VIEW;

/**
 * Контроллер формы добавления задания с численным вариантом ответа. Может быть запущен в контексте заполненной ранее формы
 *
 * @author dsyromyatnikov
 * @since 12.10.2025
 */
public class AddTestTaskFormController extends AbstractControllerWithContext<AddFastNumericTestForm> implements Initializable {
    /**
     * Представление, которое обрабатывает контроллер
     */
    public static final String ADD_TASK_FORM_VIEW = "/fxml/addTaskFormView.fxml";

    private final TestTaskGeneratorFactory testTaskGeneratorFactory;

    /**
     * Форма, которую ранее заполнял пользователь
     */
    @Nullable
    private AddFastNumericTestForm addForm;

    /**
     * Поле ввода вопроса
     */
    @FXML
    private TextArea questionField;

    /**
     * Контейнер с параметрами
     */
    @FXML
    private VBox parametersContainer;

    /**
     * Поле ответа
     */
    @FXML
    private TextField answerField;

    /**
     * Поле погрешности ответа
     */
    @FXML
    private TextField answerErrorField;

    @Inject
    public AddTestTaskFormController(NavigationService navigationService, TestTaskGeneratorFactory testTaskGeneratorFactory) {
        super(navigationService);
        this.testTaskGeneratorFactory = testTaskGeneratorFactory;
    }

    @Override
    public void setContext(AddFastNumericTestForm context) {
        this.addForm = context;
    }

    /**
     * Если контроллер был создан с контекстом заполненной ранее формы, выводит значения из формы в представление
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (addForm == null) {
            return;
        }

        questionField.setText(addForm.question());
        for (Parameter parameter : addForm.parameters()) {
            ParameterContainerView parameterContainer = new ParameterContainerView();
            parameterContainer.addEventHandler(ParameterRemovedEvent.REMOVE_PARAMETER, this::onRemoveParameterClick);

            switch (parameter) {
                case TerminalParameter terminalParameter -> {
                    parameterContainer.setParameterType(TERMINAL);

                    TerminalParameterView terminalParameterView = new TerminalParameterView();
                    terminalParameterView.setName(terminalParameter.getName());
                    terminalParameterView.setMaxValue(terminalParameter.getMaxValue().toPlainString());
                    terminalParameterView.setMinValue(terminalParameter.getMinValue().toPlainString());
                    terminalParameterView.setStep(terminalParameter.getStep().toPlainString());

                    parameterContainer.setFilledParameter(terminalParameterView);
                }
                case DependentParameter dependentParameter -> {
                    parameterContainer.setParameterType(DEPENDENT);

                    DependentParameterView dependentParameterView = new DependentParameterView();
                    dependentParameterView.setName(dependentParameter.getName());
                    dependentParameter.getDependentParameterNames().forEach(dependentParameterView::addDependencyRow);
                    dependentParameterView.setEvaluationScript(dependentParameter.getCalculationScript());

                    parameterContainer.setFilledParameter(dependentParameterView);
                }
            }
            parametersContainer.getChildren().add(parameterContainer);
        }
        if (!parametersContainer.getChildren().isEmpty()) {
            parametersContainer.setVisible(true);
        }

        answerField.setText(addForm.answer());
        answerErrorField.setText(addForm.answerError());
    }

    /**
     * Обрабатывает событие нажатия на кнопку добавления параметра
     */
    @FXML
    private void onAddParameterClick() {
        List<Node> parameterViews = getParameterViews();
        ParameterContainerView parameter = new ParameterContainerView();
        parameter.addEventHandler(ParameterRemovedEvent.REMOVE_PARAMETER, this::onRemoveParameterClick);
        if (!parameterViews.isEmpty()) {
            parametersContainer.setVisible(true);
        }
        parameterViews.add(parameter);
        parametersContainer.setVisible(true);
        parametersContainer.setManaged(true);
    }

    /**
     * Обрабатывает событие удаления параметра
     */
    private void onRemoveParameterClick(ParameterRemovedEvent event) {
        List<Node> parameterViews = getParameterViews();
        parameterViews.remove(event.getParameter());
        if (parameterViews.isEmpty()) {
            parametersContainer.setVisible(false);
        }
    }

    /**
     * Обрабатывает событие нажатия на кнопку сохранения вопроса
     */
    @FXML
    private void onSubmitButton() {
        try {
            navigateTo(ADD_TASK_PREVIEW_FORM_VIEW, testTaskGeneratorFactory.create(collectDataOnForm()));
        } catch (Exception e) {
            printError(e.getMessage());
        }
    }

    /**
     * Собирает данные с формы и формирует объект формы
     */
    private AddFastNumericTestForm collectDataOnForm() {
        String question = questionField.getText();
        List<Parameter> parameters = getParameterViews().stream().map(ParameterContainerView.class::cast)
                .map(ParameterContainerView::getFilledParameter)
                .filter(Objects::nonNull)
                .map(filledParameter -> switch (filledParameter) {
                    case TerminalParameterView terminalParamView -> createTerminalParameter(terminalParamView);
                    case DependentParameterView dependentParamView -> createDependentParameter(dependentParamView);
                }).toList();
        String answer = answerField.getText();
        String answerError = answerErrorField.getText();
        return new AddFastNumericTestForm(question, parameters, answer, answerError);
    }

    private static TerminalParameter createTerminalParameter(TerminalParameterView terminalParamView) {
        TerminalParameter terminalParam = new TerminalParameter(terminalParamView.getName());
        try {
            terminalParam.setMaxValue(new BigDecimal(terminalParamView.getMaxValue()));
            terminalParam.setMinValue(new BigDecimal(terminalParamView.getMinValue()));
            terminalParam.setStep(new BigDecimal(terminalParamView.getStep()));
        } catch (NumberFormatException _) {
            throw new InvalidNumberFormatException(
                    "Неверно заданы допустимые значения параметра %s".formatted(
                            terminalParam.getName()));
        }
        return terminalParam;
    }

    private static DependentParameter createDependentParameter(DependentParameterView dependentParamView) {
        DependentParameter dependentParameter = new DependentParameter(dependentParamView.getName());
        dependentParameter.setDependentParameters(dependentParamView.getDependencies());
        dependentParameter.setEvaluationScript(dependentParamView.getEvaluationScript());
        return dependentParameter;
    }

    /**
     * @return добавленные описания параметров на форме
     */
    private List<Node> getParameterViews() {
        return parametersContainer.getChildren();
    }
}
