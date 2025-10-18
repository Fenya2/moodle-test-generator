package ru.moodle.testgenerator.moodletestgenerator.core.parameters;

import java.math.BigDecimal;

/**
 * @author dsyromyatnikov
 * @since 03.10.2025
 */
public final class TerminalParameter extends Parameter {
    /**
     * Минимальное значение параметра
     */
    private BigDecimal minValue;
    /**
     * Максимальное значение параметра
     */
    private BigDecimal maxValue;
    /**
     * Шаг, задающий сетку области значений параметра
     */
    private BigDecimal step;

    public TerminalParameter(String name) {
        super(name);
    }

    public BigDecimal getMinValue() {
        return minValue;
    }

    public void setMinValue(BigDecimal minValue) {
        this.minValue = minValue;
    }

    public BigDecimal getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(BigDecimal maxValue) {
        this.maxValue = maxValue;
    }

    public BigDecimal getStep() {
        return step;
    }

    public void setStep(BigDecimal step) {
        this.step = step;
    }
}
