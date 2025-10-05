package ru.moodle.testgenerator.moodletestgenerator.uicomponents.forms;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ru.moodle.testgenerator.moodletestgenerator.ParameterContainerView;
import ru.moodle.testgenerator.moodletestgenerator.core.QuestionGenerator;
import ru.moodle.testgenerator.moodletestgenerator.core.form.AddQuestionForm;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.DependentParameter;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.Parameter;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.TerminalParameter;
import ru.moodle.testgenerator.moodletestgenerator.uicomponents.parameters.DependentParameterView;
import ru.moodle.testgenerator.moodletestgenerator.uicomponents.parameters.ParameterRemovedEvent;
import ru.moodle.testgenerator.moodletestgenerator.uicomponents.parameters.TerminalParameterView;

public class AddQuestionController
{
    @FXML
    public Label errorLabel;

    @FXML
    private TextArea questionField;

    @FXML
    private VBox parametersContainer;

    @FXML
    private TextField answerField;

    /**
     * Обрабатывает событие нажатия на кнопку добавления параметра
     */
    @FXML
    private void onAddParameterClick()
    {
        List<Node> parameterViews = getParameterViews();
        ru.moodle.testgenerator.moodletestgenerator.ParameterContainerView parameter =
                new ru.moodle.testgenerator.moodletestgenerator.ParameterContainerView();
        parameter.addEventHandler(ParameterRemovedEvent.REMOVE_PARAMETER, this::onRemoveParameterClick);
        if (!parameterViews.isEmpty())
        {
            parametersContainer.setVisible(true);
        }
        parameterViews.add(parameter);
        parametersContainer.setVisible(true);
        parametersContainer.setManaged(true);
    }

    /**
     * Обрабатывает событие удаления параметра
     */
    private void onRemoveParameterClick(ParameterRemovedEvent event)
    {
        List<Node> parameterViews = getParameterViews();
        int parameterIndex = parameterViews.indexOf(event.getParameter());
        parameterViews.remove(parameterIndex);
        if (parameterViews.isEmpty())
        {
            parametersContainer.setVisible(false);
        }
    }

    /**
     * Обрабатывает событие нажатия на кнопку сохранения вопроса
     */
    @FXML
    private void onSubmitButton()
    {
        String question = questionField.getText();
        List<Parameter> parameters = getParameterViews().stream().map(ParameterContainerView.class::cast)
                .map(ParameterContainerView::getFilledParameter)
                .filter(Objects::nonNull)
                .map(filledParameter -> switch (filledParameter)
                {
                    case TerminalParameterView terminalParamView ->
                    {
                        TerminalParameter terminalParam = new TerminalParameter(terminalParamView.getName());
                        try
                        {
                            terminalParam.setMaxValue(new BigDecimal(terminalParamView.getMaxValue()));
                            terminalParam.setMinValue(new BigDecimal(terminalParamView.getMinValue()));
                            terminalParam.setMaxValue(new BigDecimal(terminalParamView.getStep()));
                        }
                        catch (NumberFormatException _)
                        {
                            errorLabel.setText("Неверно заданы допустимые значения параметра %s".formatted(
                                    terminalParam.getName()));
                            errorLabel.setVisible(true);
                        }
                        yield terminalParam;
                    }
                    case DependentParameterView dependentParamView ->
                    {
                        DependentParameter dependentParameter = new DependentParameter(dependentParamView.getName());
                        dependentParameter.setDependentParameters(dependentParamView.getDependencies());
                        dependentParameter.setEvaluationScript(dependentParamView.getEvaluationScript());
                        yield dependentParameter;
                    }
                }).toList();
        String answer = answerField.getText();
        AddQuestionForm form = new AddQuestionForm(question, parameters, answer);
        try
        {
            new QuestionGenerator(form);
        }
        catch (Exception e)
        {
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
        }
    }

    private List<Node> getParameterViews()
    {
        return parametersContainer.getChildren();
    }
}
