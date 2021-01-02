package fudan.database.project.dao;

import fudan.database.project.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserDAOJdbcImpl extends DAO<User> implements UserDAO{

    @Override
    public User get(int uid) {
        String sql = "SELECT uid, userName, pass FROM user WHERE uid = ?";
        return get(sql, uid);
    }

    @Override
    public User get(String userName) {
        String sql = "SELECT uid, userName, pass FROM user WHERE userName = ?";
        return get(sql, userName);
    }
}
