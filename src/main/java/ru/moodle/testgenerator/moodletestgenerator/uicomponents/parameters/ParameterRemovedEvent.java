package ru.moodle.testgenerator.moodletestgenerator.uicomponents.parameters;

import java.io.Serial;

import javafx.event.Event;
import javafx.event.EventType;
import ru.moodle.testgenerator.moodletestgenerator.ParameterContainerView;

/**
 * Событие удаления параметра
 *
 * @author dsyromyatnikov
 * @since 29.09.2025
 */
public final class ParameterRemovedEvent extends AbstractParameterEvent
{
    @Serial
    private static final long serialVersionUID = -2911629523495045944L;

    public static final EventType<ParameterRemovedEvent> REMOVE_PARAMETER = new EventType<>(Event.ANY, "REMOVE PARAM");

    public ParameterRemovedEvent(ParameterContainerView parameter)
    {
        super(REMOVE_PARAMETER, parameter);
    }
}
