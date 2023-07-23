package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDao {

    public Account loginAccount(Account account) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("account_id");
                Account loggedInAccount = new Account(id, account.getUsername(), account.getPassword());
                return loggedInAccount;
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    public Account registerAccount(String username, String password) {
        Account newAccount = null;
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "INSERT INTO account (username, password) VALUES (?, ?);";
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                int account_id = generatedKeys.getInt(1);
                newAccount = new Account(account_id, username, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newAccount;
    }

    public boolean usernameExists(String username) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM account WHERE username = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException ex) {
            return false;
        }
        return false;
    }
}
