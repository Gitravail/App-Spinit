package com.tournafond.raphael.spinit.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.tournafond.raphael.spinit.model.User;

@Dao
public interface UserDao {

    @Insert
    void createUser(User user);

    @Update
    int updateUser(User user);

    @Query("SELECT * FROM User LIMIT 1")
    LiveData<User> getUser();
}
