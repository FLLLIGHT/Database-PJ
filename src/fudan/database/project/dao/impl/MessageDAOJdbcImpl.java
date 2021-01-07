package fudan.database.project.dao.impl;

import fudan.database.project.dao.DAO;
import fudan.database.project.dao.MessageDAO;
import fudan.database.project.entity.Message;

import java.util.List;

public class MessageDAOJdbcImpl extends DAO<Message> implements MessageDAO {
    @Override
    public void save(Message message) {
        String sql = "INSERT INTO message(toId, toType, messageContent, alreadyRead) VALUES(?,?,?,?)";
        update(sql, message.getToId(), message.getToType(), message.getMessageContent(), message.getAlreadyRead());
    }

    @Override
    public List<Message> get(int toId, int toType) {
        String sql = "SELECT * FROM message WHERE toId = ? AND toType = ? AND alreadyRead = 0";
        return getForList(sql, toId, toType);
    }

    @Override
    public Message getByMessageId(int messageId) {
        String sql = "SELECT * FROM message WHERE messageId = ?";
        return get(sql, messageId);
    }

    @Override
    public void update(int toId) {
        String sql = "UPDATE message SET alreadyRead = 1 WHERE toId = ?";
        update(sql, toId);
    }
}
