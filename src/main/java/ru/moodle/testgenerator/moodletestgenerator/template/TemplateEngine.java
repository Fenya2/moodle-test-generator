package ru.moodle.testgenerator.moodletestgenerator.template;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Сервис создания строк по шаблонам. Шаблон - строка, где отдельные ее именованные части вида [[{name}]] задают
 * "степень свободы" шаблона. В именованные части подставляются значения параметров
 */
public interface TemplateEngine {
    /**
     * Осуществляет подстановку числовых параметров в переданный шаблон
     *
     * @param template   шаблон
     * @param parameters параметры
     * @return строка, сформированная по шаблону с подставленными параметрами
     */
    String render(String template, Map<String, BigDecimal> parameters);
}
