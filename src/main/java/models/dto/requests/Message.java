package models.dto.requests;

public class Message {
    private final String type;

    public Message(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type='" + type + '\'' +
                '}';
    }
}
