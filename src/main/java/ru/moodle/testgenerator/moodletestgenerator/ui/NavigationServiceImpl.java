package ru.moodle.testgenerator.moodletestgenerator.ui;

import java.io.IOException;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import jakarta.annotation.Nullable;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Сервис переключения сцен
 *
 * @author dsyromyatnikov
 * @since 11.10.2025
 */
@Singleton
public class NavigationServiceImpl implements NavigationService
{
    private final Stage primaryStage;
    private final GuiceFXMLLoader fxmlLoader;

    @Inject
    public NavigationServiceImpl(Stage primaryStage, GuiceFXMLLoader loader)
    {
        this.primaryStage = primaryStage;
        this.fxmlLoader = loader;
    }

    @Override
    public void navigateTo(String fxmlPath)
    {
        navigateTo(fxmlPath, null);
    }

    /**
     * Переключает сцену на представление с расположением {@code fxmlPath}
     * @param fxmlPath расположение представления для отображения сцены (полный путь)
     * @param context контекст, необходимый следующему контроллеру для работы
     */
    public void navigateTo(String fxmlPath, @Nullable Object context)
    {
        try
        {
            primaryStage.setScene(new Scene(fxmlLoader.load(getClass().getResource(fxmlPath), context)));
        }
        catch (IOException e)
        {
            throw new NavigateSceneException("Unable to navigate to scene with fxml path %s".formatted(fxmlPath), e);
        }
    }
}