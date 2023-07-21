package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

    // private boolean messageExists(int message_id, Connection connection) throws SQLException {
    //     String sql = "SELECT COUNT(*) FROM message WHERE message_id = ?";
    //     PreparedStatement preparedStatement = connection.prepareStatement(sql);
    //     preparedStatement.setInt(1, message_id);
    //     ResultSet resultSet = preparedStatement.executeQuery();
    //     if (resultSet.next()) {
    //         int count = resultSet.getInt(1);
    //         return count > 0;
    //     }
    //     return false;
    // }


}
