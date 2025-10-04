package ru.moodle.testgenerator.moodletestgenerator.uicomponents.parameters;

import java.io.Serial;

import javafx.event.Event;
import javafx.event.EventType;
import ru.moodle.testgenerator.moodletestgenerator.ParameterContainerView;

/**
 * @author dsyromyatnikov
 * @since 04.10.2025
 */
public abstract sealed class AbstractParameterEvent extends Event permits ParameterRemovedEvent, ParameterUpdateEvent
{
    @Serial
    private static final long serialVersionUID = 2951501306751576501L;

    /**
     * Представление параметра, с которым производятся манипуляции
     */
    private final transient ParameterContainerView parameterContainer;

    /**
     * @param eventType тип события
     * @param parameterContainer представление параметра
     */
    AbstractParameterEvent(EventType<? extends Event> eventType, ParameterContainerView parameterContainer)
    {
        super(eventType);
        this.parameterContainer = parameterContainer;
    }

    public ParameterContainerView getParameter()
    {
        return parameterContainer;
    }
}
