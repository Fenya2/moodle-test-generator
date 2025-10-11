package ru.moodle.testgenerator.moodletestgenerator.ui;

/**
 * Сервис для переключения между {@link javafx.scene.Scene сценами}
 *
 * @author dsyromyatnikov
 * @since 12.10.2025
 */
public interface NavigationService
{
    /**
     * Переключает сцену
     *
     * @param fxmlPath путь к представлению для отображения, которое будет корневым элеметом сцены
     */
    void navigateTo(String fxmlPath);

    /**
     * Переключает сцену
     *
     * @param fxmlPath путь к представлению для отображения, которое будет корневым элеметом сцены
     * @param context контекст, необходимый для работы следующей сцены
     */
    void navigateTo(String fxmlPath, Object context);
}
