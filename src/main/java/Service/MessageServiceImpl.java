package Service;

import Model.Message;
import DAO.MessageDao;

public class MessageServiceImpl implements MessageService {
    
    private MessageDao messageDao;

    public MessageServiceImpl(MessageDao messageDao) {
      this.messageDao = messageDao;
    }
  
    @Override
    public Message createMessage(Message message) {
      return messageDao.createMessage(message);
    }
}
