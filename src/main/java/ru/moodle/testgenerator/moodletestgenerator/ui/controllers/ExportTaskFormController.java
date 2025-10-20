package ru.moodle.testgenerator.moodletestgenerator.ui.controllers;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import ru.moodle.testgenerator.moodletestgenerator.core.ParameterRandomizer;
import ru.moodle.testgenerator.moodletestgenerator.core.TestTask;
import ru.moodle.testgenerator.moodletestgenerator.core.TestTaskGenerationResult;
import ru.moodle.testgenerator.moodletestgenerator.core.TestTaskGenerator;
import ru.moodle.testgenerator.moodletestgenerator.core.export.ExportingService;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.TerminalParameter;
import ru.moodle.testgenerator.moodletestgenerator.ui.NavigationService;
import ru.moodle.testgenerator.moodletestgenerator.ui.PositiveIntegerField;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static ru.moodle.testgenerator.moodletestgenerator.ui.controllers.TestTaskPreviewController.ADD_TASK_PREVIEW_FORM_VIEW;

/**
 * Контроллер, управляющий формой экспорта задания
 *
 * @author dsyromyatnikov
 * @since 19.10.2025
 */
public class ExportTaskFormController extends AbstractControllerWithContext<TestTaskGenerator> {

    public static final String EXPORT_TASK_VIEW_FORM_VIEW = "/exportTaskFormView.fxml";

    private final ExportingService exportingService;
    private final ParameterRandomizer parameterRandomizer;

    private TestTaskGenerator testTaskGenerator;

    @FXML
    private Label errorLabel;

    @FXML
    private PositiveIntegerField testCountField;

    @FXML
    private TextField directoryPathField;

    @FXML
    private TextField fileNameField;

    @Inject
    public ExportTaskFormController(NavigationService navigationService, ExportingService exportingService, ParameterRandomizer parameterRandomizer) {
        super(navigationService);
        this.exportingService = exportingService;
        this.parameterRandomizer = parameterRandomizer;
    }

    @Override
    public void setContext(TestTaskGenerator context) {
        this.testTaskGenerator = context;
    }

    /**
     * Обрабатывает событие нажатия на кнопку "Вернуться"
     */
    @FXML
    private void onBackClick() {
        navigateTo(ADD_TASK_PREVIEW_FORM_VIEW, testTaskGenerator);
    }

    /**
     * Обрабатывает событие нажатия на кнопку выбора директории для экспортирования заданий
     */
    @FXML
    private void onClickChooseDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Выберите папку для экспорта");
        directoryChooser.setInitialDirectory(calculateInitialDirectory());
        File selectedDirectory = directoryChooser.showDialog(directoryPathField.getScene().getWindow());
        if (selectedDirectory != null) {
            directoryPathField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    private File calculateInitialDirectory() {

        String currentPath = directoryPathField.getText();
        if (!currentPath.isBlank()) {
            File initialDir = new File(currentPath);
            if (initialDir.exists() && initialDir.isDirectory()) {
                return initialDir;
            }
        }
        return new File(System.getProperty("user.home"));
    }

    /**
     * Обрабатывает событие нажатия на кнопку экспортирования заданий
     */
    @FXML
    public void onExportClick() {

        try {
            int testCount = Integer.parseInt(testCountField.getText());
            Path filePath = Path.of(directoryPathField.getText(), fileNameField.getText());

            List<TerminalParameter> terminalParameters = testTaskGenerator.getTerminalParameters();
            List<TestTask> tasks = parameterRandomizer.randomizeTerminalParameters(terminalParameters, testCount).stream()
                    .map(testTaskGenerator::generateTestTask)
                    .map(TestTaskGenerationResult::getTestTask)
                    .toList();

            exportingService.exportToGift(tasks, filePath);
            showLoadedDialog();
        } catch (Exception e) {
            printError(e.getMessage());
        }
    }

    private void showLoadedDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Готово");
        alert.setHeaderText("Экспорт заданий завершен");
        alert.initOwner(directoryPathField.getScene().getWindow());
        alert.showAndWait();
    }
}
