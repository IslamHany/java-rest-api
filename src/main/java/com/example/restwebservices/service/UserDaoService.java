package com.example.restwebservices.service;

import com.example.restwebservices.errors.UserNotFoundException;
import com.example.restwebservices.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class UserDaoService {
    private static List<User> users = new ArrayList<>();
    private static int usersCount;

    //loads at the time of class loading
    static {
        users.add(new User(1, "islam", new Date()));
        users.add(new User(2, "hany", new Date()));
        users.add(new User(3, "dody", new Date()));
        users.add(new User(4, "mohammed", new Date()));
        usersCount = users.size();
    }

    public List<User> findAll() {
        return users;
    }

    public User addUser(User user) {
        if (user.getId() == null) user.setId(++usersCount);
        users.add(user);

        return user;
    }

    public User findOne(int id){
        for (User user: users) {
            if (user.getId() == id) return user;
        }

        return null;
    }

    public User deleteById(int id){
        User deletedUser;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == id){
                deletedUser = users.get(i);
                users.remove(i);

                return deletedUser;
            }
        }

        throw new UserNotFoundException("" + id);
    }
}
