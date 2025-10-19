package ru.moodle.testgenerator.moodletestgenerator.core.export;

import ru.moodle.testgenerator.moodletestgenerator.core.TestTask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

/**
 * Экспортирует задания в GIFT формат
 *
 * @author dsyromyatnikov
 * @since 19.10.2025
 */
public class GiftExporter implements ExportingService {
    private static final Set<String> GIFT_SPECIAL_CHARACTERS = Set.of("~", "=", "#", "{", "}");
    private static final String GIFT_FILE_EXTENSION = "gift";
    private static final String GIFT_TEST_WITH_SINGLE_ANSWER_PATTERN = "%s{=%s}";

    @Override
    public void exportToGift(List<TestTask> tasks, Path path) {
        try (var writer = Files.newBufferedWriter(resolvePath(path), StandardCharsets.UTF_8)) {
            for (TestTask task : tasks) {
                String taskStr = GIFT_TEST_WITH_SINGLE_ANSWER_PATTERN.formatted(
                        escapeSpecialCharacters(task.getQuestion()), task.getAnswer());
                writer.write(taskStr);
                writer.newLine();
                writer.newLine();
            }
        } catch (IOException e) {
            throw new ExportingException("Не удалось открыть файл для записи", e);
        }
    }

    private static Path resolvePath(Path path) {
        String fileName = path.getFileName().toString();
        if (!fileName.contains(".")) {
            return path.resolveSibling(fileName + "." + GIFT_FILE_EXTENSION);
        }
        return path;
    }

    /**
     * Экранирует специальные символы согласно формату GIFT (в секции с вопросами)
     */
    private static String escapeSpecialCharacters(String questionString) {
        return GIFT_SPECIAL_CHARACTERS.stream()
                .reduce(questionString, (str, ch) -> str.replace(ch, "\\" + ch));
    }
}
