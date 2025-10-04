package ru.moodle.testgenerator.moodletestgenerator.uicomponents.parameters;

import java.util.Set;
import java.util.stream.Collectors;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Параметр, значение которого зависит от других параметров
 *
 * @author dsyromyatnikov
 * @since 03.10.2025
 */
public final class DependentParameterView extends AbstractParameterView
{
    private final VBox dependenciesContainer;
    private final TextArea scriptArea;

    public DependentParameterView()
    {
        // Секция зависимостей
        VBox dependenciesSection = new VBox(5);
        dependenciesContainer = new VBox(5);
        Label dependenciesLabel = new Label("Зависимости:");
        Button addDependencyButton = new Button("+");
        addDependencyButton.setOnAction(_ -> addDependencyRow());
        dependenciesSection.getChildren().addAll(dependenciesLabel, dependenciesContainer, addDependencyButton);

        // Секция скрипта
        VBox scriptSection = new VBox(5);
        Label scriptLabel = new Label("Скрипт для вычисления параметра:");
        scriptArea = new TextArea();
        scriptArea.setPromptText("a + 2 * b");
        scriptArea.setPrefRowCount(1);
        scriptSection.getChildren().addAll(scriptLabel, scriptArea);

        this.getChildren().addAll(dependenciesSection, scriptSection);
    }

    /**
     * Добавляет новое поле для ввода имени зависимого параметра
     */
    private void addDependencyRow()
    {
        HBox dependencyRow = new HBox(5);
        TextField dependencyField = new TextField();
        dependencyField.setPromptText("a");
        Button removeButton = new Button("–");
        removeButton.setOnAction(_ -> dependenciesContainer.getChildren().remove(dependencyRow));
        dependencyRow.getChildren().addAll(dependencyField, removeButton);
        dependenciesContainer.getChildren().add(dependencyRow);
    }

    /**
     * Возвращает массив имен параметров, от которых зависит данный параметр
     *
     * @return массив имен параметров
     */
    public Set<String> getDependencies()
    {
        return dependenciesContainer.getChildren().stream()
                .map(HBox.class::cast)
                .map(hBox -> hBox.getChildren().getFirst())
                .map(TextField.class::cast)
                .map(TextField::getText)
                .collect(Collectors.toSet());
    }

    /**
     * Добавляет зависимость с указанным именем
     *
     * @param parameterName имя параметра
     */
    public void addDependencyRow(String parameterName)
    {
        HBox dependencyRow = new HBox(5);
        TextField dependencyField = new TextField();
        dependencyField.setText(parameterName);
        Button removeButton = new Button("–");
        removeButton.setOnAction(_ -> dependenciesContainer.getChildren().remove(dependencyRow));
        dependencyRow.getChildren().addAll(dependencyField, removeButton);
        dependenciesContainer.getChildren().add(dependencyRow);
    }

    /**
     * Возвращает выражение для вычисления параметра
     *
     * @return строка с выражением
     */
    public String getEvaluationScript()
    {
        return scriptArea.getText();
    }
}