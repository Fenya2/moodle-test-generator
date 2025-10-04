package ru.moodle.testgenerator.moodletestgenerator.core.parameters;

/**
 * Параметр
 *
 * @author dsyromyatnikov
 * @since 03.10.2025
 */
public abstract sealed class Parameter permits TerminalParameter, DependentParameter
{
    private final String name;

    Parameter(String name)
    {
        this.name = name;
    }

    /**
     * @return имя параметра
     */
    public String getName()
    {
        return name;
    }
}
