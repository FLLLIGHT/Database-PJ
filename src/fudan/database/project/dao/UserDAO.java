package fudan.database.project.dao;

import fudan.database.project.entity.User;

import java.util.List;

public interface UserDAO {

    public User get(int uid);

    public User get(String userName);

}
