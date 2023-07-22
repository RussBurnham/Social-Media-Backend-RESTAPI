package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

public class MessageDao {

    public Message createMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            if (!userExists(message.getPosted_by(), connection)) {
                return null;
            }
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.posted_by);
            preparedStatement.setString(2, message.message_text);
            preparedStatement.setLong(3, message.time_posted_epoch);

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_message_id = pkeyResultSet.getInt(1);
                return new Message(
                    generated_message_id, 
                    message.getPosted_by(), 
                    message.getMessage_text(), 
                    message.getTime_posted_epoch()
                );
            }
        } catch(SQLException ex){
            return null;
        }
        return null;
    }

    private boolean userExists(int account_id, Connection connection) throws SQLException {
        String sql = "SELECT COUNT(*) FROM account WHERE account_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, account_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            int count = resultSet.getInt(1);
            return count > 0; 
        }
        return false;
    }
    
    public boolean deleteMessage(int message_id) {
        try {   
            Connection connection = ConnectionUtil.getConnection();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM message WHERE message_id = ?");
            ps.setInt(1, message_id);
            ps.executeUpdate();
            return true;
        
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public Message getMessageById(int message_id) {
        Connection connection = ConnectionUtil.getConnection();       
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, message_id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int messageId = rs.getInt("message_id");
                int postedBy = rs.getInt("posted_by");
                String messageText = rs.getString("message_text");
                long timePostedEpoch = rs.getLong("time_posted_epoch");
                Message message = new Message(messageId, postedBy, messageText, timePostedEpoch);
                return message;
            }
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
        return null;
      }

    public List<Message> getMessageByAccountId(int posted_by) {
        List<Message> messageList = new ArrayList<>();
        Connection connection = ConnectionUtil.getConnection();       
        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, posted_by);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int messageId = rs.getInt("message_id");
                int postedBy = rs.getInt("posted_by");
                String messageText = rs.getString("message_text");
                long timePostedEpoch = rs.getLong("time_posted_epoch");
                Message message = new Message(messageId, postedBy, messageText, timePostedEpoch);
                messageList.add(message);
              }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return messageList;
    }

    public List<Message> getAllMessages() {
        List<Message> messageList = new ArrayList<>();
        Connection connection = ConnectionUtil.getConnection();       
        try {
            String sql = "SELECT * FROM message;";
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                int messageId = rs.getInt("message_id");
                int postedBy = rs.getInt("posted_by");
                String messageText = rs.getString("message_text");
                long timePostedEpoch = rs.getLong("time_posted_epoch");
                Message message = new Message(messageId, postedBy, messageText, timePostedEpoch);
                messageList.add(message);
              }    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return messageList;
    }

    public Message updateMessageById(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "UPDATE message SET posted_by = ?, message_text = ?, time_posted_epoch = ? WHERE message_id = ?;";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, message.posted_by);
            ps.setString(2, message.message_text);
            ps.setLong(3, message.time_posted_epoch);
            ps.setInt(4, message.getMessage_id()); 
            ps.executeUpdate();
            Message updatedMessage = getMessageById(message.getMessage_id()); 
            return updatedMessage;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
