package ru.moodle.testgenerator.moodletestgenerator.uicomponents.addQuestionForm;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ru.moodle.testgenerator.moodletestgenerator.core.form.AddQuestionForm;
import ru.moodle.testgenerator.moodletestgenerator.uicomponents.parameters.ParameterRemovedEvent;

public class AddQuestionController
{
    @FXML
    private TextArea questionField;

    @FXML
    private VBox parametersContainer;

    @FXML
    private TextField answerField;

    @FXML
    private Button doneButton;

    /**
     * Форма добавления вопроса
     */
    private final AddQuestionForm addQuestionForm;

    public AddQuestionController()
    {
        this.addQuestionForm = new AddQuestionForm();
    }

    /**
     * Обрабатывает событие нажатия на кнопку добавления параметра
     */
    @FXML
    private void onAddParameterClick()
    {
        ru.moodle.testgenerator.moodletestgenerator.ParameterContainerView parameter =
                new ru.moodle.testgenerator.moodletestgenerator.ParameterContainerView();
        parameter.addEventHandler(ParameterRemovedEvent.REMOVE_PARAMETER, this::onRemoveParameterClick);
        parameter.addEventHandler(ParameterUpdated);
        if (!parametersContainer.getChildren().isEmpty())
        {
            parametersContainer.setVisible(true);
        }
        getParameterViews().add(parameter);
        parametersContainer.setVisible(true);
        parametersContainer.setManaged(true);
        addQuestionForm.addParameterPlace();
    }

    /**
     * Обрабатывает событие удаления параметра
     */
    private void onRemoveParameterClick(ParameterRemovedEvent event)
    {
        List<Node> parameterViews = getParameterViews();
        int parameterIndex = parameterViews.indexOf(event.getParameter());
        addQuestionForm.deleteParameterAt(parameterIndex);
        parameterViews.remove(parameterIndex);
        if (parameterViews.isEmpty())
        {
            parametersContainer.setVisible(false);
        }
    }

    /**
     * Обрабатывает событие обновления представления параметра на форме
     */
    private void onUpdateParameter()
    {

    }

    private List<Node> getParameterViews()
    {
        return parametersContainer.getChildren();
    }

    /**
     * Обрабатывает событие нажатия на кнопку сохранения вопроса
     */
    @FXML
    private void onSubmitButton()
    {
        System.out.println("onSubmitButton");
    }
}
