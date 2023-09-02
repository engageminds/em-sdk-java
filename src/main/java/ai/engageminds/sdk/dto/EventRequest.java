package ai.engageminds.sdk.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class EventRequest extends CommonRequest {
    private List<Event> events;

    public EventRequest addEvent(Event e) {
        if (events == null) {
            events = new ArrayList<>();
        }
        events.add(e);
        return this;
    }
}
