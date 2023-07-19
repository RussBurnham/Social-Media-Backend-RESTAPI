package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AccountDao {

    public Account registerAccount(Account account) { 
        Connection connection = ConnectionUtil.getConnection();
        try {
            
            String sql = "INSERT INTO account (username, password) VALUES (?, ?);";
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
    
          if (usernameExists(account.getUsername())) {
            return null;
          }
    
          ps.setString(1, account.getUsername());
          ps.setString(2, account.getPassword());
          ps.executeUpdate();
    
          try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
              int id = generatedKeys.getInt(1);
              account.setAccount_id(id);
              return account;
            }
          }
    
        } catch (SQLException e) {
            return null;
        }
        return null;
      }
    
      public boolean usernameExists(String username) {
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                 "SELECT COUNT(*) FROM account WHERE username = ?;"
             )) {
    
          ps.setString(1, username);
          try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
              int count = rs.getInt(1);
              return count > 0;
            }
          }
    
        } catch (SQLException ex) {
          return false;
        }
        return false;
      }
    
      public Account loginAccount(Account account) {
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                 "SELECT * FROM account WHERE username = ? AND password = ?"
             )) {
    
          ps.setString(1, account.getUsername());
          ps.setString(2, account.getPassword());
          try (ResultSet resultSet = ps.executeQuery()) {
            if (resultSet.next()) {
              int id = resultSet.getInt("account_id");
              return new Account(id, account.getUsername(), account.getPassword());
            }
          }
          return null;
    
        } catch (SQLException e) {
          return null;
        }
      }

}
