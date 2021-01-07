package fudan.database.project.dao;

import fudan.database.project.entity.Message;

import java.util.List;

public interface MessageDAO {
    public void save(Message message);
    public List<Message> get(int toId, int toType);
    public Message getByMessageId(int messageId);
    public void update(int toId);
}
