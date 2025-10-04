package ru.moodle.testgenerator.moodletestgenerator;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application
{
    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("add-question-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Moodle test generator");
        stage.setScene(scene);
        stage.show();
    }
}
