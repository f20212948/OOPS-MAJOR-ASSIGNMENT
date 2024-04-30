package com.major.Major_Assignment.Dao;

import com.major.Major_Assignment.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User , Integer>
{
    User findByEmail(String email);

    User findByUserID(int userID);
}
