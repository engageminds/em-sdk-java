package ai.engageminds.sdk.dto;

import lombok.Data;

import java.util.List;

@Data
public class EventResponse {
    private int code;
    private String msg;
    private List<Event> events;
}
