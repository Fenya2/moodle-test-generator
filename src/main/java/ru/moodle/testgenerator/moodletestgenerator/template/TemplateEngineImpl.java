package ru.moodle.testgenerator.moodletestgenerator.template;

import com.google.inject.Singleton;
import org.apache.commons.text.StringSubstitutor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Реализация {@link TemplateEngine} на основе {@link StringSubstitutor}
 *
 * @author dsyromyatnikov
 * @since 18.10.2025
 */
@Singleton
public class TemplateEngineImpl implements TemplateEngine {

    @Override
    public String render(String template, Map<String, BigDecimal> parameters) {
        return new StringSubstitutor(prepareParameters(parameters), "[[", "]]", '!').replace(template);
    }

    private static Map<String, String> prepareParameters(Map<String, BigDecimal> parameters) {
        return parameters.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().toPlainString()));
    }
}
