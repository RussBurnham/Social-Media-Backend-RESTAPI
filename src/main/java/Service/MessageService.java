package Service;

import Model.Message;
import DAO.MessageDao;

public class MessageService {

    private MessageDao messageDao;

    public MessageService(){
      messageDao = new MessageDao();
  }

    public MessageService(MessageDao messageDao) {
      this.messageDao = messageDao;
    }

    public Message createMessage(Message message) {    
      if (message.getMessage_text().isBlank() || message.getMessage_text().length() > 254) {
        return null;
      }
      return messageDao.createMessage(message);
    }

    public boolean deleteMessage(int message_id) {
      return messageDao.deleteMessage(message_id);
    }

}