package com.cfranking.dao;

import com.cfranking.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao {

    private static final String USERS_KEY = "USER";

    @Autowired
    private HashOperations<String, String, User> hashOperations;

    public void persist(User user) {
        hashOperations.put(USERS_KEY, user.getHandle(), user);
    }

    public User getUser(String handle) {
        return hashOperations.get(USERS_KEY, handle);
    }

    public List<User> getUsers(List<String> handles) {
        return hashOperations.multiGet("USER", handles);
    }

    public void delete(String handle) {
        hashOperations.delete("USER", handle);
    }
}
