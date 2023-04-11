package models.dto.responses;

public class ResponseData extends Response {
    private final Object data;

    public ResponseData(int statusCode, String statusMessage, Object data) {
        super(statusCode, statusMessage);
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        return "ResponseData{" +
                "data=" + data +
                '}';
    }
}
