package ru.moodle.testgenerator.moodletestgenerator.core.export;

import ru.moodle.testgenerator.moodletestgenerator.core.NumericTestTask;

import java.nio.file.Path;
import java.util.List;

/**
 * Сервис экспорта заданий в формат moodle
 */
public interface ExportingService {

    /**
     * Экспортирует сформированные задания в файл формата Gift и сохраняет его по переданному пути
     *
     * @param tasks    список вопросов
     * @param path     путь к файлу
     */
    void exportToGift(List<NumericTestTask> tasks, Path path);
}
