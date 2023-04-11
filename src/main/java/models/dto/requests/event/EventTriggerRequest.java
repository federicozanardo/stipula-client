package models.dto.requests.event;

public class EventTriggerRequest {
    private final String obligationFunctionName;
    private final int time;

    public EventTriggerRequest(String obligationFunctionName, int time) {
        this.obligationFunctionName = obligationFunctionName;
        this.time = time;
    }

    public String getObligationFunctionName() {
        return obligationFunctionName;
    }

    public int getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "EventTriggerRequest{" +
                "obligationFunctionName='" + obligationFunctionName + '\'' +
                ", time=" + time +
                '}';
    }
}
