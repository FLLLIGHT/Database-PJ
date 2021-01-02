package fudan.database.project.service;

import fudan.database.project.dao.UserDAO;
import fudan.database.project.dao.UserDAOJdbcImpl;
import fudan.database.project.entity.User;

public class AccountService {

    private UserDAO userDAO = new UserDAOJdbcImpl();

    public User checkUser(String userName, String pass){
        User user = userDAO.get(userName);
        if(user==null) {
            System.out.println("user not found");
            return null;
        }
        if(user.getPass().equals(pass)) {
            System.out.println("yes");
            return user;
        }
        System.out.println("password false");
        return null;
    }
}
