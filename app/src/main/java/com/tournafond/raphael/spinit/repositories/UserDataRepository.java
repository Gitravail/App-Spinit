package com.tournafond.raphael.spinit.repositories;

import android.arch.lifecycle.LiveData;

import com.tournafond.raphael.spinit.database.dao.UserDao;
import com.tournafond.raphael.spinit.model.User;

public class UserDataRepository {

    private final UserDao userDao;

    public UserDataRepository(UserDao userDao) { this.userDao = userDao; }

    // --- GET USER ---
    public LiveData<User> getUser() { return this.userDao.getUser(); }

    // --- CREATE USER ---
    public void createUser(User user) { this.userDao.createUser(user); }

    // --- UPDATE USER ---
    public int updateUser(User user) { return this.userDao.updateUser(user); }
}
