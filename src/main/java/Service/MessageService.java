package Service;

import Model.Message;
import DAO.MessageDao;
import java.util.List;

public class MessageService {

	private MessageDao messageDao;

	public MessageService() {
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

	public Message getMessageById(int message_id) {
		return messageDao.getMessageById(message_id);
	}

	public List<Message> getMessageByAccountId(int posted_by) {
		return messageDao.getMessageByAccountId(posted_by);
	}

	public List<Message> getAllMessages() {
		return messageDao.getAllMessages();
	}

	public Message updateMessageById(Message message) {
		return messageDao.updateMessageById(message);
	}

}