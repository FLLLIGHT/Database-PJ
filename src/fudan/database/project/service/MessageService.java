package fudan.database.project.service;

import fudan.database.project.dao.MessageDAO;
import fudan.database.project.dao.impl.MessageDAOJdbcImpl;
import fudan.database.project.entity.Message;

import java.util.List;

public class MessageService {
    MessageDAO messageDAO = new MessageDAOJdbcImpl();

    public void sendMessage(int toId, int toType, String messageContent){
        Message message = new Message(toId, toType, messageContent, 0);
        messageDAO.save(message);
    }

    public List<Message> getUnreadMessages(int toId, int toType){
        return messageDAO.get(toId, toType);
    }

    public void markMessageAsRead(int messageId){
        Message message = messageDAO.getByMessageId(messageId);
        message.setAlreadyRead(1);
        messageDAO.save(message);
    }
}
