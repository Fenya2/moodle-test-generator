package ru.moodle.testgenerator.moodletestgenerator.uicomponents.parameters;

import java.io.Serial;

import javafx.event.Event;
import javafx.event.EventType;
import ru.moodle.testgenerator.moodletestgenerator.ParameterContainerView;

/**
 * @author dsyromyatnikov
 * @since 04.10.2025
 */
public final class ParameterUpdateEvent extends AbstractParameterEvent
{
    @Serial
    private static final long serialVersionUID = -8055303502492649011L;

    private static final EventType<ParameterRemovedEvent> REMOVE_PARAMETER = new EventType<>(Event.ANY, "REMOVE PARAM");

    /**
     * @param parameterContainerView представление изменяемого параметра
     */
    public ParameterUpdateEvent(ParameterContainerView parameterContainerView)
    {
        super(REMOVE_PARAMETER, parameterContainerView);
    }
}
