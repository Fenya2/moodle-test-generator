package ru.moodle.testgenerator.moodletestgenerator.ui;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import jakarta.annotation.Nullable;
import javafx.fxml.FXMLLoader;
import ru.moodle.testgenerator.moodletestgenerator.ui.controllers.ControllerWithContext;

import java.io.IOException;
import java.net.URL;

/**
 * Загрузчик fxml контроллера. Обычный загрузчик, предоставляющийся JavaFx не дает возможности внедрять зависимости.
 * Но у стандартного загрузчика есть возможность определить фабрику контроллеров. В качестве фабрики этот загрузчик
 * устанавливает guice {@link Injector}, который внедряет требуемые для работы контроллера зависимости
 *
 * @author dsyromyatnikov
 * @since 11.10.2025
 */
@Singleton
public class GuiceFXMLLoader {
    private final Injector injector;

    @Inject
    public GuiceFXMLLoader(Injector injector) {
        this.injector = injector;
    }

    /**
     * @param fxmlUrl путь к представлению
     * @param context контекст, необходимый контроллеру для обслуживания загружаемого представления
     * @param <T>     тип представления
     * @return представление, к которому привязан подготовленный контроллер, его обслуживающий
     */
    public <T> T load(URL fxmlUrl, @Nullable Object context) throws IOException {
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        loader.setControllerFactory(clazz ->
        {
            Object controller = injector.getInstance(clazz);
            if (controller instanceof ControllerWithContext controllerWithContext) {
                controllerWithContext.setContext(context);
            }
            return controller;
        });
        return loader.load();
    }
}