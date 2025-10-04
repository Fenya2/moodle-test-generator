package ru.moodle.testgenerator.moodletestgenerator.core.parameters;

import java.math.BigDecimal;

/**
 * @author dsyromyatnikov
 * @since 03.10.2025
 */
public final class TerminalParameter extends Parameter
{
    public TerminalParameter(String name)
    {
        super(name);
    }

    /**
     * Имя параметра
     */
    private String name;

    /**
     * Минимальное значение параметра
     */
    private BigDecimal minValue;

    /**
     * Максимальное значение параметра
     */
    private BigDecimal maxValue;

    /**
     * Шаг, с которым генерируются значения
     */
    private BigDecimal step;

    public BigDecimal getMinValue()
    {
        return minValue;
    }

    public void setMinValue(BigDecimal minValue)
    {
        this.minValue = minValue;
    }

    public BigDecimal getMaxValue()
    {
        return maxValue;
    }

    public void setMaxValue(BigDecimal maxValue)
    {
        this.maxValue = maxValue;
    }

    public BigDecimal getStep()
    {
        return step;
    }

    public void setStep(BigDecimal step)
    {
        this.step = step;
    }

    @Override
    public String getName()
    {
        return name;
    }
}
