package models.dto.requests.event;

public class EventTriggerSchedulingRequest {
    private final EventTriggerRequest request;
    private final String contractId;
    private final String contractInstanceId;

    public EventTriggerSchedulingRequest(
            EventTriggerRequest request,
            String contractId,
            String contractInstanceId
    ) {
        this.request = request;
        this.contractId = contractId;
        this.contractInstanceId = contractInstanceId;
    }

    public EventTriggerRequest getRequest() {
        return request;
    }

    public String getContractId() {
        return contractId;
    }

    public String getContractInstanceId() {
        return contractInstanceId;
    }

    @Override
    public String toString() {
        return "EventTriggerSchedulingRequest{" +
                "request=" + request +
                ", contractId='" + contractId + '\'' +
                ", contractInstanceId='" + contractInstanceId + '\'' +
                '}';
    }
}
