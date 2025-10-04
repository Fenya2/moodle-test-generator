module ru.moodle.testgenerator.moodletestgenerator {
    requires javafx.controls;
    requires javafx.fxml;
    requires ru.moodle.testgenerator.moodletestgenerator;

    opens ru.moodle.testgenerator.moodletestgenerator to javafx.fxml;
    exports ru.moodle.testgenerator.moodletestgenerator;
    exports ru.moodle.testgenerator.moodletestgenerator.uicomponents.parameters;
    opens ru.moodle.testgenerator.moodletestgenerator.uicomponents.parameters to javafx.fxml;
    exports ru.moodle.testgenerator.moodletestgenerator.uicomponents.addQuestionForm;
    opens ru.moodle.testgenerator.moodletestgenerator.uicomponents.addQuestionForm to javafx.fxml;
}