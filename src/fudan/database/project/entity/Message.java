package fudan.database.project.entity;

public class Message {
    private int messageId;
    private int toId;
    private int toType;
    private String messageContent;
    private int alreadyRead;

    public Message() {
        //
    }

    public Message(int toId, int toType, String messageContent, int alreadyRead) {
        this.toId = toId;
        this.toType = toType;
        this.messageContent = messageContent;
        this.alreadyRead = alreadyRead;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public int getToType() {
        return toType;
    }

    public void setToType(int toType) {
        this.toType = toType;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public int getAlreadyRead() {
        return alreadyRead;
    }

    public void setAlreadyRead(int alreadyRead) {
        this.alreadyRead = alreadyRead;
    }
}
