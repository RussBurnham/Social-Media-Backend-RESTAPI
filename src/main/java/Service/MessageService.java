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

    public void deleteMessage(int message_id) {
      messageDao.deleteMessage(message_id);
    }

}