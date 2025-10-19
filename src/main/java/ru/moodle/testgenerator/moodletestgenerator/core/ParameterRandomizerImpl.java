package ru.moodle.testgenerator.moodletestgenerator.core;

import com.google.inject.Singleton;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.Parameter;
import ru.moodle.testgenerator.moodletestgenerator.core.parameters.TerminalParameter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author dsyromyatnikov
 * @since 17.10.2025
 */
@Singleton
public class ParameterRandomizerImpl implements ParameterRandomizer {
    private final Random random;

    public ParameterRandomizerImpl() {
        this.random = new Random();
    }

    @Override
    public Map<String, BigDecimal> randomizeTerminalParameters(List<TerminalParameter> parameters) {
        return parameters.stream()
                .collect(Collectors.toMap(Parameter::getName, this::getRandomParameterValue));
    }

    @Override
    public BigDecimal randomizeTerminalParameter(TerminalParameter terminalParameter) {
        return getRandomParameterValue(terminalParameter);
    }

    /**
     * @param parameter терминальный параметр
     * @return случайное возможное значение параметра (вероятность распределена равномерно по области определения)
     */
    private BigDecimal getRandomParameterValue(TerminalParameter parameter) {
        BigDecimal minValue = parameter.getMinValue();
        BigDecimal maxValue = parameter.getMaxValue();
        BigDecimal step = parameter.getStep();

        // maxN = floor((maxValue - minValue) / step)
        BigDecimal nDecimal = (maxValue.subtract(minValue)).divide(step, RoundingMode.FLOOR);
        int maxN = nDecimal.intValueExact() + 1;
        int n = random.nextInt(maxN);
        return minValue.add(step.multiply(BigDecimal.valueOf(n)));
    }
}
