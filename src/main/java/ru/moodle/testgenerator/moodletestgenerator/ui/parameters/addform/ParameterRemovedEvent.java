package ru.moodle.testgenerator.moodletestgenerator.ui.parameters.addform;

import java.io.Serial;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * Событие удаления параметра
 *
 * @author dsyromyatnikov
 * @since 29.09.2025
 */
public final class ParameterRemovedEvent extends Event
{
    @Serial
    private static final long serialVersionUID = -2911629523495045944L;

    public static final EventType<ParameterRemovedEvent> REMOVE_PARAMETER = new EventType<>(Event.ANY, "REMOVE_PARAM");

    private final ParameterContainerView containerView;

    public ParameterRemovedEvent(ParameterContainerView parameterContainerView)
    {
        this(REMOVE_PARAMETER, parameterContainerView);
    }

    private ParameterRemovedEvent(EventType<? extends Event> eventType, ParameterContainerView containerView)
    {
        this.containerView = containerView;
        super(eventType);
    }

    /**
     * @return удаляемый параметр
     */
    public ParameterContainerView getParameter()
    {
        return containerView;
    }
}
