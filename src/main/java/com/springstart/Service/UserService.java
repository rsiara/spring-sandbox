package com.springstart.Service;

import com.springstart.Model.Entity.User;
import com.springstart.Model.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserDao userDao;

    public UserDao getUserDao() {
        return userDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public ResponseEntity<User[]> getAllUsers() {
        return userDao.getAllUsers();
    }

    public User getUserById(int id) {
        return userDao.getUserById(id);
    }

    public User createUser(User user) {
        return userDao.createUser(user);
    }

    public boolean exists(User user) {

        return userDao.isExist(user.getId());
    }
}