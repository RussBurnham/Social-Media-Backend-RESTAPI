package Service;

import Model.Account;
import DAO.AccountDao;

public class AccountService {

    private AccountDao accountDao;

    public AccountService(){
      accountDao = new AccountDao();
  }

    public AccountService(AccountDao accountDao) {
      this.accountDao = accountDao;
    }
    
    public Account loginAccount(Account account) {
        return accountDao.loginAccount(account);
    }

    public Account registerAccount(String username, String password) {
        if (username.isBlank() || password.length() < 4) {
            return null;
        }
        return accountDao.registerAccount(username, password);
    }

}
