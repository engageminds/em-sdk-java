package ai.engageminds.sdk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Event {
    private long ts;                   // Client time in milliseconds
    private String cdid;               // media user id
    private String eid;                // event ID
    private Map<String, Object> props; // event values
    private List<Err> err;             // 返回事件错误集合

    public Event setProp(String key, Object value) {
        if (props == null) {
            props = new HashMap<>();
        }
        props.put(key, value);
        return this;
    }

    @Data
    public static class Err {
        private String type;  // error type,
        private String prop;  // error property name
        private Object value; // property value
    }
}
